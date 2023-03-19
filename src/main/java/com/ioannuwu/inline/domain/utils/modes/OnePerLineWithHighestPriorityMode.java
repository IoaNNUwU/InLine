package com.ioannuwu.inline.domain.utils.modes;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.ioannuwu.inline.domain.utils.RenderElementsProvider;
import com.ioannuwu.inline.ui.render.EditorElementsRenderer;
import com.ioannuwu.inline.ui.render.elements.RenderElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class OnePerLineWithHighestPriorityMode implements Mode {

    private final EditorElementsRenderer editorElementsRenderer;
    private final RenderElementsProvider renderDataProvider;

    private final ArrayList<Entity> list = new ArrayList<>();

    public OnePerLineWithHighestPriorityMode(RenderElementsProvider renderDataProvider,
                                             EditorElementsRenderer editorElementsRenderer) {
        this.editorElementsRenderer = editorElementsRenderer;
        this.renderDataProvider = renderDataProvider;
    }

    @Override
    public void afterAdded(RangeHighlighter highlighter) {
        Collection<RenderElement> renderElements = renderDataProvider.provide(highlighter, 0);
        if (renderElements.isEmpty()) return; // Ignore redundant

        int currentLine = highlighter.getDocument().getLineNumber(highlighter.getStartOffset());
        // Add current entity to the list
        Entity currentEntity = new Entity(highlighter, currentLine, null);
        list.add(currentEntity);
        // find all entities on same line - WITH CURRENT - IT WAS ADDED TO LIST BUT NOT RENDERED
        HashSet<Entity> setOnSameLine = new HashSet<>();
        for (final var entity : list) {
            if (entity.initialLine == currentLine) setOnSameLine.add(entity);
        }
        // find the highest entity - WITH CURRENT - IT WAS ADDED TO LIST BUT NOT RENDERED
        Entity highestEntity = null;
        int highestMyVal = 0;
        for (final var entity : setOnSameLine) {
            HighlightInfo info = ((HighlightInfo) entity.rangeHighlighter.getErrorStripeTooltip());
            int currentVal = info.getSeverity().myVal;
            if (currentVal > highestMyVal) {
                highestEntity = entity;
                highestMyVal = currentVal;
            }
        }
        assert highestEntity != null; // because current entity was added to list
        // UnRender all on current line - WITH CURRENT - IT WAS ADDED TO LIST BUT NOT RENDERED
        for (final var entity : setOnSameLine) {
            editorElementsRenderer.unRender(entity.renderElements);
        }
        // Render highest and put proper render elements in the highest entity
        Collection<RenderElement> highestRenderElements = renderDataProvider.provide(highestEntity.rangeHighlighter, 1);
        if (highestRenderElements.isEmpty()) {
            return;
        }
        highestEntity.renderElements = editorElementsRenderer.render(highestRenderElements);
    }

    @Override
    public void beforeRemoved(RangeHighlighter highlighter) {
        // find if highlighter is in the list
        Entity finalEntity = null;
        for (final var entity : list) {
            if (entity.rangeHighlighter == highlighter) finalEntity = entity;
        }
        if (finalEntity == null) return; // if not in the list ignore - RETURN
        // RangeHighlighter highlighter is in the list
        int currentLine = finalEntity.initialLine;
        Set<Disposable> elements = finalEntity.renderElements;
        // In any scenario should UNRENDER and REMOVE this highlighter
        editorElementsRenderer.unRender(elements);
        list.remove(finalEntity);
        // Find on same line - WITHOUT CURRENT - IT WAS DELETED AND UNRENDERED
        HashSet<Entity> setOnSameLine = new HashSet<>();
        for (final var entity : list) {
            if (entity.initialLine == currentLine) setOnSameLine.add(entity);
        }
        // If there is no highlighters on that line do nothing - RETURN
        if (setOnSameLine.isEmpty()) return;
        // Find with the highest priority - set has at least one
        // WITHOUT CURRENT - IT WAS DELETED AND UNRENDERED
        Entity highestEntity = null;
        int highestMyVal = 0;
        for (final var entity : setOnSameLine) {
            HighlightInfo info = ((HighlightInfo) entity.rangeHighlighter.getErrorStripeTooltip());
            int currentVal = info.getSeverity().myVal;
            if (currentVal > highestMyVal) {
                highestEntity = entity;
                highestMyVal = currentVal;
            }
        }
        assert highestEntity != null; // because set was NOT EMPTY
        Collection<RenderElement> renderElements = renderDataProvider.provide(highestEntity.rangeHighlighter, 0);

        if (renderElements.isEmpty()) {
            return;
        }

        // UnRender all on same line - WITHOUT CURRENT - IT WAS DELETED AND UNRENDERED
        for (final var entity : setOnSameLine) {
            editorElementsRenderer.unRender(entity.renderElements);
            entity.renderElements = null;
        }
        // Render the highest highlighter and put in the highest entity
        highestEntity.renderElements = editorElementsRenderer.render(renderElements);
    }

    private static class Entity {
        public final @NotNull RangeHighlighter rangeHighlighter;
        public final int initialLine;

        public @Nullable Set<Disposable> renderElements;

        private Entity(@NotNull RangeHighlighter rangeHighlighter, int initialLine, @Nullable Set<Disposable> renderElements) {
            this.rangeHighlighter = rangeHighlighter;
            this.initialLine = initialLine;
            this.renderElements = renderElements;
        }
    }
}

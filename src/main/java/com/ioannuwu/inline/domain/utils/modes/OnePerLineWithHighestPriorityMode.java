package com.ioannuwu.inline.domain.utils.modes;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.ioannuwu.inline.domain.render.RenderData;
import com.ioannuwu.inline.ui.render.EditorElementsRenderer;
import com.ioannuwu.inline.domain.utils.RenderDataProvider;
import com.ioannuwu.inline.domain.render.RenderElements;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.set.MutableSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OnePerLineWithHighestPriorityMode implements Mode {

    private final EditorElementsRenderer editorElementsRenderer;
    private final RenderDataProvider renderDataProvider;

    private final MutableList<Entity> list = Lists.mutable.of();

    public OnePerLineWithHighestPriorityMode(RenderDataProvider renderDataProvider, EditorElementsRenderer editorElementsRenderer) {
        this.editorElementsRenderer = editorElementsRenderer;
        this.renderDataProvider = renderDataProvider;
    }

    @Override
    public void afterAdded(RangeHighlighter highlighter) {
        RenderData renderData = renderDataProvider.provide(highlighter);
        if (renderData == null) return; // Ignore redundant

        int currentLine = highlighter.getDocument().getLineNumber(highlighter.getStartOffset());
        // Add current entity to the list
        Entity currentEntity = new Entity(highlighter, currentLine, null);
        list.add(currentEntity);
        // find all entities on same line - WITH CURRENT - IT WAS ADDED TO LIST BUT NOT RENDERED
        MutableSet<Entity> setOnSameLine = Sets.mutable.of();
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
        RenderData highestRenderData = renderDataProvider.provide(highestEntity.rangeHighlighter);
        if (highestRenderData == null) {
            // TODO render data should never be null because it filtered at the start of the method
            // but for some reason that works
            return;
        }
        highestEntity.renderElements = editorElementsRenderer.render(highestRenderData, highestEntity.rangeHighlighter.getStartOffset());
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
        RenderElements elements = finalEntity.renderElements;
        // In any scenario should UNRENDER and REMOVE this highlighter
        editorElementsRenderer.unRender(elements);
        list.remove(finalEntity);
        // Find on same line - WITHOUT CURRENT - IT WAS DELETED AND UNRENDERED
        MutableSet<Entity> setOnSameLine = Sets.mutable.of();
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
        RenderData renderData = renderDataProvider.provide(highestEntity.rangeHighlighter);

        if (renderData == null) {
            // TODO render data should never be null because it filtered at the start of afterAdded method
            // but for some reason that works
            return;
        }

        // UnRender all on same line - WITHOUT CURRENT - IT WAS DELETED AND UNRENDERED
        for (final var entity : setOnSameLine) {
            editorElementsRenderer.unRender(entity.renderElements);
            entity.renderElements = null;
        }
        // Render the highest highlighter and put in the highest entity
        highestEntity.renderElements = editorElementsRenderer.render(renderData, highestEntity.rangeHighlighter.getStartOffset());
    }

    private static class Entity {
        public final @NotNull RangeHighlighter rangeHighlighter;
        public final int initialLine;

        public @Nullable RenderElements renderElements;

        private Entity(@NotNull RangeHighlighter rangeHighlighter, int initialLine, @Nullable RenderElements renderElements) {
            this.rangeHighlighter = rangeHighlighter;
            this.initialLine = initialLine;
            this.renderElements = renderElements;
        }
    }
}

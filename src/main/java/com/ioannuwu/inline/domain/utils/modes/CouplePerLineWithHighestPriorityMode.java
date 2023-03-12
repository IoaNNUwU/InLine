package com.ioannuwu.inline.domain.utils.modes;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.ioannuwu.inline.domain.utils.MaxPerLine;
import com.ioannuwu.inline.domain.utils.RenderElementsProvider;
import com.ioannuwu.inline.ui.render.EditorElementsRenderer;
import com.ioannuwu.inline.ui.render.elements.RenderElement;

import java.util.*;
import java.util.stream.Collectors;

public class CouplePerLineWithHighestPriorityMode implements Mode {

    private final EditorElementsRenderer editorElementsRenderer;
    private final RenderElementsProvider renderElementsProvider;

    private final MaxPerLine maxPerLine;

    private final Comparator<Entity> comparator;

    private final ArrayList<Entity> list = new ArrayList<>();

    public CouplePerLineWithHighestPriorityMode(RenderElementsProvider renderElementsProvider,
                                                EditorElementsRenderer editorElementsRenderer,
                                                MaxPerLine maxPerLine, Comparator<Entity> comparator) {
        this.editorElementsRenderer = editorElementsRenderer;
        this.renderElementsProvider = renderElementsProvider;
        this.maxPerLine = maxPerLine;
        this.comparator = comparator;
    }

    @SuppressWarnings("unchecked cast")
    @Override
    public void afterAdded(RangeHighlighter highlighter) {
        Collection<RenderElement> renderElements = renderElementsProvider.provide(highlighter);
        if (renderElements.isEmpty()) return; // Ignore redundant

        int currentLine = highlighter.getDocument().getLineNumber(highlighter.getStartOffset());

        Entity currentEntity = new Entity(highlighter, currentLine, null);
        list.add(currentEntity);

        List<Entity> entitiesOnCurrentLineSorted = ((ArrayList<Entity>) list.clone()).stream()
                .filter(entity -> entity.initialLine == currentLine)
                .filter(myEntity -> myEntity.rangeHighlighter.getStartOffset() > 0)
                .sorted(comparator)
                .collect(Collectors.toList());

        entitiesOnCurrentLineSorted.forEach(entity -> {
            editorElementsRenderer.unRender(entity.renderElements);
            entity.renderElements = null;
        });

        List<Entity> top = entitiesOnCurrentLineSorted.stream()
                .limit(maxPerLine.maxPerLine())
                .collect(Collectors.toList());

        for (final var entity : top) {
            Collection<RenderElement> elements = renderElementsProvider.provide(entity.rangeHighlighter);
            if (elements.isEmpty()) continue; // WHY
            int startOffset = entity.rangeHighlighter.getStartOffset();
            if (startOffset > highlighter.getDocument().getTextLength()) return;
            entity.renderElements = editorElementsRenderer.render(elements, startOffset);
        }
    }

    @Override
    public void beforeRemoved(RangeHighlighter highlighter) {
        Optional<Entity> opEntity = list.stream().filter((myEntity -> myEntity.rangeHighlighter == highlighter)).findAny();
        if (opEntity.isEmpty()) return;
        Entity entity = opEntity.get();

        Collection<RenderElement> renderElements = renderElementsProvider.provide(highlighter);
        if (renderElements.isEmpty()) return; // WHY

        int currentLine = entity.initialLine;
        Set<Disposable> elements = entity.renderElements;
        editorElementsRenderer.unRender(elements);
        list.remove(entity);

        if (highlighter.getStartOffset() > highlighter.getDocument().getTextLength()) return;

        List<Entity> entitiesOnCurrentLineSorted = list.stream()
                .filter(myEntity -> myEntity.initialLine == currentLine)
                .filter(myEntity -> myEntity.rangeHighlighter.getStartOffset() > 0)
                .sorted(comparator)
                .collect(Collectors.toList());

        entitiesOnCurrentLineSorted.forEach(myEntity -> {
            editorElementsRenderer.unRender(myEntity.renderElements);
            myEntity.renderElements = null;
        });

        List<Entity> top = entitiesOnCurrentLineSorted.stream()
                .limit(maxPerLine.maxPerLine())
                .collect(Collectors.toList());

        for (final var myEntity : top) {
            Collection<RenderElement> elementCollection = renderElementsProvider.provide(myEntity.rangeHighlighter);
            if (elementCollection.isEmpty()) continue; // WHY
            if (myEntity.rangeHighlighter.getStartOffset() <= 0) continue;
            myEntity.renderElements = editorElementsRenderer.render(elementCollection, myEntity.rangeHighlighter.getStartOffset());
        }
    }
}

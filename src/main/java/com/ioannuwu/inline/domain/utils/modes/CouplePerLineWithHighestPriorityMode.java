package com.ioannuwu.inline.domain.utils.modes;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.ioannuwu.inline.domain.render.RenderData;
import com.ioannuwu.inline.domain.render.RenderElements;
import com.ioannuwu.inline.domain.utils.RenderDataProvider;
import com.ioannuwu.inline.ui.render.EditorElementsRenderer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CouplePerLineWithHighestPriorityMode implements Mode {

    private final EditorElementsRenderer editorElementsRenderer;
    private final RenderDataProvider renderDataProvider;

    private final Comparator<Entity> comparator;

    private final ArrayList<Entity> list = new ArrayList<>();

    public CouplePerLineWithHighestPriorityMode(RenderDataProvider renderDataProvider, EditorElementsRenderer editorElementsRenderer, Comparator<Entity> comparator) {
        this.editorElementsRenderer = editorElementsRenderer;
        this.renderDataProvider = renderDataProvider;
        this.comparator = comparator;
    }

    @Override
    public void afterAdded(RangeHighlighter highlighter) {
        RenderData myData = renderDataProvider.provide(highlighter);
        if (myData == null) return; // Ignore redundant

        int currentLine = highlighter.getDocument().getLineNumber(highlighter.getStartOffset());

        Entity currentEntity = new Entity(highlighter, currentLine, null);
        list.add(currentEntity);

        List<Entity> entitiesOnCurrentLineSorted = list.stream()
                .filter(entity -> entity.initialLine == currentLine)
                .filter(myEntity -> myEntity.rangeHighlighter.getStartOffset() > 0)
                .sorted(comparator)
                .collect(Collectors.toList());

        entitiesOnCurrentLineSorted.forEach(entity -> {
            editorElementsRenderer.unRender(entity.renderElements);
            entity.renderElements = null;
        });

        List<Entity> top = entitiesOnCurrentLineSorted.stream()
                .limit(myData.maxErrorsPerLine)
                .collect(Collectors.toList());

        for (final var entity : top) {
            RenderData data = renderDataProvider.provide(entity.rangeHighlighter);
            if (data == null) continue; // WHY
            int startOffset = entity.rangeHighlighter.getStartOffset();
            if (startOffset > highlighter.getDocument().getTextLength()) return;
            entity.renderElements = editorElementsRenderer.render(data, startOffset);
        }
    }

    @Override
    public void beforeRemoved(RangeHighlighter highlighter) {
        Optional<Entity> opEntity = list.stream().filter((myEntity -> myEntity.rangeHighlighter == highlighter)).findAny();
        if (opEntity.isEmpty()) return;
        Entity entity = opEntity.get();

        RenderData myData = renderDataProvider.provide(highlighter);
        if (myData== null) return; // WHY

        int currentLine = entity.initialLine;
        RenderElements elements = entity.renderElements;
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
                .limit(myData.maxErrorsPerLine)
                .collect(Collectors.toList());

        for (final var myEntity : top) {
            RenderData data = renderDataProvider.provide(myEntity.rangeHighlighter);
            if (data == null) continue; // WHY
            myEntity.renderElements = editorElementsRenderer.render(data, myEntity.rangeHighlighter.getStartOffset());
        }
    }
}

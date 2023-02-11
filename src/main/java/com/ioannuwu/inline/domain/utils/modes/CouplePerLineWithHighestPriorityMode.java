package com.ioannuwu.inline.domain.utils.modes;

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

    private final int MAX_PER_LINE;

    public CouplePerLineWithHighestPriorityMode(RenderDataProvider renderDataProvider, EditorElementsRenderer editorElementsRenderer, Comparator<Entity> comparator, int max_per_line) {
        this.editorElementsRenderer = editorElementsRenderer;
        this.renderDataProvider = renderDataProvider;
        this.comparator = comparator;
        MAX_PER_LINE = max_per_line;
    }

    @Override
    public void afterAdded(RangeHighlighter highlighter) {
        if (renderDataProvider.provide(highlighter) == null) return; // Ignore redundant

        int currentLine = highlighter.getDocument().getLineNumber(highlighter.getStartOffset());

        Entity currentEntity = new Entity(highlighter, currentLine, null);
        list.add(currentEntity);

        List<Entity> entitiesOnCurrentLineSorted = list.stream()
                .filter(entity -> entity.initialLine == currentLine)
                .sorted(comparator)
                .collect(Collectors.toList());

        entitiesOnCurrentLineSorted.forEach(System.out::println);

        entitiesOnCurrentLineSorted.forEach(entity -> {
            editorElementsRenderer.unRender(entity.renderElements);
            entity.renderElements = null;
        });

        List<Entity> top = entitiesOnCurrentLineSorted.stream()
                .limit(MAX_PER_LINE)
                .collect(Collectors.toList());

        for (final var entity : top) {
            RenderData data = renderDataProvider.provide(entity.rangeHighlighter);
            if (data == null) continue; // TODO WHY
            entity.renderElements = editorElementsRenderer.render(data, entity.rangeHighlighter.getStartOffset());
        }
    }

    @Override
    public void beforeRemoved(RangeHighlighter highlighter) {
        Optional<Entity> opEntity = list.stream().filter((myEntity -> myEntity.rangeHighlighter == highlighter)).findAny();
        if (opEntity.isEmpty()) return;
        Entity entity = opEntity.get();

        int currentLine = entity.initialLine;
        RenderElements elements = entity.renderElements;
        editorElementsRenderer.unRender(elements);
        list.remove(entity);

        if (highlighter.getStartOffset() > highlighter.getDocument().getTextLength()) return;

        List<Entity> entitiesOnCurrentLineSorted = list.stream()
                .filter(myEntity -> myEntity.initialLine == currentLine)
                .sorted(comparator)
                .collect(Collectors.toList());

        entitiesOnCurrentLineSorted.forEach(System.out::println);

        entitiesOnCurrentLineSorted.forEach(myEntity -> {
            editorElementsRenderer.unRender(myEntity.renderElements);
            myEntity.renderElements = null;
        });

        List<Entity> top = entitiesOnCurrentLineSorted.stream().limit(MAX_PER_LINE).collect(Collectors.toList());

        for (final var myEntity : top) {
            RenderData data = renderDataProvider.provide(myEntity.rangeHighlighter);
            if (data == null) continue; // TODO WHY
            myEntity.renderElements = editorElementsRenderer.render(data, myEntity.rangeHighlighter.getStartOffset());
        }
    }
}

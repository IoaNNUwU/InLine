package com.ioannuwu.inline.domain.utils.modes;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.ioannuwu.inline.domain.utils.MaxPerLine;
import com.ioannuwu.inline.domain.utils.RenderElementsProvider;
import com.ioannuwu.inline.ui.render.EditorElementsRenderer;
import com.ioannuwu.inline.ui.render.elements.RenderElement;
import com.ioannuwu.inline.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

public class CouplePerLineWithHighestPriorityMode implements Mode {

    private final EditorElementsRenderer editorElementsRenderer;
    private final RenderElementsProvider renderElementsProvider;

    private final MaxPerLine maxPerLine;

    private final ArrayList<Entity> list = new ArrayList<>();

    public CouplePerLineWithHighestPriorityMode(RenderElementsProvider renderElementsProvider,
                                                EditorElementsRenderer editorElementsRenderer,
                                                MaxPerLine maxPerLine) {
        this.editorElementsRenderer = editorElementsRenderer;
        this.renderElementsProvider = renderElementsProvider;
        this.maxPerLine = maxPerLine;
    }

    @SuppressWarnings("unchecked cast")
    @Override
    public void afterAdded(RangeHighlighter highlighter) {
        Collection<RenderElement> renderElements = renderElementsProvider.provide(highlighter, 0);
        if (renderElements.isEmpty()) return; // Ignore redundant

        System.out.println("ADD: " + ((HighlightInfo) highlighter.getErrorStripeTooltip()).getDescription());
        int currentLine = highlighter.getDocument().getLineNumber(highlighter.getStartOffset());

        Entity currentEntity = new Entity(highlighter, currentLine, null);
        list.add(currentEntity);

        List<Entity> entitiesOnCurrentLineSorted = ((ArrayList<Entity>) list.clone()).stream()
                .filter(entity -> entity.initialLine == currentLine)
                .filter(myEntity -> myEntity.rangeHighlighter.getStartOffset() > 0)
                .sorted(Utils.ENTITY_COMPARATOR)
                .collect(Collectors.toList());

        entitiesOnCurrentLineSorted.forEach(entity -> {
            editorElementsRenderer.unRender(entity.renderElements);
            entity.renderElements = null;
        });

        List<Entity> top = entitiesOnCurrentLineSorted.stream()
                .limit(maxPerLine.maxPerLine())
                .sorted(Utils.ENTITY_COMPARATOR_BY_OFFSET)
                .collect(Collectors.toList());

        int topSize = top.size();

        System.out.println("   top: " + top.stream()
                .map(ent -> ((HighlightInfo) ent.rangeHighlighter.getErrorStripeTooltip()).getDescription()).collect(Collectors.toList()));

        System.out.println("   all" + entitiesOnCurrentLineSorted.stream()
                .map(ent -> ((HighlightInfo) ent.rangeHighlighter.getErrorStripeTooltip()).getDescription()).collect(Collectors.toList()));


        for (int i = 0; i < topSize; i++) {
            Entity entity = top.get(i);
            Collection<RenderElement> elements = renderElementsProvider.provide(entity.rangeHighlighter, i);
            if (elements.isEmpty()) continue; // WHY
            int startOffset = entity.rangeHighlighter.getStartOffset();
            if (startOffset > highlighter.getDocument().getTextLength()) return;
            entity.renderElements = editorElementsRenderer.render(elements);
        }
    }

    @Override
    public void beforeRemoved(RangeHighlighter highlighter) {
        Optional<Entity> opEntity = list.stream().filter((myEntity -> myEntity.rangeHighlighter == highlighter)).findAny();
        if (opEntity.isEmpty()) return;
        Entity entity = opEntity.get();
        System.out.println("REM: " + ((HighlightInfo) highlighter.getErrorStripeTooltip()).getDescription());

        int currentLine = entity.initialLine;
        Set<Disposable> elements = entity.renderElements;
        editorElementsRenderer.unRender(elements);
        list.remove(entity);

        if (highlighter.getStartOffset() > highlighter.getDocument().getTextLength()) return;

        List<Entity> entitiesOnCurrentLineSorted = list.stream()
                .filter(myEntity -> myEntity.initialLine == currentLine)
                .filter(myEntity -> myEntity.rangeHighlighter.getStartOffset() > 0)
                .sorted(Utils.ENTITY_COMPARATOR)
                .collect(Collectors.toList());

        entitiesOnCurrentLineSorted.forEach(myEntity -> {
            editorElementsRenderer.unRender(myEntity.renderElements);
            myEntity.renderElements = null;
        });

        List<Entity> top = entitiesOnCurrentLineSorted.stream()
                .limit(maxPerLine.maxPerLine())
                .sorted(Utils.ENTITY_COMPARATOR_BY_OFFSET)
                .collect(Collectors.toList());

        System.out.println("   top: " + top.stream()
                .map(ent -> ((HighlightInfo) ent.rangeHighlighter.getErrorStripeTooltip()).getDescription()).collect(Collectors.toList()));
        System.out.println("   all" + entitiesOnCurrentLineSorted.stream()
                .map(ent -> ((HighlightInfo) ent.rangeHighlighter.getErrorStripeTooltip()).getDescription()).collect(Collectors.toList()));

        int topSize = top.size();

        for (int i = 0; i < topSize; i++) {
            Entity myEntity = top.get(i);
            Collection<RenderElement> elementCollection = renderElementsProvider.provide(myEntity.rangeHighlighter, i);
            if (elementCollection.isEmpty()) continue; // WHY
            if (myEntity.rangeHighlighter.getStartOffset() <= 0) continue;
            myEntity.renderElements = editorElementsRenderer.render(elementCollection);
        }
    }
}

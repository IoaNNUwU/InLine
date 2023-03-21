package com.ioannuwu.inline.domain.utils.modes;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.ioannuwu.inline.domain.utils.MaxPerLine;
import com.ioannuwu.inline.domain.utils.RenderElementsProvider;
import com.ioannuwu.inline.ui.render.EditorElementsRenderer;
import com.ioannuwu.inline.ui.render.elements.RenderElement;
import com.ioannuwu.inline.utils.Utils;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CouplePerLineWithHighestPriorityMode implements Mode {

    private final EditorElementsRenderer editorElementsRenderer;
    private final RenderElementsProvider renderElementsProvider;

    private final MaxPerLine maxPerLine;

    private final HashMap<RangeHighlighter, Set<Disposable>> map = new HashMap<>();

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
        if (renderElements.isEmpty()) return;
        map.put(highlighter, Collections.EMPTY_SET);

        final Document document = highlighter.getDocument();

        // We can use entrySet if performance is an issue but code is cleaner without it
        List<RangeHighlighter> list = map.keySet().stream()
                .filter(isOutOfBounds(document.getTextLength()))
                .collect(Collectors.toList());
        list.forEach(h -> {
            System.out.println("HIGHLIGHTER INVALID IN FN REMOVE * DBG");
            editorElementsRenderer.unRender(map.remove(h));
        });

        int lineNumber = document.getLineNumber(highlighter.getStartOffset());

        List<RangeHighlighter> highlightersOnCurrentLineSorted = map.keySet().stream()
                .filter(isOnLineNumber(lineNumber))
                .sorted(RangeHighlighterComparator.BY_SEVERITY_THEN_OFFSET_THEN_DESCRIPTION)
                .collect(Collectors.toList());

        highlightersOnCurrentLineSorted.forEach(h -> {
            editorElementsRenderer.unRender(map.get(h));
            map.put(h, Collections.EMPTY_SET);
        });

        List<RangeHighlighter> top = highlightersOnCurrentLineSorted.stream()
                .limit(maxPerLine.maxPerLine())
                .sorted(RangeHighlighterComparator.BY_OFFSET_REVERSED)
                .collect(Collectors.toList());

        System.out.println("   top: " + top.stream()
                .map(h -> ((HighlightInfo) h.getErrorStripeTooltip()).getDescription())
                .collect(Collectors.joining(",", "[", "]")));
        System.out.println("   all" + highlightersOnCurrentLineSorted.stream()
                .map(h -> ((HighlightInfo) h.getErrorStripeTooltip()).getDescription())
                .collect(Collectors.joining(",", "[", "]")));


        for (int i = 0; i < top.size(); i++) {
            RangeHighlighter rangeHighlighter = top.get(i);
            Collection<RenderElement> elements = renderElementsProvider.provide(rangeHighlighter, i);
            map.put(rangeHighlighter, editorElementsRenderer.render(elements));
        }
    }

    @Override
    public void beforeRemoved(RangeHighlighter highlighter) {

        editorElementsRenderer.unRender(map.remove(highlighter));

        Document document = highlighter.getDocument();

        List<RangeHighlighter> list = map.keySet().stream()
                .filter(isOutOfBounds(document.getTextLength()))
                .collect(Collectors.toList());
        list.forEach(h -> {
            System.out.println("HIGHLIGHTER INVALID IN FN REMOVE * DBG");
            editorElementsRenderer.unRender(map.remove(h));
        });

        // if current highlighter is out of bounds last line will be rerendered
        boolean isCurrentHighlighterOutOfBounds = isOutOfBounds(document.getTextLength()).test(highlighter);

        int lineNumber = isCurrentHighlighterOutOfBounds ? document.getLineCount()
                : document.getLineNumber(highlighter.getStartOffset());

        List<RangeHighlighter> highlightersOnCurrentLineSorted = map.keySet().stream()
                .filter(isOnLineNumber(lineNumber))
                .sorted(RangeHighlighterComparator.BY_SEVERITY_THEN_OFFSET_THEN_DESCRIPTION)
                .collect(Collectors.toList());

        highlightersOnCurrentLineSorted.forEach(h -> {
            editorElementsRenderer.unRender(map.get(h));
            map.put(h, Collections.EMPTY_SET);
        });

        List<RangeHighlighter> top = highlightersOnCurrentLineSorted.stream()
                .limit(maxPerLine.maxPerLine())
                .sorted(RangeHighlighterComparator.BY_OFFSET_REVERSED)
                .collect(Collectors.toList());

        System.out.println("   top: " + top.stream()
                .map(h -> ((HighlightInfo) h.getErrorStripeTooltip()).getDescription())
                .collect(Collectors.joining(",", "[", "]")));
        System.out.println("   all" + highlightersOnCurrentLineSorted.stream()
                .map(h -> ((HighlightInfo) h.getErrorStripeTooltip()).getDescription())
                .collect(Collectors.joining(",", "[", "]")));

        for (int i = 0; i < top.size(); i++) {
            RangeHighlighter rangeHighlighter = top.get(i);
            Collection<RenderElement> elementCollection = renderElementsProvider.provide(rangeHighlighter, i);
            map.put(rangeHighlighter, editorElementsRenderer.render(elementCollection));
        }
    }

    private static Predicate<RangeHighlighter> isOutOfBounds(int textLength) {
        return h -> h.getStartOffset() < 0 || h.getStartOffset() > textLength;
    }

    private static Predicate<RangeHighlighter> isOnLineNumber(int lineNumber) {
        return h -> h.getDocument().getLineNumber(h.getStartOffset()) == lineNumber;
    }
}

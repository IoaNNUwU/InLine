package com.ioannuwu.inline.domain.utils;

import com.intellij.completion.ngram.slp.util.Pair;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class RenderElementsRangeHighlighterAndLineAccessMap implements MyRangeHighlighterAndLineAccessMap<RenderElements> {

    private final ArrayList<Pair<DoubleAccessor, RenderElements>> pairsList;

    public RenderElementsRangeHighlighterAndLineAccessMap() {
        this(10);
    }

    public RenderElementsRangeHighlighterAndLineAccessMap(int initialCapacity) {
        pairsList = new ArrayList<>(initialCapacity);
    }

    @Override
    public @Nullable RenderElements get(RangeHighlighter rangeHighlighter) {
        for (final var pair : pairsList) {
            DoubleAccessor accessor = pair.left;
            RenderElements elements = pair.right;
            if (rangeHighlighter == accessor.rangeHighlighter) {
                return elements;
            }
        }
        return null;
    }

    @Override
    public @NotNull RenderElements[] get(int lineNumber) {
        ArrayList<RenderElements> finalList = new ArrayList<>(5);
        for (final var pair : pairsList) {
            DoubleAccessor accessor = pair.left;
            RenderElements elements = pair.right;
            if (lineNumber == accessor.lineNumber) {
                finalList.add(elements);
            }
        }
        return finalList.toArray(RenderElements[]::new);
    }

    @Override
    public @Nullable RenderElements remove(RangeHighlighter rangeHighlighter) {
        for (final var pair : pairsList) {
            DoubleAccessor accessor = pair.left;
            RenderElements elements = pair.right;
            if (rangeHighlighter == accessor.rangeHighlighter) {
                pairsList.remove(pair);
                return elements;
            }
        }
        return null;
    }

    @Override
    public void put(RangeHighlighter rangeHighlighter, RenderElements elem) {
        DoubleAccessor accessor = new DoubleAccessor(rangeHighlighter,
                rangeHighlighter.getDocument().getLineNumber(rangeHighlighter.getStartOffset()));
        pairsList.add(new Pair<>(accessor, elem));
    }

    private static class DoubleAccessor {

        public final RangeHighlighter rangeHighlighter;
        public final int lineNumber;

        public DoubleAccessor(RangeHighlighter rangeHighlighter, int lineNumber) {
            this.rangeHighlighter = rangeHighlighter;
            this.lineNumber = lineNumber;
        }
    }
}

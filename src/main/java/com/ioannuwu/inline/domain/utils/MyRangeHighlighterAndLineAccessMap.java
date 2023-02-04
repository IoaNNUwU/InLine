package com.ioannuwu.inline.domain.utils;

import com.intellij.openapi.editor.markup.RangeHighlighter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MyRangeHighlighterAndLineAccessMap<T> {

    @Nullable T get(RangeHighlighter rangeHighlighter);

    @NotNull T[] get(int lineNumber);

    @Nullable T remove(RangeHighlighter rangeHighlighter);

    void put(RangeHighlighter rangeHighlighter, T elem);
}
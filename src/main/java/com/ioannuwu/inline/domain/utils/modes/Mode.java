package com.ioannuwu.inline.domain.utils.modes;

import com.intellij.openapi.editor.markup.RangeHighlighter;

public interface Mode {

    void afterAdded(RangeHighlighter highlighter);

    void beforeRemoved(RangeHighlighter highlighter);

    default void attributesChanged(RangeHighlighter highlighter) {
        beforeRemoved(highlighter);
        afterAdded(highlighter);
    }
}

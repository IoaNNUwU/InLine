package com.ioannuwu.inline.domain.render;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.markup.RangeHighlighter;

public class RangeHighlighterAdapter implements Disposable {

    private final RangeHighlighter rangeHighlighter;

    public RangeHighlighterAdapter(RangeHighlighter rangeHighlighter) {
        this.rangeHighlighter = rangeHighlighter;
    }

    @Override
    public void dispose() {
        rangeHighlighter.dispose();
    }
}

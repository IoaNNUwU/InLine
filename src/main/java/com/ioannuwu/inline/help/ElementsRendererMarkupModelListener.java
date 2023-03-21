package com.ioannuwu.inline.help;

import com.intellij.openapi.editor.ex.RangeHighlighterEx;
import com.intellij.openapi.editor.impl.event.MarkupModelListener;
import com.ioannuwu.inline.domain.utils.modes.Mode;
import org.jetbrains.annotations.NotNull;

public class ElementsRendererMarkupModelListener implements MarkupModelListener {

    private final Mode mode;

    public ElementsRendererMarkupModelListener(Mode mode) {
        this.mode = mode;
    }

    @Override
    public void afterAdded(@NotNull RangeHighlighterEx highlighter) {
        mode.afterAdded(highlighter);
    }

    @Override
    public void attributesChanged(@NotNull RangeHighlighterEx highlighter, boolean renderersChanged, boolean fontStyleOrColorChanged) {
        mode.attributesChanged(highlighter);
    }

    @Override
    public void beforeRemoved(@NotNull RangeHighlighterEx highlighter) {
        mode.beforeRemoved(highlighter);
    }
}

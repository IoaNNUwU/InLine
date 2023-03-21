package com.ioannuwu.inline.help;

import com.intellij.openapi.editor.ex.RangeHighlighterEx;
import com.intellij.openapi.editor.impl.event.MarkupModelListener;
import org.jetbrains.annotations.NotNull;

// previous - Mode
public class HelpMarkupModelListener implements MarkupModelListener {

    private final ViewModel viewModel;

    public HelpMarkupModelListener(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void afterAdded(@NotNull RangeHighlighterEx highlighter) {

    }

    @Override
    public void attributesChanged(@NotNull RangeHighlighterEx highlighter, boolean renderersChanged, boolean fontStyleOrColorChanged) {

    }

    @Override
    public void beforeRemoved(@NotNull RangeHighlighterEx highlighter) {

    }
}

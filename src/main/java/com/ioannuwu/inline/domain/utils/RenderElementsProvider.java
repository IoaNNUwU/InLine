package com.ioannuwu.inline.domain.utils;

import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.ioannuwu.inline.ui.render.elements.RenderElement;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface RenderElementsProvider {

    @NotNull Collection<RenderElement> provide(@NotNull RangeHighlighter rangeHighlighter);
}

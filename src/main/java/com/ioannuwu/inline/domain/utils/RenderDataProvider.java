package com.ioannuwu.inline.domain.utils;

import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.ioannuwu.inline.domain.render.RenderData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface RenderDataProvider {

    @Nullable RenderData provide(@NotNull RangeHighlighter highlighter);
}

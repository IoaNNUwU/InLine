package com.ioannuwu.inline.domain.render;

import com.ioannuwu.inline.domain.RangeHighlighterWrapper;
import org.jetbrains.annotations.NotNull;

public interface RenderDataProvider {

    @NotNull RenderData provide(@NotNull RangeHighlighterWrapper rangeHighlighter);
}

package com.ioannuwu.inline.ui.render;

import com.ioannuwu.inline.domain.render.RenderData;
import com.ioannuwu.inline.domain.render.RenderElements;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface EditorElementsRenderer {

    @NotNull RenderElements render(@NotNull RenderData renderData, int startOffset);

    void unRender(@Nullable RenderElements elements);
}


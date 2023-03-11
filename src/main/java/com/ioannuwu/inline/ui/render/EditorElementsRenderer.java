package com.ioannuwu.inline.ui.render;

import com.intellij.openapi.Disposable;
import com.ioannuwu.inline.domain.render.RenderData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

public interface EditorElementsRenderer {

    @NotNull Set<Disposable> render(@NotNull RenderData renderData, int startOffset);

    void unRender(@Nullable Collection<Disposable> highlighters);
}


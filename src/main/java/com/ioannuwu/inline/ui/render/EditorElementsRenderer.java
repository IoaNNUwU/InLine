package com.ioannuwu.inline.ui.render;

import com.intellij.openapi.Disposable;
import com.ioannuwu.inline.ui.render.elements.RenderElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

public interface EditorElementsRenderer {
    /**
     * @param renderElements collection of render elements for specific error
     * @return Set of Disposables associated with that elements which should
     * be removed when error is removed
     */
    @NotNull Set<Disposable> render(@NotNull Collection<RenderElement> renderElements, int startOffset);

    void unRender(@Nullable Collection<Disposable> highlighters);
}


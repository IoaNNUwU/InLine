package com.ioannuwu.inline.ui.render;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.util.Disposer;
import com.ioannuwu.inline.ui.render.elements.RenderElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class EditorElementsRendererImpl implements EditorElementsRenderer {

    @Override
    public @NotNull Set<Disposable> render(@NotNull Collection<RenderElement> renderElements, int startOffset) {
        Set<Disposable> set = new HashSet<>();
        for (var elem : renderElements) {
            set.add(elem.render());
        }
        return set;
    }

    @Override
    public void unRender(@Nullable Collection<Disposable> elements) {
        if (elements == null) return;
        if (elements.isEmpty()) return;

        for (var elem : elements) {
            Disposer.dispose(elem);
        }
    }
}

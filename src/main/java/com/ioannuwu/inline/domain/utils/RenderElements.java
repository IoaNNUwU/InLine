package com.ioannuwu.inline.domain.utils;


import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.ioannuwu.inline.ui.render.MyElementRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class RenderElements {
    public final @Nullable Inlay<MyElementRenderer> inlay;
    public final @Nullable RangeHighlighter lineHighlighter;

    public RenderElements(@Nullable Inlay<MyElementRenderer> inlay, @Nullable RangeHighlighter lineHighlighter) {
        this.inlay = inlay;
        this.lineHighlighter = lineHighlighter;
    }

    public static final @NotNull RenderElements EMPTY = new RenderElements(null, null);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenderElements that = (RenderElements) o;
        return Objects.equals(inlay, that.inlay) && Objects.equals(lineHighlighter, that.lineHighlighter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inlay, lineHighlighter);
    }
}

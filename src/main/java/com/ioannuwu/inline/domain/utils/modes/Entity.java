package com.ioannuwu.inline.domain.utils.modes;

import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.ioannuwu.inline.domain.render.RenderElements;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Entity {
    public final @NotNull RangeHighlighter rangeHighlighter;
    public final int initialLine;

    public @Nullable RenderElements renderElements;

    public Entity(@NotNull RangeHighlighter rangeHighlighter, int initialLine, @Nullable RenderElements renderElements) {
        this.rangeHighlighter = rangeHighlighter;
        this.initialLine = initialLine;
        this.renderElements = renderElements;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "initialLine=" + initialLine +
                ", renderElements=" + renderElements +
                '}';
    }
}

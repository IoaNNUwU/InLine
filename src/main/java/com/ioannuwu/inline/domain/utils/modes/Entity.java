package com.ioannuwu.inline.domain.utils.modes;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Set;

public class Entity {
    public final @NotNull RangeHighlighter rangeHighlighter;
    public final int initialLine;

    public @Nullable Set<Disposable> renderElements;

    public Entity(@NotNull RangeHighlighter rangeHighlighter, int initialLine, @Nullable Set<Disposable> renderElements) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return initialLine == entity.initialLine && rangeHighlighter.equals(entity.rangeHighlighter) && Objects.equals(renderElements, entity.renderElements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rangeHighlighter, initialLine, renderElements);
    }
}

package com.ioannuwu.inline.ui.render.elements;

import com.intellij.openapi.editor.markup.GutterIconRenderer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Objects;

public class MyGutterRenderer extends GutterIconRenderer {

    private final Icon icon;

    public MyGutterRenderer(@NotNull Icon icon) {
        this.icon = icon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyGutterRenderer that = (MyGutterRenderer) o;
        return icon.equals(that.icon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(icon);
    }

    @Override
    public @NotNull Icon getIcon() {
        return icon;
    }
}

package com.ioannuwu.inline.ui.render.elements.graphiccomponents;

import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.markup.TextAttributes;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Represents part of EditorCustomElementRenderer
 */
public interface GraphicsComponent {

    GraphicsComponent EMPTY = new GraphicsComponent() { };

    default void draw(@NotNull Inlay inlay, @NotNull Graphics g, @NotNull Rectangle targetRegion,
                      @NotNull TextAttributes textAttributes) { }
}

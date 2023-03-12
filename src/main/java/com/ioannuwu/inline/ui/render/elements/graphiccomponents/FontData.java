package com.ioannuwu.inline.ui.render.elements.graphiccomponents;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class FontData {
    public final @NotNull Font font;
    public final float editorFontSize;
    public final @NotNull FontMetrics fontMetrics;
    public final int lineHeight;

    public FontData(@NotNull Font font, float editorFontSize, @NotNull FontMetrics fontMetrics, int lineHeight) {
        this.font = font;
        this.editorFontSize = editorFontSize;
        this.fontMetrics = fontMetrics;
        this.lineHeight = lineHeight;
    }
}

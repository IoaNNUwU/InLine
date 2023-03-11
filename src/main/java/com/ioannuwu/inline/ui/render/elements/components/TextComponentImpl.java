package com.ioannuwu.inline.ui.render.elements.components;

import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.markup.TextAttributes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class TextComponentImpl implements TextComponent {

    private final Font deriveFont;
    private final FontMetrics fontMetrics;

    private final Color color;
    private final String text;

    private int width = -1;
    private final int lineHeight;

    /**
     * @param font if null uses editor's font
     * @param color if null will not be rendered
     * @param text if null will not be rendered
     * <p>
     * will not be rendered means width() = 0 and render returns empty Disposable
     */
    public TextComponentImpl(@NotNull Font font, float editorFontSize, @NotNull FontMetrics fontMetrics, int lineHeight,
                             @Nullable Color color, @Nullable String text) {
        this.deriveFont = font.deriveFont(editorFontSize + 1);
        this.fontMetrics = fontMetrics;
        this.color = color;
        this.text = text;
        this.lineHeight = lineHeight;
    }
    public TextComponentImpl(@NotNull FontData fontData,
                             @Nullable Color color, @Nullable String text) {
        this.deriveFont = fontData.font.deriveFont(fontData.editorFontSize + 1);
        this.fontMetrics = fontData.fontMetrics;
        this.color = color;
        this.text = text;
        this.lineHeight = fontData.lineHeight;
    }

    @Override
    public void draw(@NotNull Inlay inlay, @NotNull Graphics g, @NotNull Rectangle targetRegion,
                     @NotNull TextAttributes textAttributes) {
        if (width == 0) return;
        if (width == -1) width();

        g.setFont(deriveFont);
        g.setColor(color);

        int magicNumber = 2;

        g.drawString(text, targetRegion.x + fontMetrics.charWidth('a') * 2 / 3,
                targetRegion.y + lineHeight * 3 / 4 - magicNumber);
    }

    @Override
    public int width() {
        if (width == 0) return 0;
        width = (text == null || color == null) ? 0 : fontMetrics.stringWidth(text) + fontMetrics.charWidth('a') * 3 / 2;

        return width;
    }
}



















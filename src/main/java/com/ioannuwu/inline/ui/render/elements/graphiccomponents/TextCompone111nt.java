package com.ioannuwu.inline.ui.render.elements.graphiccomponents;

import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.markup.TextAttributes;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public interface TextCompone111nt extends GraphicsComponent {

    class Base implements TextCompone111nt {

        protected final FontData fontData;

        protected final Color color;
        protected final String text;

        public Base(@NotNull FontData fontData, @NotNull Color color, @NotNull String text) {
            this.fontData = fontData;
            this.color = color;
            this.text = text;
        }

        @Override
        public void draw(@NotNull Inlay inlay, @NotNull Graphics g, @NotNull Rectangle targetRegion,
                         @NotNull TextAttributes textAttributes) {

            g.setFont(fontData.font());
            g.setColor(color);

            double centerX = width() / 2f + targetRegion.x;

            var fontMetrics = fontData.fontMetrics();

            g.drawString(text,
                    (int) (centerX - fontMetrics.stringWidth(text) / 2) + fontMetrics.charWidth('a'),
                    targetRegion.y + fontData.lineHeight() * 3 / 4);
        }

        @Override
        public int width() {
            var fontMetrics = fontData.fontMetrics();
            return fontMetrics.stringWidth(text) + fontMetrics.charWidth('a') * 2;
        }
    }

    class WithDotAtTheEnd extends Base {

        public WithDotAtTheEnd(@NotNull FontData fontData, @NotNull Color color, @NotNull String text) {
            super(fontData, color, text + ".");
        }
    }

    class RustStyleTextComponent implements TextCompone111nt {

        protected final FontData fontData;
        protected final FontProvider editorFont;

        protected final Color color;
        protected final String text;

        protected final int indentationLevel;

        public RustStyleTextComponent(@NotNull FontData fontData, FontProvider editorFont, @NotNull Color color,
                                      @NotNull String text, int indentationLevel) {
            this.fontData = fontData;
            this.editorFont = editorFont;
            this.color = color;
            this.text = text;

            this.indentationLevel = indentationLevel + 1;

        }

        @Override
        public void draw(@NotNull Inlay inlay, @NotNull Graphics g, @NotNull Rectangle targetRegion, @NotNull TextAttributes textAttributes) {

            g.setFont(fontData.font());
            g.setColor(color);

            double lineHeight = fontData.lineHeight();
            double yOffset = lineHeight * 0.75;

            double lineHeightMagicNumber = 0.25;

            if (indentationLevel == 1) {
                g.drawString(text,
                        targetRegion.x + fontData.fontMetrics().charWidth('a'),
                        (int) (targetRegion.y + yOffset));

                g.setFont(editorFont.font());

                g.drawString("^",
                        targetRegion.x,
                        (int) (targetRegion.y + yOffset - lineHeight * lineHeightMagicNumber));
            } else {

                g.drawString(text,
                        targetRegion.x,
                        (int) (targetRegion.y + yOffset));

                g.setFont(editorFont.font());

                g.drawString("^",
                        targetRegion.x,
                        (int) (targetRegion.y + yOffset - lineHeight * (indentationLevel - 1) - lineHeight * lineHeightMagicNumber));


                for (int i = 1; i < indentationLevel; i++) {
                    g.drawString("|",
                            targetRegion.x,
                            (int) (targetRegion.y + yOffset - lineHeight * i + lineHeight * 0.1));
                }
            }
        }

        @Override
        public int width() {
            var fontMetrics = fontData.fontMetrics();
            return fontMetrics.stringWidth(text) + fontMetrics.charWidth('a') * 2;
        }
    }
}
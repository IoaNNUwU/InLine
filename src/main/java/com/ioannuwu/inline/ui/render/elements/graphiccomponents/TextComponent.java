package com.ioannuwu.inline.ui.render.elements.graphiccomponents;

import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.markup.TextAttributes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public interface TextComponent extends GraphicsComponent, PrettyWidth {

    TextComponent EMPTY = new TextComponent() {
    };


    class Base implements TextComponent {

        protected final Font deriveFont;
        protected final FontMetrics fontMetrics;

        protected final Color color;
        protected final String text;

        protected int width = -1;
        protected final int lineHeight;

        /**
         * @param font  if null uses editor's font
         * @param color if null will not be rendered
         * @param text  if null will not be rendered
         *              <p>
         *              will not be rendered means width() = 0 and render returns empty Disposable
         */
        public Base(@NotNull Font font, float editorFontSize, @NotNull FontMetrics fontMetrics, int lineHeight,
                    @Nullable Color color, @Nullable String text) {
            this.deriveFont = font.deriveFont(editorFontSize + 1);
            this.fontMetrics = fontMetrics;
            this.color = color;
            this.text = " " + text + " ";
            this.lineHeight = lineHeight;
        }

        public Base(@NotNull FontData fontData, @Nullable Color color, @Nullable String text) {
            this(fontData.font, fontData.editorFontSize, fontData.fontMetrics, fontData.lineHeight, color, text);
        }

        @Override
        public void draw(@NotNull Inlay inlay, @NotNull Graphics g, @NotNull Rectangle targetRegion,
                         @NotNull TextAttributes textAttributes) {
            if (width == 0) return;
            if (width == -1) width();

            g.setFont(deriveFont);
            g.setColor(color);

            double centerX = width / 2f + targetRegion.x;

            g.drawString(text,
                    (int) centerX - fontMetrics.stringWidth(text) / 2 - fontMetrics.stringWidth("  "),
                    targetRegion.y + lineHeight * 3 / 4);
        }

        @Override
        public int width() {
            if (width == 0) return 0;

            width = (text == null || color == null) ? 0
                    : (fontMetrics.stringWidth(text + " ") * 110 / 100
                    + lineHeight / 2);

            return width;
        }
    }

    class WithDotAtTheEnd extends Base {

        /**
         * @param font  if null uses editor's font
         * @param color if null will not be rendered
         * @param text  if null will not be rendered
         *              <p>
         *              will not be rendered means width() = 0 and render returns empty Disposable
         */
        public WithDotAtTheEnd(@NotNull Font font, float editorFontSize, @NotNull FontMetrics fontMetrics, int lineHeight,
                               @Nullable Color color, @Nullable String text) {
            super(font, editorFontSize, fontMetrics, lineHeight, color, text + ".");
        }

        public WithDotAtTheEnd(@NotNull FontData fontData, @Nullable Color color, @Nullable String text) {
            this(fontData.font, fontData.editorFontSize, fontData.fontMetrics, fontData.lineHeight, color, text);
        }

        @Override
        public int width() {
            if (width == 0) return 0;

            width = (text == null || color == null) ? 0
                    : (fontMetrics.stringWidth(text) * 110 / 100
                    + lineHeight / 2);

            return width;
        }
    }
}
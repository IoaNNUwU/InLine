package com.ioannuwu.inline.ui.render.elements.graphiccomponents;

import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.markup.TextAttributes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public interface TextComponent extends GraphicsComponent {

    class Base implements TextComponent {

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
}
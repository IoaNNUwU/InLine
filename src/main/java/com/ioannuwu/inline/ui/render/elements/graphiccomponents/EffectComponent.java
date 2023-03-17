package com.ioannuwu.inline.ui.render.elements.graphiccomponents;

import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.markup.TextAttributes;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public interface EffectComponent extends GraphicsComponent {

    EffectComponent EMPTY = new EffectComponent() { };

    class Box implements EffectComponent {

        private final FontData fontData;

        private final Color color;
        private final int arc;

        public Box(Color color, FontData fontData) {
            this.color = color;
            this.arc = fontData.lineHeight() * 4 / 10;
            this.fontData = fontData;
        }

        @Override
        public void draw(@NotNull Inlay inlay, @NotNull Graphics g, @NotNull Rectangle targetRegion, @NotNull TextAttributes textAttributes) {
            int magicNumberToFixBoxBlinking = 1;

            g.setColor(color);
            g.drawRoundRect(
                    targetRegion.x + fontData.fontMetrics().charWidth('a'),
                    targetRegion.y + magicNumberToFixBoxBlinking,
                    targetRegion.width,
                    targetRegion.height - 2 * magicNumberToFixBoxBlinking,
                    arc, arc);
        }
    }

    class Shadow implements EffectComponent {

        protected final FontData fontData;

        protected final Color color;
        protected final String text;

        protected final PrettyWidth widthProvider;

        public Shadow(@NotNull FontData fontData, @NotNull Color color, @NotNull String text, PrettyWidth widthProvider) {
            this.fontData = fontData;
            this.color = color;
            this.text = text;
            this.widthProvider = widthProvider;
        }

        @Override
        public void draw(@NotNull Inlay inlay, @NotNull Graphics g, @NotNull Rectangle targetRegion,
                         @NotNull TextAttributes textAttributes) {

            g.setFont(fontData.font());
            g.setColor(color);

            double centerX = widthProvider.width() / 2f + targetRegion.x;

            var fontMetrics = fontData.fontMetrics();
            var charWidth = fontMetrics.charWidth('a');

            g.drawString(text,
                    (int) (centerX - fontMetrics.stringWidth(text) / 2 + charWidth + charWidth * 2 / 10),
                    targetRegion.y + fontData.lineHeight() * 3 / 4 + charWidth * 1 / 10);
        }
    }
}

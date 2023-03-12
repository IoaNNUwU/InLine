package com.ioannuwu.inline.ui.render.elements.graphiccomponents;

import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.markup.TextAttributes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public interface EffectComponent extends GraphicsComponent {

    EffectComponent EMPTY = new EffectComponent() { };

    class Box implements EffectComponent {

        private final Color color;
        private final int arc;

        public Box(Color color, FontMetrics fontMetrics) {
            this.color = color;
            this.arc = fontMetrics.getHeight() * 4 / 10;
        }

        @Override
        public void draw(@NotNull Inlay inlay, @NotNull Graphics g, @NotNull Rectangle targetRegion, @NotNull TextAttributes textAttributes) {
            int magicNumberToFixBoxBlinking = 1;

            g.setColor(color);
            g.drawRoundRect(
                    targetRegion.x + targetRegion.width / 30, // TODO
                    targetRegion.y + magicNumberToFixBoxBlinking,
                    targetRegion.width - 2 * magicNumberToFixBoxBlinking,
                    targetRegion.height - 2 * magicNumberToFixBoxBlinking,
                    arc, arc);
        }
    }

    class Shadow implements EffectComponent {

        private final Font deriveFont;
        private final FontMetrics fontMetrics;

        private final Color color;
        private final String text;

        private final int lineHeight;

        public Shadow(@NotNull Font font, float editorFontSize, @NotNull FontMetrics fontMetrics, int lineHeight,
                                 @Nullable Color color, @Nullable String text) {
            this.deriveFont = font.deriveFont(editorFontSize + 1);
            this.fontMetrics = fontMetrics;
            this.color = color;
            this.text = text;
            this.lineHeight = lineHeight;
        }

        public Shadow(@NotNull FontData fontData,
                       @Nullable Color color, @Nullable String text) {
            this.deriveFont = fontData.font.deriveFont(fontData.editorFontSize + 1);
            this.fontMetrics = fontData.fontMetrics;
            this.color = color;
            this.text = text;
            this.lineHeight = fontData.lineHeight;
        }

        @Override
        public void draw(@NotNull Inlay inlay, @NotNull Graphics g, @NotNull Rectangle targetRegion, @NotNull TextAttributes textAttributes) {
            String tempText = text + '.';

            int borderMagicNumberToFixBoxBlinking = 2;

            g.setFont(deriveFont);

            int charWidth = fontMetrics.charWidth('a');
            // Right shadow
            g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 200).darker());
            g.drawString(tempText, targetRegion.x + charWidth * 2 / 3 + charWidth * 2 / 10,
                    targetRegion.y + charWidth * 2 / 10 + lineHeight * 3 / 4 - borderMagicNumberToFixBoxBlinking);
            // Left shadow
            g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 130));
            g.drawString(text, targetRegion.x + charWidth * 2 / 3 - charWidth * 1 / 10,
                    targetRegion.y - charWidth * 1 / 10 + lineHeight * 3 / 4 - borderMagicNumberToFixBoxBlinking);
        }
    }
}

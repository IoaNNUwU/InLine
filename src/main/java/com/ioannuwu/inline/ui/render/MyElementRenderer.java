package com.ioannuwu.inline.ui.render;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorCustomElementRenderer;
import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.colors.EditorFontType;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.util.ui.UIUtilities;
import com.ioannuwu.inline.domain.render.RenderData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class MyElementRenderer implements EditorCustomElementRenderer {

    private final RenderData renderData;

    private final int numberOfWhitespaces;

    private final EditorFontType fontType = EditorFontType.PLAIN;

    public MyElementRenderer(RenderData renderData, int numberOfWhitespaces) {
        this.renderData = renderData;
        this.numberOfWhitespaces = numberOfWhitespaces;
    }

    @Override
    public @Nullable GutterIconRenderer calcGutterIconRenderer(@NotNull Inlay inlay) {
        return new MyGutterRenderer(renderData.gutterIcon);
    }

    @Override
    public int calcWidthInPixels(@NotNull Inlay inlay) {
        if (renderData.text == null) return 1;
        Editor editor = inlay.getEditor();
        FontMetrics fontMetrics = UIUtilities.getFontMetrics(editor.getComponent(), editor.getColorsScheme().getFont(fontType));
        return fontMetrics.stringWidth(renderData.text) + fontMetrics.charWidth(' ') * 3 / 2;
    }

    @Override
    public void paint(@NotNull Inlay inlay, @NotNull Graphics g, @NotNull Rectangle targetRegion, @NotNull TextAttributes textAttributes) {
        if (renderData.text == null) return;
        int borderMagicNumberToFixBoxBlinking = 2;
        targetRegion.y = targetRegion.y + borderMagicNumberToFixBoxBlinking;
        targetRegion.x = targetRegion.x + borderMagicNumberToFixBoxBlinking;
        targetRegion.width = targetRegion.width - 2 * borderMagicNumberToFixBoxBlinking;
        targetRegion.height = targetRegion.height - 2 * borderMagicNumberToFixBoxBlinking;

        Editor editor = inlay.getEditor();
        Font font = editor.getColorsScheme().getFont(fontType);
        FontMetrics fontMetrics = UIUtilities.getFontMetrics(editor.getComponent(), font);

        int arc = fontMetrics.getHeight() * 4 / 10;
        int charWidth = fontMetrics.charWidth('a');

        targetRegion.x = targetRegion.x + this.numberOfWhitespaces * charWidth;

        g.setColor(renderData.effectColor);
        g.drawRoundRect(targetRegion.x, targetRegion.y, targetRegion.width, targetRegion.height, arc, arc);

        g.setFont(font);
        g.setColor(renderData.textColor);
        g.drawString(renderData.text, targetRegion.x + charWidth * 2 / 3, targetRegion.y + editor.getLineHeight() * 3 / 4 - borderMagicNumberToFixBoxBlinking);
    }
}

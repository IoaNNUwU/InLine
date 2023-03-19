package com.ioannuwu.inline.ui.render;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorCustomElementRenderer;
import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.ioannuwu.inline.ui.render.elements.graphiccomponents.FontMetricsProvider;
import com.ioannuwu.inline.ui.render.elements.graphiccomponents.FontProvider;
import com.ioannuwu.inline.ui.render.elements.graphiccomponents.GraphicsComponent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Collection;

public class RustStyleElementRenderer implements EditorCustomElementRenderer {

    private final Collection<GraphicsComponent> graphicComponents;
    private final Document document;

    private final int startOffset;
    private final FontMetricsProvider editorFontMetricsProvider;

    public RustStyleElementRenderer(Collection<GraphicsComponent> graphicComponents, Document document,
                                    int startOffset, FontMetricsProvider editorFontMetricsProvider) {
        this.graphicComponents = graphicComponents;
        this.document = document;
        this.startOffset = startOffset;
        this.editorFontMetricsProvider = editorFontMetricsProvider;
    }

    @Override
    public int calcWidthInPixels(@NotNull Inlay inlay) {
        int sum = 0;
        for (var comp : graphicComponents)
            sum += comp.width();

        return (sum == 0) ? 1 : sum;
    }

    @Override
    public void paint(@NotNull Inlay inlay, @NotNull Graphics g, @NotNull Rectangle targetRegion, @NotNull TextAttributes textAttributes) {
        if (document.getTextLength() < startOffset) return;
        int errorLineNumber = document.getLineNumber(startOffset);
        int lineStartOffset = document.getLineStartOffset(errorLineNumber);

        int offsetFromLineStart = startOffset - lineStartOffset;

        Rectangle newTargetRegion = new Rectangle(targetRegion.x + (offsetFromLineStart) * editorFontMetricsProvider.fontMetrics().charWidth('a'),
                targetRegion.y, targetRegion.width, targetRegion.height);

        for (var comp : graphicComponents) {
            if (comp.width() == 0) comp.draw(inlay, g, newTargetRegion, textAttributes);
        }
        for (var comp : graphicComponents) {
            if (comp.width() != 0) comp.draw(inlay, g, newTargetRegion, textAttributes);
        }
    }
}

package com.ioannuwu.inline.ui.render;

import com.intellij.openapi.editor.EditorCustomElementRenderer;
import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.ioannuwu.inline.ui.render.elements.graphiccomponents.GraphicsComponent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Collection;

public class MyElementRenderer implements EditorCustomElementRenderer {

    private final Collection<GraphicsComponent> graphicComponents;

    public MyElementRenderer(Collection<GraphicsComponent> graphicComponents) {
        this.graphicComponents = graphicComponents;
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
        for (var comp : graphicComponents) {
            if (comp.width() == 0) comp.draw(inlay, g, targetRegion, textAttributes);
        }
        for (var comp : graphicComponents) {
            if (comp.width() != 0) comp.draw(inlay, g, targetRegion, textAttributes);
        }
    }
}

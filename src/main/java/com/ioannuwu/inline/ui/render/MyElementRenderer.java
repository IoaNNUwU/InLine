package com.ioannuwu.inline.ui.render;

import com.intellij.openapi.editor.EditorCustomElementRenderer;
import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.ioannuwu.inline.ui.render.elements.graphiccomponents.GraphicsComponent;
import com.ioannuwu.inline.ui.render.elements.graphiccomponents.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Collection;

public class MyElementRenderer implements EditorCustomElementRenderer {

    private final TextComponent textComponent;
    private final Collection<GraphicsComponent> graphicComponents;

    public MyElementRenderer(TextComponent textComponent, Collection<GraphicsComponent> graphicComponents) {
        this.textComponent = textComponent;
        this.graphicComponents = graphicComponents;
    }

    @Override
    public int calcWidthInPixels(@NotNull Inlay inlay) {
        return textComponent.width();
    }

    @Override
    public void paint(@NotNull Inlay inlay, @NotNull Graphics g, @NotNull Rectangle targetRegion, @NotNull TextAttributes textAttributes) {
        for (var comp : graphicComponents) {
            comp.draw(inlay, g, targetRegion, textAttributes);
        }
        // text component should be rendered last
        textComponent.draw(inlay, g, targetRegion, textAttributes);
    }
}

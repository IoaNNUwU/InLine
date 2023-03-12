package com.ioannuwu.inline.ui.render.elements;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.InlayModel;
import com.ioannuwu.inline.ui.render.MyElementRenderer;
import com.ioannuwu.inline.ui.render.elements.graphiccomponents.GraphicsComponent;
import com.ioannuwu.inline.ui.render.elements.graphiccomponents.TextComponent;

import java.util.Collection;

/**
 * Represents text - main part of hints. <p>
 * Also represents Effect on a text because it is too close and
 * every effect depends on a text. <p>
 * Contains Collection of GraphicsComponent
 */
public class MainTextRenderElement implements RenderElement {

    private final Collection<GraphicsComponent> graphicsComponents;
    private final TextComponent textComponent;

    private final InlayModel inlayModel;
    private final int offset;

    public MainTextRenderElement(Collection<GraphicsComponent> graphicsComponents, TextComponent textComponent, InlayModel inlayModel, int offset) {
        this.graphicsComponents = graphicsComponents;
        this.textComponent = textComponent;
        this.inlayModel = inlayModel;
        this.offset = offset;
    }

    @Override
    public Disposable render() {
        return inlayModel.addAfterLineEndElement(offset, false,
                new MyElementRenderer(textComponent, graphicsComponents));
    }
}

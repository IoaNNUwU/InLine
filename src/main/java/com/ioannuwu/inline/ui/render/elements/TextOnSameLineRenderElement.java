package com.ioannuwu.inline.ui.render.elements;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.InlayModel;

import java.util.Collection;

/**
 * Represents text - main part of hints. <p>
 * Also represents Effect on a text because it is too close and
 * every effect depends on a text. <p>
 * Contains Collection of GraphicsComponent
 */
public class TextOnSameLineRenderElement implements RenderElement {

    private final Collection<GraphicsComponent> graphicsComponents;

    private final InlayModel inlayModel;
    private final int offset;

    public TextOnSameLineRenderElement(Collection<GraphicsComponent> graphicsComponents, InlayModel inlayModel, int offset) {
        this.graphicsComponents = graphicsComponents;
        this.inlayModel = inlayModel;
        this.offset = offset;
    }

    @Override
    public Disposable render() {
        return inlayModel.addAfterLineEndElement(offset, false,
                new MyElementRenderer(graphicsComponents));
    }
}

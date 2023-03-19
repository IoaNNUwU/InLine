package com.ioannuwu.inline.ui.render.elements;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.InlayModel;
import com.ioannuwu.inline.ui.render.RustStyleElementRenderer;
import com.ioannuwu.inline.ui.render.elements.graphiccomponents.FontMetricsProvider;
import com.ioannuwu.inline.ui.render.elements.graphiccomponents.FontProvider;
import com.ioannuwu.inline.ui.render.elements.graphiccomponents.GraphicsComponent;
import com.ioannuwu.inline.utils.Utils;

import java.util.Collection;

public class RustStyleTextRenderElement implements RenderElement {

    private final Collection<GraphicsComponent> graphicsComponents;

    private final InlayModel inlayModel;
    private final int offset;

    private final Document document;

    private final FontMetricsProvider editorFontMetrics;
    private final FontProvider editorFont;

    public RustStyleTextRenderElement(Collection<GraphicsComponent> graphicsComponents, InlayModel inlayModel,
                                      int offset, Document document, FontMetricsProvider EditorFontMetrics, FontProvider editorFont) {
        this.graphicsComponents = graphicsComponents;
        this.inlayModel = inlayModel;
        this.offset = offset;
        this.document = document;
        this.editorFontMetrics = EditorFontMetrics;
        this.editorFont = editorFont;
    }

    @Override
    public Disposable render() {

        int errorLine = document.getLineNumber(offset);
        int errorLineEndOffset = document.getLineEndOffset(errorLine);

        if (errorLineEndOffset > document.getTextLength()) return Utils.EMPTY_DISPOSABLE;

        return inlayModel.addBlockElement(errorLineEndOffset, false, false,
                1, new RustStyleElementRenderer(
                        graphicsComponents, document, offset, editorFontMetrics));
    }
}

package com.ioannuwu.inline.ui.render.elements;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorCustomElementRenderer;
import com.intellij.openapi.editor.InlayModel;
import com.ioannuwu.inline.utils.Utils;

import java.util.Collection;

public class RustStyleTextRenderElement implements RenderElement {

    private final Collection<GraphicsComponent> graphicsComponents;

    private final InlayModel inlayModel;
    private final int offset;

    private final Document document;

    private final FontMetricsProvider editorFontMetrics;

    public RustStyleTextRenderElement(Collection<GraphicsComponent> graphicsComponents, InlayModel inlayModel,
                                      int offset, Document document, FontMetricsProvider EditorFontMetrics) {
        this.graphicsComponents = graphicsComponents;
        this.inlayModel = inlayModel;
        this.offset = offset;
        this.document = document;
        this.editorFontMetrics = EditorFontMetrics;
    }

    @Override
    public Disposable render() {

        int errorLine = document.getLineNumber(offset);
        int errorLineEndOffset = document.getLineEndOffset(errorLine);

        if (errorLineEndOffset > document.getTextLength()) return Utils.EMPTY_DISPOSABLE;

        EditorCustomElementRenderer editorCustomElementRenderer = new RustStyleElementRenderer(
                graphicsComponents, document, offset, editorFontMetrics);
        // TODO among us
        editorCustomElementRenderer = new MyElementRenderer(graphicsComponents);

        return inlayModel.addBlockElement(errorLineEndOffset, true, false,
                1, editorCustomElementRenderer);
    }
}

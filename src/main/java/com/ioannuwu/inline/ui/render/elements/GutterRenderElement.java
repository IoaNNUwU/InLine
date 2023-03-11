package com.ioannuwu.inline.ui.render.elements;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.ioannuwu.inline.domain.render.RangeHighlighterAdapter;
import com.ioannuwu.inline.domain.utils.MyTextAttributes;
import com.ioannuwu.inline.ui.render.MyGutterRenderer;

import javax.swing.*;

public class GutterRenderElement implements RenderElement {

    private final MarkupModel markupModel;
    private final Icon icon;
    private final int lineNumber;

    public GutterRenderElement(MarkupModel markupModel, Icon icon, int lineNumber) {
        this.markupModel = markupModel;
        this.icon = icon;
        this.lineNumber = lineNumber;
    }

    @Override
    public Disposable render() {
        var lineHighlighter = markupModel.addLineHighlighter(lineNumber, 0,
                new MyTextAttributes(null));

        lineHighlighter.setGutterIconRenderer(new MyGutterRenderer(icon));

        return new RangeHighlighterAdapter(lineHighlighter);
    }
}

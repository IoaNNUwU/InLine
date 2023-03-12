package com.ioannuwu.inline.ui.render.elements;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.ioannuwu.inline.domain.render.RangeHighlighterAdapter;
import com.ioannuwu.inline.domain.utils.MyTextAttributes;

import java.awt.*;

public class BackgroundRenderElement implements RenderElement {

    private final Color color;
    private final MarkupModel markupModel;
    private final int lineNumber;

    public BackgroundRenderElement(Color color, MarkupModel markupModel, int lineNumber) {
        this.color = color;
        this.markupModel = markupModel;
        this.lineNumber = lineNumber;
    }

    @Override
    public Disposable render() {
        var lineHighlighter = markupModel.addLineHighlighter(lineNumber,
                0, new MyTextAttributes(color));

        return new RangeHighlighterAdapter(lineHighlighter);
    }
}

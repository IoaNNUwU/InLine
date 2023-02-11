package com.ioannuwu.inline.ui.render;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.util.Disposer;
import com.ioannuwu.inline.domain.render.RenderData;
import com.ioannuwu.inline.domain.utils.MyTextAttributes;
import com.ioannuwu.inline.domain.render.RenderElements;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EditorElementsRendererImpl implements EditorElementsRenderer {

    private final Editor editor;

    public EditorElementsRendererImpl(Editor editor) {
        this.editor = editor;
    }

    @Override
    public @NotNull RenderElements render(@NotNull RenderData renderData, int startOffset) {

        int lineNumber = editor.getDocument().getLineNumber(startOffset);

        RangeHighlighter lineHighlighterWithGutterIcon = null;
        Inlay<MyElementRenderer> inlay = null;

        if (renderData.showBackground || renderData.showGutterIcon) {
            lineHighlighterWithGutterIcon = editor.getMarkupModel()
                    .addLineHighlighter(lineNumber, 0,
                            new MyTextAttributes(renderData.showBackground ? renderData.backgroundColor : null));

            if (renderData.showGutterIcon)
                lineHighlighterWithGutterIcon.setGutterIconRenderer(
                        new MyGutterRenderer(renderData.icon != null ? renderData.icon : AllIcons.General.Gear));
        }
        if (renderData.showText || renderData.showEffect) {
            inlay = editor.getInlayModel().addAfterLineEndElement(
                    startOffset, false,
                    new MyElementRenderer(renderData));
        }
        return new RenderElements(inlay, lineHighlighterWithGutterIcon);
    }

    @Override
    public void unRender(@Nullable RenderElements elements) {
        if (elements == null) return;
        if (elements.inlay != null) Disposer.dispose(elements.inlay);
        if (elements.lineHighlighter != null) elements.lineHighlighter.dispose();
    }
}

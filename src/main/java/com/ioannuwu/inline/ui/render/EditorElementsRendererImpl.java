package com.ioannuwu.inline.ui.render;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.Disposer;
import com.ioannuwu.inline.domain.render.RangeHighlighterAdapter;
import com.ioannuwu.inline.domain.render.RenderData;
import com.ioannuwu.inline.domain.utils.FontProvider;
import com.ioannuwu.inline.domain.utils.MyTextAttributes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class EditorElementsRendererImpl implements EditorElementsRenderer {

    private final Editor editor;
    private final FontProvider fontProvider;

    public EditorElementsRendererImpl(Editor editor, FontProvider fontProvider) {
        this.editor = editor;
        this.fontProvider = fontProvider;
    }

    @Override
    public @NotNull Set<Disposable> render(@NotNull RenderData renderData, int startOffset) {

        int lineNumber = editor.getDocument().getLineNumber(startOffset);

        int lineEndOffset = editor.getDocument().getLineEndOffset(lineNumber);
        int lineStartOffset = editor.getDocument().getLineStartOffset(lineNumber);

        Set<Disposable> renderElements = new HashSet<>();

        if (renderData.showBackground || renderData.showGutterIcon) {
            var lineHighlighter = editor.getMarkupModel().addLineHighlighter(lineNumber, 0,
                    new MyTextAttributes(renderData.showBackground ? renderData.backgroundColor : null));

            renderElements.add(new RangeHighlighterAdapter(lineHighlighter));

            if (renderData.showGutterIcon) // TODO adds icon to existing highlighter
                lineHighlighter.setGutterIconRenderer(
                        new MyGutterRenderer(renderData.icon != null ? renderData.icon : AllIcons.General.Gear));
        }
        if (renderData.showText || renderData.showEffect) {
            var inlay = editor.getInlayModel().addAfterLineEndElement(
                    startOffset, false,
                    new MyElementRenderer(renderData, fontProvider));
            // TODO change inlay style to multiline

            renderElements.add(inlay);
        }
        return renderElements;
    }

    @Override
    public void unRender(@Nullable Collection<Disposable> elements) {
        if (elements == null) return;
        if (elements.isEmpty()) return;

        for (var elem : elements) {
            Disposer.dispose(elem);
        }
    }
}

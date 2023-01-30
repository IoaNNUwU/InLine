package com.ioannuwu.inline.domain;

import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.ex.RangeHighlighterEx;
import com.intellij.openapi.editor.impl.event.MarkupModelListener;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.fileEditor.TextEditor;
import com.ioannuwu.inline.domain.render.RenderData;
import com.ioannuwu.inline.domain.render.RenderDataProvider;
import com.ioannuwu.inline.ui.render.MyElementRenderer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class MyMarkupModelListener implements MarkupModelListener {

    private final TextEditor editor;
    private final RenderDataProvider renderDataProvider;

    private final HashMap<RangeHighlighter, MyRenderElements> map = new HashMap<>(20);

    public MyMarkupModelListener(TextEditor editor, RenderDataProvider renderDataProvider) {
        this.editor = editor;
        this.renderDataProvider = renderDataProvider;
    }

    @Override
    public void afterAdded(@NotNull RangeHighlighterEx highlighter) {
        RangeHighlighterWrapper wrapper = RangeHighlighterWrapper.tryFrom(highlighter);
        if (wrapper == null) {
            return;
        }
        RenderData data = renderDataProvider.provide(wrapper);
        Inlay<MyElementRenderer> inlay = editor.getEditor().getInlayModel().addAfterLineEndElement(
                highlighter.getStartOffset(), false,
                new MyElementRenderer(data, renderDataProvider.getBorder(), renderDataProvider.getNumberOfWhitespaces()));

        RangeHighlighter lineHighlighter = editor.getEditor().getMarkupModel().addLineHighlighter(highlighter.getDocument()
                .getLineNumber(highlighter.getStartOffset()), 0, new MyTextAttributes(data.backgroundColor));

        map.put(highlighter, new MyRenderElements(inlay, lineHighlighter));
    }

    @Override
    public void beforeRemoved(@NotNull RangeHighlighterEx highlighter) {
        if (!map.containsKey(highlighter)) return;

        MyRenderElements elements = map.get(highlighter);

        elements.inlay.dispose();
        elements.lineHighlighter.dispose();

        map.remove(highlighter);
    }

    @Override
    public void attributesChanged(@NotNull RangeHighlighterEx highlighter, boolean renderersChanged, boolean fontStyleOrColorChanged) {
        if (!map.containsKey(highlighter)) return;
        MyRenderElements elements = map.get(highlighter);
        elements.inlay.update();
    }

    private static class MyRenderElements {
        private final Inlay<MyElementRenderer> inlay;
        private final RangeHighlighter lineHighlighter;

        public MyRenderElements(Inlay<MyElementRenderer> inlay, RangeHighlighter lineHighlighter) {
            this.inlay = inlay;
            this.lineHighlighter = lineHighlighter;
        }
    }
}

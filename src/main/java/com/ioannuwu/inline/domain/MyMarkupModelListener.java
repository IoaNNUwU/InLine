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

    private final HashMap<RangeHighlighter, Inlay<MyElementRenderer>> map = new HashMap<>(20);
    private final HashMap<RangeHighlighter, RangeHighlighter> lineHighlightersMap = new HashMap<>(20);

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

        map.put(highlighter, inlay);
        RangeHighlighter lineHighlighter = editor.getEditor().getMarkupModel().addLineHighlighter(highlighter.getDocument()
                .getLineNumber(highlighter.getStartOffset()), 0, new MyTextAttributes(data.backgroundColor));
        lineHighlightersMap.put(highlighter, lineHighlighter);
    }

    @Override
    public void beforeRemoved(@NotNull RangeHighlighterEx highlighter) {
        if (!map.containsKey(highlighter)) return;
        Inlay<MyElementRenderer> inlay = map.get(highlighter);
        inlay.dispose();
        map.remove(highlighter);
        RangeHighlighter fromMap = lineHighlightersMap.get(highlighter);
        fromMap.dispose();
        lineHighlightersMap.remove(highlighter);
    }

    @Override
    public void attributesChanged(@NotNull RangeHighlighterEx highlighter, boolean renderersChanged, boolean fontStyleOrColorChanged) {
        if (!map.containsKey(highlighter)) return;
        Inlay<MyElementRenderer> inlay = map.get(highlighter);
        inlay.update();
    }
}

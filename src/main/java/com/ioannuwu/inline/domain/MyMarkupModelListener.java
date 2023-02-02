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
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Objects;

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
        RangeHighlighterWrapper wrapper;
        try {
            wrapper = new RangeHighlighterWrapper.WithDescription(highlighter);
        } catch (RangeHighlighterWrapperException ignored) { return; }

        RenderData renderData = renderDataProvider.provide(wrapper);

        Inlay<MyElementRenderer> textAndEffectInlay = null;
        RangeHighlighter lineHighlighter = null;

        if (!renderData.showBackground && !renderData.showText && !renderData.showEffect) {
            map.put(highlighter, MyRenderElements.EMPTY);
            return;
        }
        if (renderData.showBackground) {
            lineHighlighter = editor.getEditor().getMarkupModel().addLineHighlighter(highlighter.getDocument()
                    .getLineNumber(highlighter.getStartOffset()), 0, new MyTextAttributes(renderData.backgroundColor));
        }
        if (renderData.showText || renderData.showEffect) {
            textAndEffectInlay = editor.getEditor().getInlayModel().addAfterLineEndElement(
                    highlighter.getStartOffset(), false,
                    new MyElementRenderer(renderData));
        }
        map.put(highlighter, new MyRenderElements(textAndEffectInlay, lineHighlighter));
    }

    @Override
    public void attributesChanged(@NotNull RangeHighlighterEx highlighter, boolean renderersChanged, boolean fontStyleOrColorChanged) {
        beforeRemoved(highlighter);
        afterAdded(highlighter);
    }

    @Override
    public void beforeRemoved(@NotNull RangeHighlighterEx highlighter) {
        if (!map.containsKey(highlighter)) return;
        MyRenderElements elements = map.get(highlighter);
        if (elements == MyRenderElements.EMPTY) {
            map.remove(highlighter);
            return;
        }

        Inlay<MyElementRenderer> inlay = elements.inlay;
        if (inlay != null) inlay.dispose();

        RangeHighlighter lineHighlighter = elements.lineHighlighter;
        if (lineHighlighter != null) lineHighlighter.dispose();

        map.remove(highlighter);
    }

    private static class MyRenderElements {
        public final @Nullable Inlay<MyElementRenderer> inlay;
        public final @Nullable RangeHighlighter lineHighlighter;

        public MyRenderElements(@Nullable Inlay<MyElementRenderer> inlay, @Nullable RangeHighlighter lineHighlighter) {
            this.inlay = inlay;
            this.lineHighlighter = lineHighlighter;
        }

        private static final @NotNull MyRenderElements EMPTY = new MyRenderElements(null, null);

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MyRenderElements that = (MyRenderElements) o;
            return Objects.equals(inlay, that.inlay) && Objects.equals(lineHighlighter, that.lineHighlighter);
        }

        @Override
        public int hashCode() {
            return Objects.hash(inlay, lineHighlighter);
        }
    }
}

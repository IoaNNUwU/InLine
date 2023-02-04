package com.ioannuwu.inline.domain;

import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.ex.RangeHighlighterEx;
import com.intellij.openapi.editor.impl.event.MarkupModelListener;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.fileEditor.TextEditor;
import com.ioannuwu.inline.domain.render.RenderData;
import com.ioannuwu.inline.domain.render.RenderDataProvider;
import com.ioannuwu.inline.domain.utils.RenderElements;
import com.ioannuwu.inline.domain.utils.MyTextAttributes;
import com.ioannuwu.inline.domain.utils.RangeHighlighterWrapperException;
import com.ioannuwu.inline.domain.utils.RenderElementsRangeHighlighterAndLineAccessMap;
import com.ioannuwu.inline.ui.render.MyElementRenderer;
import com.ioannuwu.inline.ui.render.MyGutterRenderer;
import org.jetbrains.annotations.NotNull;

public class ElementsRendererMarkupModelListener implements MarkupModelListener {

    private final TextEditor editor;
    private final RenderDataProvider renderDataProvider;

    private final RenderElementsRangeHighlighterAndLineAccessMap map = new RenderElementsRangeHighlighterAndLineAccessMap();

    public ElementsRendererMarkupModelListener(TextEditor editor, RenderDataProvider renderDataProvider) {
        this.editor = editor;
        this.renderDataProvider = renderDataProvider;
    }

    @Override
    public void afterAdded(@NotNull RangeHighlighterEx highlighter) {
        RangeHighlighterWrapper wrapper;
        try {
            wrapper = new RangeHighlighterWrapper.WithDescription(highlighter);
        } catch (RangeHighlighterWrapperException ignored) {
            return;
        }

        RenderData renderData = renderDataProvider.provide(wrapper);

        Inlay<MyElementRenderer> textAndEffectInlay = null;
        RangeHighlighter lineHighlighter = null;

        if (!renderData.showBackground && !renderData.showText && !renderData.showEffect && !renderData.showGutterIcon) {
            map.put(highlighter, RenderElements.EMPTY);
            return;
        }
        if (renderData.showBackground || renderData.showGutterIcon) {
            lineHighlighter = editor.getEditor().getMarkupModel().addLineHighlighter(highlighter.getDocument()
                    .getLineNumber(highlighter.getStartOffset()), 0,
                            new MyTextAttributes(renderData.showBackground ? renderData.backgroundColor : null));
            if (renderData.showGutterIcon)
                lineHighlighter.setGutterIconRenderer(new MyGutterRenderer(renderData.icon));
        }
        if (renderData.showText || renderData.showEffect) {
            textAndEffectInlay = editor.getEditor().getInlayModel().addAfterLineEndElement(
                    highlighter.getStartOffset(), false,
                    new MyElementRenderer(renderData));
        }
        map.put(highlighter, new RenderElements(textAndEffectInlay, lineHighlighter));
    }

    @Override
    public void attributesChanged(@NotNull RangeHighlighterEx highlighter, boolean renderersChanged, boolean fontStyleOrColorChanged) {
        beforeRemoved(highlighter);
        afterAdded(highlighter);
    }

    @Override
    public void beforeRemoved(@NotNull RangeHighlighterEx highlighter) {
        RenderElements elements = map.get(highlighter);
        if (elements == null) return;
        if (elements == RenderElements.EMPTY) {
            map.remove(highlighter);
            return;
        }

        Inlay<MyElementRenderer> inlay = elements.inlay;
        if (inlay != null) inlay.dispose();

        RangeHighlighter lineHighlighter = elements.lineHighlighter;
        if (lineHighlighter != null) lineHighlighter.dispose();

        map.remove(highlighter);
    }
}

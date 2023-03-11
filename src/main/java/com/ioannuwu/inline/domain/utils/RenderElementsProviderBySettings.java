package com.ioannuwu.inline.domain.utils;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.EditorFontType;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.util.ui.UIUtilities;
import com.ioannuwu.inline.ui.render.elements.BackgroundRenderElement;
import com.ioannuwu.inline.ui.render.elements.GutterRenderElement;
import com.ioannuwu.inline.ui.render.elements.MainTextRenderElement;
import com.ioannuwu.inline.ui.render.elements.RenderElement;
import com.ioannuwu.inline.ui.render.elements.components.EffectComponent;
import com.ioannuwu.inline.ui.render.elements.components.FontData;
import com.ioannuwu.inline.ui.render.elements.components.GraphicsComponent;
import com.ioannuwu.inline.ui.render.elements.components.TextComponentImpl;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;

public class RenderElementsProviderBySettings implements RenderElementsProvider {

    private final RenderDataProvider renderDataProvider;
    private final FontProvider fontProvider;

    private final Editor editor;

    public RenderElementsProviderBySettings(RenderDataProvider renderDataProvider,
                                            FontProvider fontProvider, Editor editor) {
        this.renderDataProvider = renderDataProvider;
        this.fontProvider = fontProvider;
        this.editor = editor;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull Collection<RenderElement> provide(@NotNull RangeHighlighter rangeHighlighter) {

        var renderData = renderDataProvider.provide(rangeHighlighter);
        if (renderData == null) return Collections.EMPTY_LIST;

        Collection<RenderElement> list = new ArrayList<>();
        int lineNumber = editor.getDocument().getLineNumber(rangeHighlighter.getStartOffset());

        if (renderData.showBackground) list.add(new BackgroundRenderElement(
                renderData.backgroundColor, editor.getMarkupModel(), lineNumber));
        if (renderData.showGutterIcon) list.add(new GutterRenderElement(
                editor.getMarkupModel(), renderData.icon, lineNumber));

        if (renderData.showText || renderData.showEffect) {

            Font font = fontProvider.provide();
            float fontSize = (float) editor.getColorsScheme().getEditorFontSize() + 1;
            Font deriveFont = (font == null) ? editor.getColorsScheme().getFont(EditorFontType.PLAIN) :
                    font.deriveFont((float) editor.getColorsScheme().getEditorFontSize() + 1);
            FontMetrics fontMetrics = UIUtilities.getFontMetrics(editor.getComponent(), deriveFont);

            FontData fontData = new FontData(deriveFont, fontSize, fontMetrics, editor.getLineHeight());

            Set<GraphicsComponent> set = new HashSet<>();
            if (renderData.showEffect) {
                switch (renderData.effectType) {
                    case NONE:                                                                                                  break;
                    case BOX: set.add(new EffectComponent.Box(renderData.effectColor, fontMetrics));                            break;
                    case SHADOW: set.add(new EffectComponent.Shadow(fontData, renderData.effectColor, renderData.description)); break;
                }
            }

            RenderElement mainRenderElement = new MainTextRenderElement(
                    set, new TextComponentImpl(
                            fontData, renderData.textColor, renderData.description
            ), editor.getInlayModel(), rangeHighlighter.getStartOffset());

            list.add(mainRenderElement);
        }

        return list;
    }
}

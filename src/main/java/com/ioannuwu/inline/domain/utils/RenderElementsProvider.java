package com.ioannuwu.inline.domain.utils;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.ioannuwu.inline.data.EffectType;
import com.ioannuwu.inline.data.MySettingsService;
import com.ioannuwu.inline.ui.render.elements.BackgroundRenderElement;
import com.ioannuwu.inline.ui.render.elements.GutterRenderElement;
import com.ioannuwu.inline.ui.render.elements.MainTextRenderElement;
import com.ioannuwu.inline.ui.render.elements.RenderElement;
import com.ioannuwu.inline.ui.render.elements.graphiccomponents.*;
import com.ioannuwu.inline.ui.render.elements.graphiccomponents.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;

public interface RenderElementsProvider {

    @NotNull Collection<RenderElement> provide(@NotNull RangeHighlighter rangeHighlighter);


    class BySettings implements RenderElementsProvider {

        private final RenderDataProvider renderDataProvider;
        private final Editor editor;

        private final MySettingsService settingsService;
        private final GraphicsEnvironment graphicsEnvironment;

        public BySettings(RenderDataProvider renderDataProvider, Editor editor, MySettingsService settingsService, GraphicsEnvironment graphicsEnvironment) {
            this.renderDataProvider = renderDataProvider;
            this.editor = editor;
            this.settingsService = settingsService;
            this.graphicsEnvironment = graphicsEnvironment;
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

                FontData fontData = new FontData.BySettings(settingsService, editor, graphicsEnvironment);

                TextComponent textComponent = (renderData.showEffect && renderData.effectType == EffectType.BOX) ?
                        new TextComponent.Base(fontData, renderData.textColor, renderData.description) :
                        new TextComponent.WithDotAtTheEnd(fontData, renderData.textColor, renderData.description);

                Set<GraphicsComponent> set = new HashSet<>();
                if (renderData.showEffect) {
                    switch (renderData.effectType) {
                        case NONE:
                            break;
                        case BOX:
                            set.add(new EffectComponent.Box(renderData.effectColor, fontData));
                            break;
                        case SHADOW:
                            set.add(new EffectComponent.Shadow(fontData, renderData.effectColor, renderData.description + ".", textComponent));
                            break;
                    }
                }
                set.add(textComponent);
                RenderElement mainRenderElement = new MainTextRenderElement(
                        set, editor.getInlayModel(), rangeHighlighter.getStartOffset());

                list.add(mainRenderElement);
            }
            return list;
        }
    }
}

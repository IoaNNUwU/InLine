package com.ioannuwu.inline

import com.intellij.openapi.editor.Editor
import com.ioannuwu.inline.data.EffectType
import com.ioannuwu.inline.data.MySettingsService
import com.ioannuwu.inline.elements.RenderElementKt
import com.ioannuwu.inline.graphics.EffectComponentKt
import com.ioannuwu.inline.graphics.GraphicsComponentKt
import com.ioannuwu.inline.graphics.TextComponent
import com.ioannuwu.inline.ui.render.elements.graphiccomponents.FontData
import com.ioannuwu.inline.wrapper.RangeHighlighterWrapper
import java.awt.GraphicsEnvironment
import kotlin.collections.ArrayList

interface RenderElementsProviderKt {

    fun provide(
        rangeHighlighter: RangeHighlighterWrapper,
        renderAttributes: RenderAttributes
    ): List<RenderElementKt>


    class BySettings(
        private val renderDataProvider: RenderDataProviderKt,
        private val settingsService: MySettingsService,
        private val graphicsEnvironment: GraphicsEnvironment,
        private val editor: Editor,
    ) : RenderElementsProviderKt {

        override fun provide(
            rangeHighlighter: RangeHighlighterWrapper,
            renderAttributes: RenderAttributes
        ): List<RenderElementKt> { // TODO ZDEC

            val renderData = renderDataProvider.provide(rangeHighlighter) ?: return emptyList()

            val offset = rangeHighlighter.offset

            val renderElements = ArrayList<RenderElementKt>()

            if (renderData.showBackground)
                renderElements.add(RenderElementKt.Background(renderData.backgroundColor, offset))

            if (renderData.showGutterIcon)
                renderElements.add(RenderElementKt.Gutter(renderData.icon!!, offset))

            if (renderData.showText || renderData.showEffect) {
                val fontData = FontData.BySettings(settingsService, editor, graphicsEnvironment)

                val textComponent = TextComponent.AfterLineText(fontData, renderData.textColor, renderData.description)

                val graphicsComponents: MutableList<GraphicsComponentKt> = mutableListOf()

                if (renderData.showEffect) when (renderData.effectType) {
                    EffectType.NONE -> {}
                    EffectType.BOX -> graphicsComponents.add(EffectComponentKt.Box(fontData, renderData.effectColor))
                    EffectType.SHADOW -> graphicsComponents.add(EffectComponentKt.Shadow(renderData.effectColor, textComponent))
                }
                graphicsComponents.add(textComponent)

                renderElements.add(RenderElementKt.Text(graphicsComponents, offset))
            }

            return renderElements
        }
    }
}
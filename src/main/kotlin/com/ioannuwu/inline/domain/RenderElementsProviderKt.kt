package com.ioannuwu.inline.domain

import com.ioannuwu.inline.data.EffectType
import com.ioannuwu.inline.data.FontDataProvider
import com.ioannuwu.inline.domain.elements.RenderElementKt
import com.ioannuwu.inline.domain.graphics.EffectComponentKt
import com.ioannuwu.inline.domain.graphics.GraphicsComponentKt
import com.ioannuwu.inline.domain.graphics.TextComponent
import com.ioannuwu.inline.domain.wrapper.RangeHighlighterWrapper

interface RenderElementsProviderKt {

    fun provide(
        rangeHighlighter: RangeHighlighterWrapper,
        renderAttributes: RenderAttributes
    ): List<RenderElementKt>


    class Impl(
        private val renderDataProvider: RenderDataProviderKt,
        private val fontDataProvider: FontDataProvider,
    ) : RenderElementsProviderKt {

        override fun provide(
            rangeHighlighter: RangeHighlighterWrapper,
            renderAttributes: RenderAttributes
        ): List<RenderElementKt> {

            val renderData = renderDataProvider.provide(rangeHighlighter) ?: return emptyList()

            val offset = rangeHighlighter.offset

            val renderElements = ArrayList<RenderElementKt>()

            if (renderData.showBackground)
                renderElements.add(RenderElementKt.Background(renderData.backgroundColor, offset))

            if (renderData.showGutterIcon)
                renderElements.add(RenderElementKt.Gutter(renderData.icon!!, offset))

            if (renderData.showText || renderData.showEffect) {

                val textComponent =
                    TextComponent.AfterLineText(fontDataProvider, renderData.textColor, renderData.description)

                val graphicsComponents: MutableList<GraphicsComponentKt> = mutableListOf()

                if (renderData.showEffect) when (renderData.effectType) {
                    EffectType.NONE -> {}

                    EffectType.BOX -> graphicsComponents.add(
                        EffectComponentKt.Box(
                            fontDataProvider,
                            renderData.effectColor
                        )
                    )

                    EffectType.SHADOW -> graphicsComponents.add(
                        EffectComponentKt.Shadow(
                            renderData.effectColor,
                            textComponent
                        )
                    )
                }
                graphicsComponents.add(textComponent)

                renderElements.add(RenderElementKt.Text(graphicsComponents, offset))
            }

            return renderElements
        }
    }
}
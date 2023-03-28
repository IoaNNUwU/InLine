package com.ioannuwu.inline.domain

import com.ioannuwu.inline.data.EffectType
import com.ioannuwu.inline.data.FontDataProvider
import com.ioannuwu.inline.domain.elements.RenderElementKt
import com.ioannuwu.inline.domain.graphics.EffectComponentKt
import com.ioannuwu.inline.domain.graphics.GraphicsComponentKt
import com.ioannuwu.inline.domain.graphics.TextComponent
import com.ioannuwu.inline.domain.wrapper.RangeHighlighterWrapper
import com.ioannuwu.inline.domain.wrapper.WrapperComparator
import java.util.*

interface RenderElementsProvider {

    fun provide(
        lineStartOffset: Int,
        highlightersToBeShownSortedByPriority: List<RangeHighlighterWrapper>,
    ): List<Collection<RenderElementKt>>


    class Impl(
        private val renderDataProvider: RenderDataProviderKt,
        private val fontDataProvider: FontDataProvider,
        private val editorFontDataProvider: FontDataProvider,
    ) : RenderElementsProvider {

        override fun provide(
            lineStartOffset: Int,
            highlightersToBeShownSortedByPriority: List<RangeHighlighterWrapper>,
        ): List<Collection<RenderElementKt>> {

            val indices = highlightersToBeShownSortedByPriority.indices

            val renderDataForEachHighlighterByPriority = highlightersToBeShownSortedByPriority.asSequence()
                .sortedWith(WrapperComparator.ByOffsetLowestIsLast)
                .map(renderDataProvider::provide)
                .toList()

            val renderElements = mutableListOf<MutableList<RenderElementKt>>()
            for (i in indices) renderElements.add(mutableListOf())

            for (i in indices) { // adds only 1 most important background
                val renderData = renderDataForEachHighlighterByPriority[i]
                if (renderData.showBackground) {
                    val renderElement = RenderElementKt.Background(
                        renderData.backgroundColor,
                        highlightersToBeShownSortedByPriority[i].offset
                    )
                    renderElements[i].add(renderElement)
                    break
                }
            }

            for (i in indices) { // adds all gutter renderers
                val renderData = renderDataForEachHighlighterByPriority[i]
                if (renderData.showGutterIcon) {
                    // possible to save elements by adding gutter to existing line element
                    val renderElement = RenderElementKt.Gutter(
                        renderData.icon!!,
                        highlightersToBeShownSortedByPriority[i].offset
                    )
                    renderElements[i].add(renderElement)
                    if (renderData.oneGutterMode) break
                }
            }

            for (i in indices) { // text and effects
                val renderData = renderDataForEachHighlighterByPriority[i]
                if (renderData.showText || renderData.showEffect) {
                    val textComponent = if (renderData.showText)
                        TextComponent.Impl(fontDataProvider, renderData.textColor, renderData.description)
                    else TextComponent.None(fontDataProvider, renderData.description)

                    val graphicsComponents = mutableListOf<GraphicsComponentKt>(textComponent)

                    if (renderData.showEffect)
                        when (renderData.effectType) {
                            EffectType.NONE -> Unit
                            EffectType.BOX -> graphicsComponents.add(
                                EffectComponentKt.Box(
                                    fontDataProvider, renderData.effectColor
                                )
                            )

                            EffectType.SHADOW -> graphicsComponents.add(
                                EffectComponentKt.Shadow(
                                    renderData.effectColor, textComponent
                                )
                            )
                        }

                    val highlighter = highlightersToBeShownSortedByPriority[i]

                    val textElement = when (renderData.textStyle) {
                        TextStyle.RUST -> RenderElementKt.RustStyleText(
                            graphicsComponents, highlighter.offset, lineStartOffset, editorFontDataProvider
                        )

                        TextStyle.DEFAULT -> RenderElementKt.DefaultText(graphicsComponents, highlighter.offset)
                    }

                    renderElements[i].add(textElement)
                }
            }

            return renderElements
        }
    }
}
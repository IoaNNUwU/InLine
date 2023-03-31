package com.ioannuwu.inline.domain

import com.ioannuwu.inline.data.EffectType
import com.ioannuwu.inline.data.FontData
import com.ioannuwu.inline.domain.elements.RenderElementKt
import com.ioannuwu.inline.domain.graphics.EffectComponentKt
import com.ioannuwu.inline.domain.graphics.GraphicsComponentKt
import com.ioannuwu.inline.domain.graphics.TextComponent
import com.ioannuwu.inline.domain.wrapper.RangeHighlighterWrapper

interface RenderElementsProvider {

    fun provide(
        lineStartOffset: Int,
        highlightersToBeShownSortedByPriority: List<RangeHighlighterWrapper>,
    ): Map<RangeHighlighterWrapper, Collection<RenderElementKt>>


    class Impl(
        private val renderDataProvider: RenderDataProvider,
        private val fontData: FontData,
        private val editorFontData: FontData,
        private val numberOfWhitespaces: NumberOfWhitespaces,
    ) : RenderElementsProvider {

        override fun provide(
            lineStartOffset: Int,
            highlightersToBeShownSortedByPriority: List<RangeHighlighterWrapper>,
        ): Map<RangeHighlighterWrapper, Collection<RenderElementKt>> {

            val indices = highlightersToBeShownSortedByPriority.indices

            val renderDataForEachHighlighterByPriority = highlightersToBeShownSortedByPriority.asSequence()
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

            val effects = mutableListOf<MutableList<GraphicsComponentKt>>()
            for (i in indices) { // init effects list
                effects.add(mutableListOf())
            }

            for (i in indices) { // effects
                val renderData = renderDataForEachHighlighterByPriority[i]

                if (!renderData.showEffect) continue

                val textComponent = if (renderData.showText)
                    TextComponent.Impl(fontData, renderData.textColor, renderData.description)
                else TextComponent.None(fontData, renderData.description)

                val effect = when (renderData.effectType) {
                    EffectType.NONE -> EffectComponentKt.EMPTY
                    EffectType.BOX -> EffectComponentKt.Box(fontData, renderData.effectColor)
                    EffectType.SHADOW -> EffectComponentKt.Shadow(renderData.effectColor, textComponent)
                }

                effects[i].add(effect)
                effects[i].add(textComponent)
            }

            val indentationLevelMap = highlightersToBeShownSortedByPriority.asSequence()
                .sortedBy { it.offset }
                .mapIndexed { index, wrapper -> Pair(wrapper, Pair(index, wrapper.offset)) }
                .toMap()

            for (i in indices) { // text
                val renderData = renderDataForEachHighlighterByPriority[i]

                val currentHighlighter = highlightersToBeShownSortedByPriority[i]

                val indentationLevel = indentationLevelMap[currentHighlighter]!!.first

                val size = indentationLevelMap.size
                val offset = indentationLevelMap[currentHighlighter]!!.second

                val doMoveRight: Boolean = indentationLevelMap.toList().reversed().asSequence()
                    .drop(size - indentationLevel)
                    .any { it.second.second == offset }


                if (renderData.showText) {
                    val highlighter = highlightersToBeShownSortedByPriority[i]

                    val textElement = when (renderData.textStyle) {

                        TextStyle.UNDER_LINE -> RenderElementKt.RustStyleText(
                            effects[i],
                            highlighter.offset,
                            indentationLevel - indentationLevelMap.size,
                            lineStartOffset,
                            editorFontData,
                            numberOfWhitespaces,
                            renderData.textColor,
                            doMoveRight
                        )

                        TextStyle.AFTER_LINE -> RenderElementKt.DefaultText(
                            effects[i],
                            highlighter.offset,
                            numberOfWhitespaces,
                            editorFontData
                        )
                    }
                    renderElements[i].add(textElement)
                }
            }

            val map = HashMap<RangeHighlighterWrapper, List<RenderElementKt>>()
            for (i in indices) {
                val wrapper = highlightersToBeShownSortedByPriority[i]
                val elements = renderElements[i]

                map[wrapper] = elements
            }
            return map
        }
    }
}
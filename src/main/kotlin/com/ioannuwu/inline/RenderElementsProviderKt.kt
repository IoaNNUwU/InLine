package com.ioannuwu.inline

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.ioannuwu.inline.data.EffectType
import com.ioannuwu.inline.data.MySettingsService
import com.ioannuwu.inline.domain.utils.RenderDataProvider
import com.ioannuwu.inline.domain.utils.RenderElementsProvider
import com.ioannuwu.inline.ui.render.elements.graphiccomponents.FontData
import com.ioannuwu.inline.ui.render.elements.graphiccomponents.TextComponent
import java.awt.GraphicsEnvironment
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

interface RenderElementsProviderKt {

    fun provide(rangeHighlighter: RangeHighlighter, renderAttributes: RenderAttributes): Collection<RenderElementKt>


    class BySettings(
        private val renderDataProvider: RenderDataProvider,
        private val settingsService: MySettingsService,
        private val graphicsEnvironment: GraphicsEnvironment,
        private val editor: Editor,
    ) : RenderElementsProviderKt {

        override fun provide(
            rangeHighlighter: RangeHighlighter,
            renderAttributes: RenderAttributes
        ): Collection<RenderElementKt> {

            val renderData = renderDataProvider.provide(rangeHighlighter) ?: return emptySet()
            if (rangeHighlighter.document.textLength <= rangeHighlighter.startOffset) return emptySet()

            val lineNumber = editor.document.getLineNumber(rangeHighlighter.startOffset)
            val list = ArrayList<RenderElementKt>()

            if (renderData.showBackground) list.add(RenderElementKt.Background(renderData.backgroundColor, lineNumber))
            if (renderData.showGutterIcon) list.add(RenderElementKt.Gutter(renderData.icon!!, lineNumber))

            if (renderData.showText || renderData.showEffect) {
                val fontData = FontData.BySettings(settingsService, editor, graphicsEnvironment)

                val textComponent = TextComponent.Base(fontData, renderData.textColor, renderData.description)

                val set = HashSet<EffectComponentKt>()

                if (renderData.showEffect) {
                    when (renderData.effectType) {
                        EffectType.NONE -> {}
                        EffectType.BOX -> set.add(EffectComponentKt.EMPTY)
                        EffectType.SHADOW -> set.add(EffectComponentKt.EMPTY)
                    }
                }
            }

        return emptySet()
        }

    }
}
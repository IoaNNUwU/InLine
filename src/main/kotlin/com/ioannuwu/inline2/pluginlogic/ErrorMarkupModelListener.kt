package com.ioannuwu.inline2.pluginlogic

import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.ex.RangeHighlighterEx
import com.intellij.openapi.editor.impl.event.MarkupModelListener
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.openapi.util.Disposer
import com.ioannuwu.inline.data.EffectType
import com.ioannuwu.inline.data.TextStyle
import com.ioannuwu.inline2.pluginlogic.element.DefaultTextRenderer
import com.ioannuwu.inline2.pluginlogic.element.UnderLineTextRenderer
import com.ioannuwu.inline2.pluginlogic.render.ElementMetrics
import com.ioannuwu.inline2.pluginlogic.render.OtherDataSelector
import com.ioannuwu.inline2.pluginlogic.render.RenderDataElementMetrics
import com.ioannuwu.inline2.pluginlogic.render.RenderDataSelector
import com.ioannuwu.inline2.pluginlogic.render.metrics.FontMetrics
import com.ioannuwu.inline2.pluginlogic.render.metrics.LineMetrics
import com.ioannuwu.inline2.pluginlogic.render.metrics.OtherData
import com.ioannuwu.inline2.pluginlogic.render.metrics.RenderData
import com.ioannuwu.inline2.pluginlogic.utils.runOnEDT
import com.jetbrains.rd.util.ConcurrentHashMap
import kotlinx.html.dom.document

class ErrorMarkupModelListener(
    private val model: EditorModel,
    private val renderDataSelector: RenderDataSelector,
    private val otherDataSelector: OtherDataSelector,
    private val editor: Editor
) : MarkupModelListener {

    private val map: MutableMap<RangeHighlighterEx, MutableList<Disposable>> = ConcurrentHashMap()

    override fun afterAdded(highlighter: RangeHighlighterEx) {

        val info = (highlighter.errorStripeTooltip as? HighlightInfo) ?: return

        val description = info.description ?: return
        if (description.isEmpty()) return
        if (map.containsKey(highlighter)) return

        val renderData: RenderData = renderDataSelector(highlighter) ?: return
        val otherData: OtherData = otherDataSelector.selectOtherData(highlighter)

        val metrics: ElementMetrics = RenderDataElementMetrics(renderData, editor)

        runOnEDT {

            val disposables: MutableList<Disposable> = map[highlighter] ?: mutableListOf()

            val textStyleRenderer = when (otherData.textStyle()) {
                TextStyle.AFTER_LINE -> DefaultTextRenderer(metrics, otherData) { otherData.effectType() }
                TextStyle.UNDER_LINE ->
                    UnderLineTextRenderer(
                        metrics, highlighter, editor,
                        otherData::effectType
                    )
            }

            if (renderData.textColor() != null) {

                when (otherData.textStyle()) {
                    TextStyle.AFTER_LINE -> disposables.add(
                        model.addAfterLineEndElement(
                            highlighter.startOffset, textStyleRenderer
                        )
                    )

                    TextStyle.UNDER_LINE -> disposables.add(
                        model.addUnderLineElement(
                            highlighter.startOffset, textStyleRenderer
                        )
                    )
                }
            }

            if (renderData.backgroundColor() != null)
                disposables.add(model.addLineHighlighter(highlighter.startOffset, renderData.backgroundColor()!!))

            if (renderData.gutterIcon() != null && !otherData.showOnlyOneGutter())
                disposables.add(model.addGutterIcon(highlighter.startOffset, renderData.gutterIcon()!!))

            map[highlighter] = disposables
        }
    }

    override fun beforeRemoved(highlighter: RangeHighlighterEx) {
        if (!map.containsKey(highlighter)) return

        runOnEDT {

            val afterLineElement = map.remove(highlighter)!!

            afterLineElement.forEach(Disposer::dispose)
        }
    }

    override fun attributesChanged(
        highlighter: RangeHighlighterEx,
        renderersChanged: Boolean,
        fontStyleOrColorChanged: Boolean
    ) {
        runOnEDT {

            beforeRemoved(highlighter)

            runOnEDT {
                afterAdded(highlighter)
            }
        }
    }
}
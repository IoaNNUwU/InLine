package com.ioannuwu.inline2.pluginlogic

import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.ex.RangeHighlighterEx
import com.intellij.openapi.editor.impl.event.MarkupModelListener
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.ioannuwu.inline2.pluginlogic.element.DefaultTextRenderer
import com.ioannuwu.inline2.pluginlogic.render.RenderDataElementMetrics
import com.ioannuwu.inline2.pluginlogic.render.RenderDataSelector
import com.ioannuwu.inline2.pluginlogic.render.metrics.FontMetrics
import com.ioannuwu.inline2.pluginlogic.render.metrics.LineMetrics
import com.ioannuwu.inline2.pluginlogic.utils.runOnEDT
import com.jetbrains.rd.util.ConcurrentHashMap

class ErrorMarkupModelListener(
    private val model: EditorModel,
    private val renderDataSelector: RenderDataSelector,
    private val editor: Editor
) : MarkupModelListener {

    private val map: MutableMap<RangeHighlighterEx, Disposable> = ConcurrentHashMap()

    override fun afterAdded(highlighter: RangeHighlighterEx) {

        val info = (highlighter.errorStripeTooltip as? HighlightInfo) ?: return

        val description = info.description ?: return
        if (description.isEmpty()) return
        if (map.containsKey(highlighter)) return

        val renderData = renderDataSelector(highlighter)!!

        val metrics = RenderDataElementMetrics(renderData, editor)

        runOnEDT {

            val afterLineElement: Disposable = model.addAfterLineEndElement(
                highlighter.startOffset, DefaultTextRenderer(metrics)
            )

            map[highlighter] = afterLineElement
        }
    }

    override fun beforeRemoved(highlighter: RangeHighlighterEx) {
        if (!map.containsKey(highlighter)) return

        runOnEDT {

            val afterLineElement = map.remove(highlighter)!!

            afterLineElement.dispose()
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

    fun updateAllHighlighters() {
        map.forEach { (t, _) ->
            attributesChanged(t,
                false,
                false)
        }
    }
}
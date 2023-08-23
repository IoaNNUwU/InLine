package com.ioannuwu.inline2.pluginlogic.element

import com.intellij.openapi.editor.EditorCustomElementRenderer
import com.intellij.openapi.editor.Inlay
import com.intellij.openapi.editor.markup.TextAttributes
import com.ioannuwu.inline.data.EffectType
import com.ioannuwu.inline2.pluginlogic.render.ElementMetrics
import com.ioannuwu.inline2.pluginlogic.render.metrics.otherdata.EffectTypeSelector
import com.ioannuwu.inline2.pluginlogic.render.metrics.otherdata.NumberOfWhitespaces
import java.awt.Graphics
import java.awt.Rectangle

class DefaultTextRenderer(
    private val renderMetrics: ElementMetrics,
    private val effectTypeSelector: () -> EffectType,
) : EditorCustomElementRenderer {

    override fun calcWidthInPixels(inlay: Inlay<*>): Int =
        renderMetrics.stringWidth(renderMetrics.text() + renderMetrics.charWidth() * 2)

    override fun paint(
        inlay: Inlay<*>,
        g: Graphics,
        targetRegion: Rectangle,
        textAttributes: TextAttributes
    ) {
        val text = renderMetrics.text()

        if (text.isBlank()) return
        if (renderMetrics.textColor() == null) return

        if (renderMetrics.effectColor() != null) {

            when (effectTypeSelector()) {
                EffectType.NONE -> Unit

                EffectType.BOX -> with(g) {
                    color = renderMetrics.effectColor()
                    font = renderMetrics.font()

                    drawRoundRect(
                        targetRegion.x - renderMetrics.lineHeight() / 20 * 3,
                        targetRegion.y + renderMetrics.lineHeight() / 20,
                        targetRegion.width - renderMetrics.lineHeight() / 20 * 2,
                        targetRegion.height - renderMetrics.lineHeight() / 20 * 2,
                        renderMetrics.lineHeight() / 6,
                        renderMetrics.lineHeight() / 6,
                    )
                }

                EffectType.SHADOW -> with(g) {

                    color = renderMetrics.effectColor()
                    font = renderMetrics.font()

                    drawString(
                        text,
                        (targetRegion.x + renderMetrics.lineHeight() * 0.1).toInt(),
                        (targetRegion.y + renderMetrics.ascent() + renderMetrics.lineHeight() * 0.05).toInt(),
                    )
                }
            }
        }

        with(g) {
            color = renderMetrics.textColor()
            font = renderMetrics.font()

            drawString(
                text,
                targetRegion.x,
                targetRegion.y + renderMetrics.ascent(),
            )
        }
    }
}
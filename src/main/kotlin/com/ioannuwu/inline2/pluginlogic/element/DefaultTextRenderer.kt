package com.ioannuwu.inline2.pluginlogic.element

import com.intellij.openapi.editor.EditorCustomElementRenderer
import com.intellij.openapi.editor.Inlay
import com.intellij.openapi.editor.markup.TextAttributes
import com.ioannuwu.inline2.pluginlogic.render.ElementMetrics
import java.awt.Graphics
import java.awt.Rectangle

class DefaultTextRenderer(
    private val renderMetrics: ElementMetrics,
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

        with(g) {
            color = renderMetrics.textColor()
            font = renderMetrics.font()

            drawString(
                text,
                targetRegion.x + renderMetrics.charWidth(),
                targetRegion.y + renderMetrics.ascent(),
            )
        }
    }
}
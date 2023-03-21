package com.ioannuwu.inline

import com.intellij.openapi.editor.Inlay
import com.intellij.openapi.editor.markup.TextAttributes
import com.ioannuwu.inline.ui.render.elements.graphiccomponents.FontMetricsProvider
import com.ioannuwu.inline.ui.render.elements.graphiccomponents.PrettyWidth
import java.awt.Color
import java.awt.Graphics
import java.awt.Rectangle

interface EffectComponentKt : PrettyWidth {

    fun draw(inlay: Inlay<*>, g: Graphics, targetRegion: Rectangle, textAttributes: TextAttributes) {}


    class Box(
        private val fontMetricsProvider: FontMetricsProvider,
        private val color: Color,
        private val arc: Int
    ) : EffectComponentKt {

        override fun draw(inlay: Inlay<*>, g: Graphics, targetRegion: Rectangle, textAttributes: TextAttributes) {
            val magicNumberToFixBoxBlinking = 1;

            g.color = color
            g.drawRoundRect(
                targetRegion.x + fontMetricsProvider.fontMetrics().charWidth('a'),
                targetRegion.y + magicNumberToFixBoxBlinking,
                targetRegion.width,
                targetRegion.height - 2 * magicNumberToFixBoxBlinking,
                arc, arc
            )
        }
    }

    class Shadow(
        private val color: Color,
        private val parent:
    ) : EffectComponentKt {

        override fun draw(inlay: Inlay<*>, g: Graphics, targetRegion: Rectangle, textAttributes: TextAttributes) {
            // TODO AMONG US
        }
    }

    object EMPTY : EffectComponentKt
}
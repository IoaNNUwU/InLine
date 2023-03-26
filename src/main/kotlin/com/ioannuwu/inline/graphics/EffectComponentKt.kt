package com.ioannuwu.inline.graphics

import java.awt.Color
import java.awt.Graphics
import java.awt.Rectangle

interface EffectComponentKt : GraphicsComponentKt {

    override val width: Int
        get() = 0

    override val priority: Int
        get() = -100

    class Box(
        private val fontMetricsProvider: FontMetricsProvider,
        private val color: Color,
    ) : EffectComponentKt {

        override fun draw(g: Graphics, targetRegion: Rectangle) {
            val magicNumberToFixBoxBlinking = 1

            val fontMetrics = fontMetricsProvider.fontMetrics()

            val arc = fontMetrics.height * 2 / 10

            g.color = color
            g.drawRoundRect(
                targetRegion.x + fontMetrics.charWidth('a'),
                targetRegion.y + magicNumberToFixBoxBlinking,
                targetRegion.width,
                targetRegion.height - 2 * magicNumberToFixBoxBlinking,
                arc, arc,
            )
        }
    }

    class Shadow(
        private val color: Color,
        private val parent: TextComponent,
    ) : EffectComponentKt {

        override fun draw(g: Graphics, targetRegion: Rectangle) {
            parent.drawFancy(g, targetRegion, color)
        }
    }

    object NONE : EffectComponentKt {
        override fun draw(g: Graphics, targetRegion: Rectangle) {}
    }
}
package com.ioannuwu.inline.domain.graphics

import com.ioannuwu.inline.data.FontDataProvider
import java.awt.Color
import java.awt.Graphics
import java.awt.Rectangle

interface EffectComponentKt : GraphicsComponentKt {

    override val width: Int
        get() = 0

    override val priority: Int
        get() = -100

    class Box(
        private val fontMetricsProvider: FontDataProvider,
        private val color: Color,
    ) : EffectComponentKt {

        override fun draw(g: Graphics, targetRegion: Rectangle) {
            val magicNumberToFixBoxBlinking = 1

            val fontMetrics = fontMetricsProvider.fontMetrics

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

        override fun toString(): String = "Box( $color )"
    }

    class Shadow(
        private val color: Color,
        private val parent: TextComponent,
    ) : EffectComponentKt {

        override fun draw(g: Graphics, targetRegion: Rectangle) {
            parent.drawFancy(g, targetRegion, color)
        }

        override fun toString(): String = "Shadow( $color )"
    }

    object EMPTY : EffectComponentKt {
        override fun draw(g: Graphics, targetRegion: Rectangle) {}

        override fun toString(): String = "Empty"
    }
}
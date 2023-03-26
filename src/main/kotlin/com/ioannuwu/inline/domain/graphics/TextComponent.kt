package com.ioannuwu.inline.domain.graphics

import com.ioannuwu.inline.data.FontDataProvider
import java.awt.Color
import java.awt.Graphics
import java.awt.Rectangle

interface TextComponent : GraphicsComponentKt {

    override fun drawFancy(g: Graphics, targetRegion: Rectangle, color: Color)

    class AfterLineText(
        private val fontData: FontDataProvider,
        private val textColor: Color,
        private val text: String,
    ) : TextComponent {

        override fun drawFancy(g: Graphics, targetRegion: Rectangle, color: Color) {
            g.color = color
            g.font = fontData.font

            val centerX = width / 2f + targetRegion.x
            val fontMetrics = fontData.fontMetrics

            g.drawString(text,
                (centerX - fontMetrics.stringWidth(text).toFloat() / 2 + fontMetrics.charWidth('a')).toInt(),
                targetRegion.y + fontData.lineHeight * 3 / 4)
        }

        override fun draw(g: Graphics, targetRegion: Rectangle) {
            drawFancy(g, targetRegion, textColor)
        }

        override val width: Int
            get() {
                val fontMetrics = fontData.fontMetrics
                return fontMetrics.stringWidth(text) + fontMetrics.charWidth('a') * 2
            }
    }


}
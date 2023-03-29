package com.ioannuwu.inline.domain.elements

import com.intellij.openapi.editor.EditorCustomElementRenderer
import com.intellij.openapi.editor.Inlay
import com.intellij.openapi.editor.markup.TextAttributes
import com.ioannuwu.inline.data.FontData
import com.ioannuwu.inline.domain.NumberOfWhitespaces
import com.ioannuwu.inline.domain.graphics.GraphicsComponentKt
import java.awt.Color
import java.awt.Graphics
import java.awt.Rectangle

/**
 * Indentation level should be calculated from `offsetFromLineStart`
 */
class RustStyleElementRenderer(
    private val graphicsComponents: Collection<GraphicsComponentKt>,
    private val offsetFromLineStart: Int,
    private val editorFontData: FontData,
    private val numberOfWhitespaces: NumberOfWhitespaces,
    private val priority: Int,
    private val arrowColor: Color,
) : EditorCustomElementRenderer {


    override fun calcWidthInPixels(inlay: Inlay<*>): Int =
        graphicsComponents.sumOf(GraphicsComponentKt::width)

    override fun paint(inlay: Inlay<*>, g: Graphics, targetRegion: Rectangle, textAttributes: TextAttributes) {

        val charWidth = editorFontData.fontMetrics.charWidth('a')

        val newXWithOffset = targetRegion.x + charWidth * offsetFromLineStart +
                charWidth * numberOfWhitespaces.numberOfWhitespaces

        val newTargetRegion = Rectangle(newXWithOffset, targetRegion.y, targetRegion.width, targetRegion.height)

        val indentationLevel = -priority - 1
        val lineHeight = editorFontData.lineHeight

        if (indentationLevel == 0) {
            graphicsComponents.asSequence()
                .sortedBy(GraphicsComponentKt::priority)
                .forEach {
                    it.draw(
                        g,
                        Rectangle(
                            newTargetRegion.x - charWidth * 6 / 10,
                            newTargetRegion.y,
                            newTargetRegion.width,
                            newTargetRegion.height
                        )
                    )
                }

            g.color = arrowColor
            g.font = editorFontData.font

            g.drawString("^", newXWithOffset, targetRegion.y + lineHeight * 49 / 100)

        } else {

            graphicsComponents.asSequence()
                .sortedBy(GraphicsComponentKt::priority)
                .forEach {
                    it.draw(
                        g,
                        Rectangle(
                            newTargetRegion.x - charWidth * 2,
                            newTargetRegion.y,
                            newTargetRegion.width,
                            newTargetRegion.height
                        )
                    )
                }

            g.color = arrowColor
            g.font = editorFontData.font

            for (i in 0 until indentationLevel) {
                g.drawString("|", newXWithOffset, targetRegion.y - lineHeight * i)
            }
            g.drawString("^", newXWithOffset, targetRegion.y + lineHeight * 49 / 100 - lineHeight * indentationLevel)
        }
    }
}
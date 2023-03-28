package com.ioannuwu.inline.domain.elements

import com.intellij.openapi.editor.EditorCustomElementRenderer
import com.intellij.openapi.editor.Inlay
import com.intellij.openapi.editor.markup.TextAttributes
import com.ioannuwu.inline.data.FontDataProvider
import com.ioannuwu.inline.domain.graphics.GraphicsComponentKt
import java.awt.Graphics
import java.awt.Rectangle

/**
 * Indentation level should be calculated from `offsetFromLineStart`
 */
class RustStyleElementRenderer(
    private val graphicsComponents: Collection<GraphicsComponentKt>,
    private val offsetFromLineStart: Int,
    private val editorFontDataProvider: FontDataProvider
) : EditorCustomElementRenderer {


    override fun calcWidthInPixels(inlay: Inlay<*>): Int =
        graphicsComponents.sumOf(GraphicsComponentKt::width)

    override fun paint(inlay: Inlay<*>, g: Graphics, targetRegion: Rectangle, textAttributes: TextAttributes) {

        val charWidth = editorFontDataProvider.fontMetrics.charWidth('a')

        val newX = targetRegion.x + charWidth * offsetFromLineStart

        val newTargetRegion = Rectangle(newX,targetRegion.y,targetRegion.width, targetRegion.height)

        graphicsComponents.asSequence()
            .sortedBy(GraphicsComponentKt::priority)
            .forEach { it.draw(g, newTargetRegion) }
    }
}
package com.ioannuwu.inline.elements

import com.intellij.openapi.editor.EditorCustomElementRenderer
import com.intellij.openapi.editor.Inlay
import com.intellij.openapi.editor.markup.TextAttributes
import com.ioannuwu.inline.graphics.GraphicsComponentKt
import java.awt.Graphics
import java.awt.Rectangle

class MyElementRendererKt(
    private val graphicsComponents: Collection<GraphicsComponentKt>
) : EditorCustomElementRenderer {

    override fun calcWidthInPixels(inlay: Inlay<*>): Int =
        graphicsComponents.sumOf(GraphicsComponentKt::width)

    override fun paint(inlay: Inlay<*>, g: Graphics, targetRegion: Rectangle, textAttributes: TextAttributes) {
        graphicsComponents.asSequence()
            .sortedBy(GraphicsComponentKt::priority)
            .forEach {
                // println("AmongUs")
                it.draw(g, targetRegion)
            }
    }
}
package com.ioannuwu.inline.domain.elements

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.Inlay
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.ioannuwu.inline.domain.MyTextAttributes
import com.ioannuwu.inline.domain.graphics.GraphicsComponentKt
import java.awt.Color
import javax.swing.Icon

/**
 * this interface assumes that the render() and render() methods
 * will be called 1 time each one after the other
 */
interface RenderElementKt {

    fun render(editor: Editor)

    fun unrender()


    class Background(private val backgroundColor: Color, private val offset: Int) : RenderElementKt {

        private var currentHighlighter: RangeHighlighter? = null

        override fun render(editor: Editor) {
            if (currentHighlighter != null) return
            val lineNumber = editor.document.getLineNumber(offset)
            currentHighlighter = editor.markupModel
                .addLineHighlighter(lineNumber, 0,
                    MyTextAttributes(backgroundColor)
                )
        }

        override fun unrender() {
            currentHighlighter?.dispose()
        }
    }

    class Gutter(private val icon: Icon, private val offset: Int) : RenderElementKt {

        private var currentHighlighter: RangeHighlighter? = null

        override fun render(editor: Editor) {
            if (currentHighlighter != null) return
            val lineNumber = editor.document.getLineNumber(offset)
            val highlighter = editor.markupModel
                .addLineHighlighter(lineNumber, 0, MyTextAttributes.EMPTY)
            highlighter.gutterIconRenderer =
                MyGutterRenderer(icon)
            currentHighlighter = highlighter
        }

        override fun unrender() {
            currentHighlighter?.dispose()
        }
    }

    class Text(
        private val effects: List<GraphicsComponentKt>,
        private val offset: Int,
    ) : RenderElementKt {

        private var currentInlay: Inlay<*>? = null

        override fun render(editor: Editor) {
            if (currentInlay != null) return
            currentInlay = editor.inlayModel.addAfterLineEndElement(
                offset, false, MyElementRendererKt(effects)
            )
        }

        override fun unrender() {
            currentInlay?.dispose()
        }
    }
}
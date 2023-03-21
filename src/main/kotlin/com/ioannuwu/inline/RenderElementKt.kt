package com.ioannuwu.inline

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorCustomElementRenderer
import com.intellij.openapi.editor.Inlay
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.ioannuwu.inline.domain.utils.MyTextAttributes
import com.ioannuwu.inline.ui.render.MyGutterRenderer
import java.awt.Color
import javax.swing.Icon

/**
 * Manages state of part of error representation in editor
 */
interface RenderElementKt {

    fun render(editor: Editor)

    fun unrender()

    class Background(
        private val backgroundColor: Color,
        private val lineNumber: Int,
    ) : RenderElementKt {

        private var currentHighlighter: RangeHighlighter? = null

        override fun render(editor: Editor) {
            if (currentHighlighter != null) return
            currentHighlighter = editor.markupModel
                .addLineHighlighter(lineNumber, 0, MyTextAttributes(backgroundColor))
        }

        override fun unrender() {
            if (currentHighlighter == null) return
            currentHighlighter!!.dispose()
            currentHighlighter = null
        }

    }

    class Gutter(
        private val icon: Icon,
        private val lineNumber: Int,
    ) : RenderElementKt {

        private var currentHighlighter: RangeHighlighter? = null

        override fun render(editor: Editor) {
            if (currentHighlighter != null) return
            val highlighter = editor.markupModel
                .addLineHighlighter(lineNumber, 0, MyTextAttributes(null))
            highlighter.gutterIconRenderer = MyGutterRenderer(icon)
            currentHighlighter = highlighter
        }

        override fun unrender() {
            if (currentHighlighter == null) return
            currentHighlighter!!.dispose()
            currentHighlighter = null
        }
    }

    class Text(
        private val offset: Int,
        private val elementRenderer: EditorCustomElementRenderer,
    ) : RenderElementKt {

        private var currentInlay: Inlay<*>? = null

        override fun render(editor: Editor) {
            if (currentInlay != null) return
            currentInlay = editor.inlayModel.addAfterLineEndElement(offset, false, elementRenderer)
        }

        override fun unrender() {
            if (currentInlay == null) return
            currentInlay!!.dispose()
            currentInlay = null
        }
    }

}
package com.ioannuwu.inline2.pluginlogic.editormodel

import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorCustomElementRenderer
import com.intellij.ui.JBColor
import com.ioannuwu.inline2.pluginlogic.EditorModel
import com.ioannuwu.inline2.pluginlogic.utils.makeDisposable
import java.awt.Color

class EditorModelImpl(private val editor: Editor) : EditorModel {

    override fun addAfterLineEndElement(
        offset: Int,
        renderer: EditorCustomElementRenderer
    ): Disposable {

        return editor.inlayModel.addAfterLineEndElement(offset, true, renderer)!!
    }

    override fun addLineHighlighter(offset: Int, renderer: Color): Disposable {

        val lineNumber = try {
            editor.document.getLineNumber(offset)
        } catch (e: IndexOutOfBoundsException) {
            editor.document.lineCount
        }

        val highlighter = editor.markupModel.addLineHighlighter(lineNumber, 0, BackgroundAttributes(JBColor.BLACK))

        return highlighter.makeDisposable { it.dispose() }
    }
}
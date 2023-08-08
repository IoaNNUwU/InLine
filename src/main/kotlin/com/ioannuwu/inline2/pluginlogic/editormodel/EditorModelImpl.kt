package com.ioannuwu.inline2.pluginlogic.editormodel

import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorCustomElementRenderer
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.openapi.editor.markup.TextAttributes
import com.ioannuwu.inline2.pluginlogic.EditorModel
import com.ioannuwu.inline2.pluginlogic.utils.makeDisposable
import java.awt.Color
import javax.swing.Icon

class EditorModelImpl(private val editor: Editor) : EditorModel {

    override fun addAfterLineEndElement(
        offset: Int,
        renderer: EditorCustomElementRenderer
    ): Disposable {

        return editor.inlayModel.addAfterLineEndElement(offset, true, renderer)!!
    }

    override fun addUnderLineElement(
        offset: Int,
        renderer: EditorCustomElementRenderer
    ): Disposable {

        return editor.inlayModel.addBlockElement(offset, true, false, 9, renderer)!!
    }

    override fun addLineHighlighter(offset: Int, backgroundColor: Color): Disposable {

        val lineNumber = try {
            editor.document.getLineNumber(offset)
        } catch (e: IndexOutOfBoundsException) {
            editor.document.lineCount
        }

        val highlighter = editor.markupModel.addLineHighlighter(lineNumber, 9, BackgroundAttributes(backgroundColor))

        return highlighter.makeDisposable { it.dispose() }
    }

    override fun addGutterIcon(offset: Int, icon: Icon): Disposable {

        val lineNumber = try {
            editor.document.getLineNumber(offset)
        } catch (e: IndexOutOfBoundsException) {
            editor.document.lineCount
        }

        val highlighter = editor.markupModel.addLineHighlighter(
            lineNumber, 10,
            TextAttributes(null, null, null, null, 0)
        )

        highlighter.gutterIconRenderer = object : GutterIconRenderer() {
            override fun equals(other: Any?): Boolean = (other as? GutterIconRenderer)?.icon == icon
            override fun hashCode(): Int = icon.hashCode()
            override fun getIcon(): Icon = icon
        }

        return highlighter.makeDisposable { it.dispose() }
    }
}
package com.ioannuwu.inline2.pluginlogic

import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.EditorCustomElementRenderer
import java.awt.Color
import javax.swing.Icon

import com.intellij.openapi.util.Key

/**
 * Represents common view on the editor which can be
 * used to interact with necessary models
 *
 * @see com.intellij.openapi.editor.Editor
 *
 * @see com.intellij.openapi.editor.InlayModel
 * @see com.intellij.openapi.editor.markup.MarkupModel
 *
 * @see com.intellij.openapi.Disposable
 */
interface EditorModel {

    /**
     * Should be used only from EDT
     */
    fun addAfterLineEndElement(offset: Int, renderer: EditorCustomElementRenderer): Disposable

    /**
     * Should be used only from EDT
     */
    fun addLineHighlighter(offset: Int, backgroundColor: Color): Disposable

    /**
     * Should be used only from EDT
     */
    fun addGutterIcon(offset: Int, icon: Icon): Disposable

    /**
     * Should be used only from EDT
     */
    fun addUnderLineElement(offset: Int, renderer: EditorCustomElementRenderer): Disposable
}
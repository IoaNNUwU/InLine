package com.ioannuwu.inline2.pluginlogic.render

import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.Editor
import com.intellij.ui.JBColor
import com.ioannuwu.inline2.pluginlogic.render.ElementMetrics
import java.awt.Color
import java.awt.Font
import javax.swing.Icon

class EditorElementMetrics(
    private val editor: Editor,
    private val description: String,
) : ElementMetrics {

    override fun textColor(): Color = JBColor.RED

    override fun backgroundColor(): Color = JBColor.BLACK

    override fun effectColor(): Color = JBColor.YELLOW

    override fun text(): String = description

    override fun font(): Font = editor.component.font.deriveFont(editor.colorsScheme.editorFontSize.toFloat())

    override fun charWidth(): Int =
        editor.component.getFontMetrics(font()).charWidth(' ')

    override fun stringWidth(string: String): Int =
        editor.component.getFontMetrics(font()).stringWidth(string)

    override fun gutterIcon(): Icon = AllIcons.General.Error

    override fun lineHeight(): Int = editor.lineHeight

    override fun ascent(): Int = editor.ascent
}
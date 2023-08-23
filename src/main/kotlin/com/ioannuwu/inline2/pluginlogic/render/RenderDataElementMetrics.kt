package com.ioannuwu.inline2.pluginlogic.render

import com.intellij.openapi.editor.Editor
import com.ioannuwu.inline2.pluginlogic.render.metrics.RenderData
import com.ioannuwu.inline2.pluginlogic.render.metrics.otherdata.NumberOfWhitespaces
import java.awt.Color
import java.awt.Font
import javax.swing.Icon

class RenderDataElementMetrics(
    private val renderData: RenderData,
    private val editor: Editor,
) : ElementMetrics {

    override fun gutterIcon(): Icon? = renderData.gutterIcon()

    override fun font(): Font =
        renderData.font().deriveFont(editor.colorsScheme.editorFontSize.toFloat())

    override fun textColor(): Color? = renderData.textColor()
    override fun backgroundColor(): Color? = renderData.backgroundColor()
    override fun effectColor(): Color? = renderData.effectColor()

    override fun text(): String = renderData.text()

    override fun lineHeight(): Int = editor.lineHeight
    override fun ascent(): Int = editor.ascent

    override fun charWidth(): Int =
        editor.component.getFontMetrics(font()).charWidth(' ')

    override fun stringWidth(string: String): Int =
        editor.component.getFontMetrics(font()).stringWidth(string)

    override fun whitespaces(n: NumberOfWhitespaces): ElementMetrics {
        return object : ElementMetrics {
            override fun whitespaces(n : NumberOfWhitespaces): ElementMetrics = this
            override fun gutterIcon(): Icon? = null

            override fun font(): Font =
                renderData.font().deriveFont(editor.colorsScheme.editorFontSize.toFloat())

            override fun textColor(): Color? = null
            override fun backgroundColor(): Color? = null
            override fun effectColor(): Color? = null

            override fun text(): String = " ".repeat(n.numberOfWhitespaces())

            override fun lineHeight(): Int = editor.lineHeight
            override fun ascent(): Int = editor.ascent

            override fun charWidth(): Int =
                editor.component.getFontMetrics(font()).charWidth(' ')

            override fun stringWidth(string: String): Int =
                editor.component.getFontMetrics(font()).stringWidth(string)
        }
    }
}
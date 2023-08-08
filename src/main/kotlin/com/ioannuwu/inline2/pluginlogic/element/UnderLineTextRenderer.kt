package com.ioannuwu.inline2.pluginlogic.element

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorCustomElementRenderer
import com.intellij.openapi.editor.Inlay
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.openapi.editor.markup.TextAttributes
import com.ioannuwu.inline.data.EffectType
import com.ioannuwu.inline2.pluginlogic.render.ElementMetrics
import java.awt.Graphics
import java.awt.Rectangle

class UnderLineTextRenderer(
    private val renderMetrics: ElementMetrics,
    private val highlighter: RangeHighlighter,
    private val editor: Editor,
    private val effectTypeSelector: () -> EffectType,
) : EditorCustomElementRenderer {

    override fun calcWidthInPixels(inlay: Inlay<*>): Int =
        renderMetrics.stringWidth(renderMetrics.text() + renderMetrics.charWidth() * 2)

    override fun paint(
        inlay: Inlay<*>,
        g: Graphics,
        targetRegion: Rectangle,
        textAttributes: TextAttributes
    ) {
        val text = "^ " + renderMetrics.text()

        if (text.isBlank()) return
        if (renderMetrics.textColor() == null) return

        val line = try {
            editor.document.getLineNumber(highlighter.startOffset)
        } catch (e: IndexOutOfBoundsException) {
            editor.document.lineCount
        }
        val offsetFromLineStart = highlighter.startOffset - editor.document.getLineStartOffset(line)

        val editorCharWidth = editor.component.getFontMetrics(
            editor.component.font.deriveFont(editor.colorsScheme.editorFontSize.toFloat())
        ).stringWidth("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA").toFloat() / 28

        if (renderMetrics.effectColor() != null) {

            when (effectTypeSelector()) {
                EffectType.NONE -> Unit
                EffectType.BOX -> Unit // TODO
                EffectType.SHADOW -> with(g) {

                    color = renderMetrics.effectColor()
                    font = renderMetrics.font()

                    drawString(
                        text,
                        (targetRegion.x + offsetFromLineStart * editorCharWidth
                                + renderMetrics.lineHeight() * 0.1).toInt(),
                        (targetRegion.y + renderMetrics.ascent() + renderMetrics.lineHeight() * 0.05).toInt(),
                    )
                }
            }
        }

        with(g) {
            color = renderMetrics.textColor()
            font = renderMetrics.font()

            drawString(
                text,
                (targetRegion.x + offsetFromLineStart * editorCharWidth).toInt(),
                targetRegion.y + renderMetrics.ascent(),
            )
        }
    }
}
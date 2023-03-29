package com.ioannuwu.inline.domain.elements

import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.Editor
import com.ioannuwu.inline.data.FontDataProvider
import com.ioannuwu.inline.domain.MyTextAttributes
import com.ioannuwu.inline.domain.NumberOfWhitespacesProvider
import com.ioannuwu.inline.domain.graphics.GraphicsComponentKt
import com.ioannuwu.inline.domain.wrapper.RangeHighlighterAdapter
import java.awt.Color
import javax.swing.Icon

/**
 * this interface assumes that the render() and render() methods
 * will be called 1 time each one after the other
 */
interface RenderElementKt {

    fun render(editor: Editor): Disposable

    override fun toString(): String


    class Background(private val backgroundColor: Color, private val offset: Int) : RenderElementKt {

        override fun render(editor: Editor): Disposable {
            val lineNumber = editor.document.getLineNumber(offset)
            return RangeHighlighterAdapter(
                editor.markupModel.addLineHighlighter(
                    lineNumber, 0, MyTextAttributes(backgroundColor)
                )
            )
        }

        override fun toString(): String = "Background( $offset )"
    }

    class Gutter(private val icon: Icon, private val offset: Int) : RenderElementKt {

        override fun render(editor: Editor): Disposable {
            val lineNumber = editor.document.getLineNumber(offset)
            val highlighter = editor.markupModel
                .addLineHighlighter(lineNumber, 0, MyTextAttributes.EMPTY)
            highlighter.gutterIconRenderer = MyGutterRenderer(icon)
            return RangeHighlighterAdapter(highlighter)
        }

        override fun toString(): String = "Gutter( $offset )"
    }

    class DefaultText(
        private val effects: List<GraphicsComponentKt>,
        private val offset: Int,
        private val numberOfWhitespacesProvider: NumberOfWhitespacesProvider,
        private val editorFontMetricsProvider: FontDataProvider,
    ) : RenderElementKt {

        override fun render(editor: Editor): Disposable {
            return editor.inlayModel.addAfterLineEndElement(
                offset, true, MyElementRendererKt(effects, numberOfWhitespacesProvider, editorFontMetricsProvider)
            )!!
        }

        override fun toString(): String = "Default { offset:$offset, effects:$effects }"

    }

    class RustStyleText(
        private val effects: List<GraphicsComponentKt>,
        private val offset: Int,
        private val lineStartOffset: Int,
        private val editorFontDataProvider: FontDataProvider,
        private val numberOfWhitespacesProvider: NumberOfWhitespacesProvider,
    ) : RenderElementKt {

        override fun render(editor: Editor): Disposable {

            val offsetFromLineStart = offset - lineStartOffset

            return editor.inlayModel.addBlockElement(
                offset, true, false, 0,
                RustStyleElementRenderer(effects, offsetFromLineStart, editorFontDataProvider, numberOfWhitespacesProvider)
            )!!
        }

        override fun toString(): String =
            "RustStyletext { offset:$offset, fromLineStart:${offset - lineStartOffset}, effects:$effects }"
    }
}
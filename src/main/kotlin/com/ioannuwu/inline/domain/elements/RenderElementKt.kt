package com.ioannuwu.inline.domain.elements

import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.Editor
import com.ioannuwu.inline.data.FontData
import com.ioannuwu.inline.domain.MyTextAttributes
import com.ioannuwu.inline.domain.NumberOfWhitespaces
import com.ioannuwu.inline.domain.graphics.GraphicsComponentKt
import com.ioannuwu.inline.domain.wrapper.RangeHighlighterAdapter
import com.jetbrains.rd.util.printlnError
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
        private val numberOfWhitespaces: NumberOfWhitespaces,
        private val editorFontMetricsProvider: FontData,
    ) : RenderElementKt {

        override fun render(editor: Editor): Disposable {
            return editor.inlayModel.addAfterLineEndElement(
                offset, false, MyElementRendererKt(effects, numberOfWhitespaces, editorFontMetricsProvider)
            )!!
        }

        override fun toString(): String = "Default { offset:$offset, effects:$effects }"

    }

    class RustStyleText(
        private val effects: List<GraphicsComponentKt>,
        private val offset: Int,
        private val priority: Int,
        private val lineStartOffset: Int,
        private val editorFontData: FontData,
        private val numberOfWhitespaces: NumberOfWhitespaces,
        private val arrowColor: Color,
        private val doMoveRight: Boolean
    ) : RenderElementKt {

        override fun render(editor: Editor): Disposable {

            val offsetFromLineStart = offset - lineStartOffset

            val elem: Disposable = try {
                /*
                 * This try-catch block is very important. For some reason
                 * InlayMode.addBlockElements() sometimes crashes with IllegalStateException,
                 * but InlayMode.addAfterLineEndElement() doesn't. So it was surprising to
                 * encounter such an issue. This has to do with multithreading, but I was
                 * unable to find a way to use WriteAction.run {} because UI was freezing.
                 * So I came up with this solution. Just ignoring all unsuccessful block
                 * elements works surprisingly well, because it is happening only after
                 * deletion of big amount of highlighters, all of which was unnecessary
                 * to render.
                 */
                editor.inlayModel.addBlockElement(
                    offset, false, false, priority,
                    RustStyleElementRenderer(
                        effects,
                        offsetFromLineStart,
                        editorFontData,
                        numberOfWhitespaces,
                        priority,
                        arrowColor,
                        doMoveRight
                    )
                )!!
            } catch (e: IllegalStateException) {
                printlnError("RustStyleElement failed to add $e")
                Disposable {}
            }

            return elem
        }

        override fun toString(): String =
            "RustStyletext { offset:$offset, fromLineStart:${offset - lineStartOffset}, effects:$effects }"
    }
}
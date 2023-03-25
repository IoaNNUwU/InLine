package com.ioannuwu.inline.wrapper

import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.openapi.editor.markup.RangeHighlighter

interface RangeHighlighterWrapper {

    val priority: Int
    val description: String
    val offset: Int
    val lineNumber: Int

    fun isSufficient(): Boolean

    fun isValidInDocument(): Boolean


    class Impl(private val highlighter: RangeHighlighter): RangeHighlighterWrapper {

        private var markerIsSufficient = false

        /** higher is better */
        override val priority: Int
            get() = if (markerIsSufficient || isSufficient())
                (highlighter.errorStripeTooltip as HighlightInfo).severity.myVal
            else throw IllegalArgumentException("wrapper should be sufficient to use priority")

        override val description: String
            get() = if (markerIsSufficient || isSufficient())
                (highlighter.errorStripeTooltip as HighlightInfo).description
            else throw IllegalArgumentException("wrapper should be sufficient to use description")

        override val offset: Int
            get() = highlighter.startOffset

        override val lineNumber: Int
            get() = if (isValidInDocument())
                highlighter.document.getLineNumber(highlighter.startOffset)
            else
                -1

        override fun isSufficient(): Boolean {
            if (highlighter.errorStripeTooltip == null) return false
            if (highlighter.errorStripeTooltip !is HighlightInfo) return false
            if ((highlighter.errorStripeTooltip!! as HighlightInfo).description.isNullOrBlank()) return false
            markerIsSufficient = true
            return true
        }

        override fun isValidInDocument(): Boolean =
            offset > 0 && offset < highlighter.document.textLength

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as RangeHighlighterWrapper

            if (priority != other.priority) return false
            if (offset != other.offset) return false
            if (description != other.description) return false

            return true
        }

        override fun hashCode(): Int {
            var result = priority
            result = 31 * result + offset
            result = 31 * result + description.hashCode()
            return result
        }
    }

}

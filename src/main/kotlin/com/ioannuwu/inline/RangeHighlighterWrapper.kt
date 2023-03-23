package com.ioannuwu.inline;

import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.openapi.editor.markup.RangeHighlighter

data class RangeHighlighterWrapper(private val highlighter: RangeHighlighter) {

    /** higher is better */
    val priority: Int = (highlighter.errorStripeTooltip as HighlightInfo).severity.myVal

    val offset: Int = highlighter.startOffset

    val description: String = (highlighter.errorStripeTooltip as HighlightInfo).description

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

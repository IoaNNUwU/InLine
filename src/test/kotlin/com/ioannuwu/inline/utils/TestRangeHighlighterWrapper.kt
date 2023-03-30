package com.ioannuwu.inline.utils

import com.ioannuwu.inline.domain.wrapper.RangeHighlighterWrapper

open class TestRangeHighlighterWrapper(
    override val priority: Int,
    override val description: String,
    override val offset: Int,
    override val lineNumber: Int,
    private val isSufficient: Boolean,
    private val isValidInDocument: Boolean,
) : RangeHighlighterWrapper {

    override fun isSufficient(): Boolean = isSufficient

    override fun isValidInDocument(): Boolean = isValidInDocument
}
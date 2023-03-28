package com.ioannuwu.inline.domain

import com.ioannuwu.inline.domain.wrapper.RangeHighlighterWrapper

interface HighlightersValidator {

    fun isValid(highlighter: RangeHighlighterWrapper): Boolean
}
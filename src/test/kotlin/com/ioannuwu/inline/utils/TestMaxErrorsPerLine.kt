package com.ioannuwu.inline.utils

import com.ioannuwu.inline.domain.MaxErrorsPerLine

object TestMaxErrorsPerLine : MaxErrorsPerLine {
    const val MAX_PER_LINE = 3

    override val maxPerLine: Int = MAX_PER_LINE
}
package com.ioannuwu.inline.utils

import com.ioannuwu.inline.domain.MaxErrorsPerLineProvider

object TestMaxErrorsPerLine : MaxErrorsPerLineProvider {
    const val MAX_PER_LINE = 3

    override val maxPerLine: Int = MAX_PER_LINE
}
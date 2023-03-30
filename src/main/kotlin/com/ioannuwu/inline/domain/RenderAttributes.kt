package com.ioannuwu.inline.domain

import com.ioannuwu.inline.data.FontData

interface RenderAttributes {

    val offSetFromLineStart: Int

    val editorFontMetricsProvider: FontData


    class Impl(
        override val offSetFromLineStart: Int,
        override val editorFontMetricsProvider: FontData
    ) : RenderAttributes
}
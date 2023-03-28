package com.ioannuwu.inline.domain

import com.ioannuwu.inline.data.FontDataProvider

interface RenderAttributes {

    val offSetFromLineStart: Int

    val editorFontMetricsProvider: FontDataProvider


    class Impl(
        override val offSetFromLineStart: Int,
        override val editorFontMetricsProvider: FontDataProvider
    ) : RenderAttributes
}
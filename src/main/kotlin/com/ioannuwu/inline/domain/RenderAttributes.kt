package com.ioannuwu.inline.domain

interface RenderAttributes {

    val indentationLevel: Int


    class Impl(override val indentationLevel: Int) : RenderAttributes
}
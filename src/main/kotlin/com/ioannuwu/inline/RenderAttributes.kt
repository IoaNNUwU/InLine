package com.ioannuwu.inline

interface RenderAttributes {

    val indentationLevel: Int


    class Impl(override val indentationLevel: Int) : RenderAttributes
}
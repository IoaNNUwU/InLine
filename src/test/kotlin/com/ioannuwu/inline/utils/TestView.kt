package com.ioannuwu.inline.utils

import com.ioannuwu.inline.domain.View
import com.ioannuwu.inline.domain.elements.RenderElementKt

object TestView : View {

    var currentShow: List<Collection<RenderElementKt>> = listOf()
    override fun showLine(orderedList: List<Collection<RenderElementKt>>) {
        currentShow = ArrayList(orderedList)
    }

    var currentHide: Collection<RenderElementKt> = listOf()
    override fun hide(renderElements: Collection<RenderElementKt>) {
        currentHide = renderElements
    }

    var currentHideLine: Collection<Collection<RenderElementKt>> = listOf()
    override fun hideLine(renderElements: Collection<Collection<RenderElementKt>>) {
        currentHideLine = renderElements
    }
}
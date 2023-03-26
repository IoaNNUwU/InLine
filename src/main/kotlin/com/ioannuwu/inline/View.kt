package com.ioannuwu.inline

import com.intellij.openapi.editor.Editor
import com.ioannuwu.inline.elements.RenderElementKt

interface View {

    /**
     * ordered list contains collections of render elements ordered from left to right
     * where 0 is first (left) element on a line
     */
    fun showLine(orderedList: List<Collection<RenderElementKt>>)

    fun hide(renderElements: Collection<RenderElementKt>)

    fun hideLine(renderElements: Collection<Collection<RenderElementKt>>)

    class EditorView(private val editor: Editor) : View {

        override fun showLine(orderedList: List<Collection<RenderElementKt>>) {
            for (renderElementList in orderedList)
                for (renderElement in renderElementList) renderElement.render(editor)
        }

        override fun hide(renderElements: Collection<RenderElementKt>) =
            renderElements.forEach(RenderElementKt::unrender)

        override fun hideLine(renderElements: Collection<Collection<RenderElementKt>>) {
            renderElements.forEach { it.forEach(RenderElementKt::unrender) }
        }
    }
}
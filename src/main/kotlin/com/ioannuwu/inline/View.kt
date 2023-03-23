package com.ioannuwu.inline

import com.intellij.openapi.editor.Editor

interface View {

    /**
     * ordered list contains collections of render elements ordered from left to right
     * where 0 is first (left) element on a line
     */
    fun renderLine(orderedList: List<Collection<RenderElementKt>>)

    fun unrender(renderElement: RenderElementKt)

    fun unrender(renderElements: Collection<RenderElementKt>) =
        renderElements.forEach(::unrender)

    fun unrender(renderElements: Collection<Collection<RenderElementKt>>) =
        renderElements.forEach(::unrender)

    class EditorView(private val editor: Editor) : View {

        override fun renderLine(orderedList: List<Collection<RenderElementKt>>) {
            for (renderElementList in orderedList) {
                for (renderElement in renderElementList) renderElement.render(editor)
            }
        }

        override fun unrender(renderElement: RenderElementKt) {
            renderElement.unrender()
        }
    }
}
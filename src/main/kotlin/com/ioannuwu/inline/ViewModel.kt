package com.ioannuwu.inline

import com.intellij.openapi.editor.Editor

interface ViewModel {

    fun add(renderElements: RenderElementsKt)

    fun remove(renderElements: RenderElementsKt)


    class Impl(private val editor: Editor) : ViewModel {

        override fun add(renderElements: RenderElementsKt) {
            renderElements.render(editor)
        }

        override fun remove(renderElements: RenderElementsKt) {
            renderElements.unrender()
        }

    }
}
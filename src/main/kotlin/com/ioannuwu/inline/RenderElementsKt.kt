package com.ioannuwu.inline

import com.intellij.openapi.editor.Editor

class RenderElementsKt(private val renderElements: Collection<RenderElementKt>): RenderElementKt {

    override fun render(editor: Editor) {
        renderElements.forEach {
            it.render(editor)
        }
    }

    override fun unrender() {
        renderElements.forEach {
            it.unrender()
        }
    }
}
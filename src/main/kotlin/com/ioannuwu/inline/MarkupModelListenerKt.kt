package com.ioannuwu.inline

import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.editor.ex.RangeHighlighterEx
import com.intellij.openapi.editor.impl.event.MarkupModelListener
import com.ioannuwu.inline.domain.EditorCallback

class MarkupModelListenerKt(private val editorCallback: EditorCallback) : MarkupModelListener {

    override fun afterAdded(highlighter: RangeHighlighterEx) {
        editorCallback.onAdded(highlighter)
    }

    override fun beforeRemoved(highlighter: RangeHighlighterEx) {
        editorCallback.onRemoved(highlighter)
    }

    override fun attributesChanged(
        highlighter: RangeHighlighterEx,
        renderersChanged: Boolean,
        fontStyleOrColorChanged: Boolean
    ) {
        beforeRemoved(highlighter)
        afterAdded(highlighter)
    }
}
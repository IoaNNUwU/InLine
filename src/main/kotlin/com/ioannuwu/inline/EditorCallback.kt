package com.ioannuwu.inline

import com.intellij.openapi.editor.markup.RangeHighlighter

interface EditorCallback {

    fun onAdded(highlighter: RangeHighlighter)

    fun onRemoved(highlighter: RangeHighlighter)


    class Impl(
        private val viewModel: ViewModel,
        private val renderElementsProvider: RenderElementsProviderKt
    ) : EditorCallback {

        override fun onAdded(highlighter: RangeHighlighter) {
            TODO("Not yet implemented")
        }

        override fun onRemoved(highlighter: RangeHighlighter) {
            TODO("Not yet implemented")
        }

    }
}
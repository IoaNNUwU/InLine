package com.ioannuwu.inline.domain

import com.intellij.openapi.editor.markup.RangeHighlighter
import com.ioannuwu.inline.domain.wrapper.RangeHighlighterWrapper

interface EditorCallback {

    fun onAdded(highlighter: RangeHighlighter)

    fun onRemoved(highlighter: RangeHighlighter)


    class ViewModelEditorCallback(private val viewModel: ViewModel) : EditorCallback {

        private val map = HashMap<RangeHighlighter, RangeHighlighterWrapper>()

        override fun onAdded(highlighter: RangeHighlighter) {

            val wrapper = RangeHighlighterWrapper.Impl(highlighter)

            if (!wrapper.isSufficient()) return

            map[highlighter] = wrapper

            viewModel.add(wrapper)
        }

        override fun onRemoved(highlighter: RangeHighlighter) {

            val wrapper = map.remove(highlighter) ?: return
            viewModel.remove(wrapper)
        }
    }
}
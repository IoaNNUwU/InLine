package com.ioannuwu.inline.domain

import com.intellij.openapi.editor.markup.RangeHighlighter
import com.ioannuwu.inline.domain.wrapper.RangeHighlighterWrapper

interface EditorCallback {

    fun onAdded(highlighter: RangeHighlighter)

    fun onRemoved(highlighter: RangeHighlighter)


    class ViewModelEditorCallback(private val viewModel: ViewModel) : EditorCallback {

        private val wrappers = HashMap<RangeHighlighter, RangeHighlighterWrapper>()

        override fun onAdded(highlighter: RangeHighlighter) {

            val fromMap = wrappers[highlighter]

            val wrapper: RangeHighlighterWrapper =
                if (fromMap == null) {
                    val temp = RangeHighlighterWrapper.Impl(highlighter)
                    if (temp.isSufficient()) temp else return
                } else
                    fromMap

            wrappers[highlighter] = wrapper
            viewModel.add(wrapper)
        }

        override fun onRemoved(highlighter: RangeHighlighter) {

            val wrapper = wrappers[highlighter] ?: return
            wrappers.remove(highlighter)
            viewModel.remove(wrapper)
        }
    }
}
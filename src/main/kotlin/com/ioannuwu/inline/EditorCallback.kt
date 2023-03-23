package com.ioannuwu.inline

import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.openapi.editor.markup.RangeHighlighter

interface EditorCallback {

    fun onAdded(highlighter: RangeHighlighter)

    fun onRemoved(highlighter: RangeHighlighter)


    class ViewModelEditorCallback(private val viewModel: ViewModel) : EditorCallback {

        private val map = HashMap<RangeHighlighter, RangeHighlighterWrapper>()

        override fun onAdded(highlighter: RangeHighlighter) {
            if(!highlighter.isSufficient()) return

            val wrapper = RangeHighlighterWrapper(highlighter)
            map[highlighter] = wrapper

            viewModel.add(wrapper)
        }

        override fun onRemoved(highlighter: RangeHighlighter) {

            val wrapper = map.remove(highlighter) ?: return

            viewModel.remove(wrapper)
        }

        private fun RangeHighlighter.isSufficient(): Boolean {
            val highlighter = this
            if (highlighter.errorStripeTooltip == null) return false
            if (highlighter.errorStripeTooltip !is HighlightInfo) return false
            if ((highlighter.errorStripeTooltip!! as HighlightInfo).description.isNullOrBlank()) return false
            return true
        }

    }
}
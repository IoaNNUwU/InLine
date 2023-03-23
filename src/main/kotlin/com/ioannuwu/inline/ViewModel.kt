package com.ioannuwu.inline

import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.markup.RangeHighlighter

interface ViewModel { // TODO just like mode but better

    // Wrapper gives proper information about error so they are unique, but simplifies things
    // View Model improves appearance of And uses render Elements provider to generate additional
    // Nodes if needed

    fun add(highlighter: RangeHighlighterWrapper)

    fun remove(highlighter: RangeHighlighterWrapper)


    class Impl(
        private val view: View,
        private val document: Document,
        private val renderElementsProvider,
    ) : ViewModel {

        private val map = HashMap<RangeHighlighterWrapper, Set<RenderElementKt>>()

        override fun add(highlighter: RangeHighlighterWrapper) {

        }

        override fun remove(highlighter: RangeHighlighterWrapper) {
            TODO("Not yet implemented")
        }

        private fun RangeHighlighter.myIsValid() =
            startOffset > 0 && startOffset < document.textLength

    }
}
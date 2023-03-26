package com.ioannuwu.inline.domain

import com.intellij.openapi.editor.Document
import com.ioannuwu.inline.domain.elements.RenderElementKt
import com.ioannuwu.inline.domain.wrapper.RangeHighlighterWrapper
import com.ioannuwu.inline.domain.wrapper.WrapperComparator

interface ViewModel {

    fun add(highlighter: RangeHighlighterWrapper)

    fun remove(highlighter: RangeHighlighterWrapper)

    class Impl(
        private val view: View,
        private val renderElementsProvider: RenderElementsProviderKt,
        private val document: Document,
        private val maxPerLine: MaxErrorsPerLineProvider,
    ) : ViewModel {

        private val map = HashMap<RangeHighlighterWrapper, List<RenderElementKt>>()

        override fun add(highlighter: RangeHighlighterWrapper) {

            map[highlighter] = emptyList()

            map.keys
                .filter { !it.isValidInDocument() }
                .forEach { view.hide(map.remove(it) ?: emptyList()) }

            val highlightersOnCurrentLineSorted = map.keys.asSequence()
                .filter { it.lineNumber == highlighter.lineNumber }
                .sortedWith(WrapperComparator.ByPriority then WrapperComparator.ByOffsetLowestIsLast)
                .toList()

            highlightersOnCurrentLineSorted.forEach {
                view.hide(map[it] ?: emptyList())
                map[it] = emptyList()
            }

            val top = highlightersOnCurrentLineSorted.asSequence()
                .take(maxPerLine.maxPerLine)
                .sortedWith(WrapperComparator.ByOffsetLowestIsFirstLikeOnTheLine)
                .toList()

            val list = mutableListOf<Collection<RenderElementKt>>()
            for (i in 0 until top.count()) {
                val wrapper = top[i]
                val elements = renderElementsProvider.provide(wrapper, RenderAttributes.Impl(i))
                map[wrapper] = elements
                list.add(i, elements)
            }
            view.showLine(list)
        }

        override fun remove(highlighter: RangeHighlighterWrapper) {

            val elem = map.remove(highlighter) ?: return
            view.hide(elem)

            map.keys
                .filter { !it.isValidInDocument() }
                .forEach { view.hide(map.remove(it) ?: emptyList()) }

            val lineNumber = if (highlighter.isValidInDocument()) highlighter.lineNumber else document.lineCount

            val highlightersOnCurrentLineSorted = map.keys.asSequence()
                .filter { it.lineNumber == lineNumber }
                .sortedWith(WrapperComparator.ByPriority then WrapperComparator.ByOffsetLowestIsLast)
                .toList()

            highlightersOnCurrentLineSorted.forEach {
                view.hide(map[it] ?: emptyList())
                map[it] = emptyList()
            }

            val top = highlightersOnCurrentLineSorted.asSequence()
                .take(maxPerLine.maxPerLine)
                .sortedWith(WrapperComparator.ByOffsetLowestIsFirstLikeOnTheLine)
                .toList()

            val list = mutableListOf<Collection<RenderElementKt>>()
            for (i in 0 until top.count()) {
                val wrapper = top[i]
                val elements = renderElementsProvider.provide(wrapper, RenderAttributes.Impl(i))
                map[wrapper] = elements
                list.add(i, elements)
            }
            view.showLine(list)
        }
    }
}
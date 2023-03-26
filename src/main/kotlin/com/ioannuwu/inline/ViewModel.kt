package com.ioannuwu.inline

import com.intellij.openapi.editor.Document
import com.ioannuwu.inline.elements.RenderElementKt
import com.ioannuwu.inline.wrapper.RangeHighlighterWrapper
import com.ioannuwu.inline.wrapper.WrapperComparator

interface ViewModel {

    fun add(highlighter: RangeHighlighterWrapper)

    fun remove(highlighter: RangeHighlighterWrapper)

    class Impl(
        private val view: View,
        private val renderElementsProvider: RenderElementsProviderKt,
        private val document: Document,
        private val maxPerLine: MaxPerLineKt,
    ) : ViewModel {

        private val map = HashMap<RangeHighlighterWrapper, List<RenderElementKt>>()

        override fun add(highlighter: RangeHighlighterWrapper) {
            println()
            println("-=+ ADD +=-")

            println(highlighter)
            map.printOnEachLine("MAP")
            println("  FROMMAP: ${map[highlighter]}")

            map[highlighter] = emptyList()

            // TODO add rust style

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

            highlightersOnCurrentLineSorted.printOnEachLine("LINE")

            val top = highlightersOnCurrentLineSorted.asSequence()
                .take(maxPerLine.maxPerLine)
                .sortedWith(WrapperComparator.ByOffsetLowestIsFirstLikeOnTheLine)
                .toList()

            top.printOnEachLine("TOP")

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
            println()
            println("-=+ REMOVE +=-")

            println(highlighter)
            map.printOnEachLine("MAP")
            println("  FROMMAP: ${map[highlighter]}")

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

            highlightersOnCurrentLineSorted.printOnEachLine("LINE")

            highlightersOnCurrentLineSorted.forEach {
                view.hide(map[it] ?: emptyList())
                map[it] = emptyList()
            }

            val top = highlightersOnCurrentLineSorted.asSequence()
                .take(maxPerLine.maxPerLine)
                .sortedWith(WrapperComparator.ByOffsetLowestIsFirstLikeOnTheLine)
                .toList()

            top.printOnEachLine("TOP")

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

private fun <T> Collection<T>.printOnEachLine(prefix: String) {
    println("  $prefix:")
    this.forEach {
        println("    $it")
    }
}

private fun <K, V> Map<K, V>.printOnEachLine(prefix: String) {
    println("  $prefix:")
    this.forEach { (k, v) ->
        println("    $k: $v;")
    }
}
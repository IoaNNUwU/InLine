package com.ioannuwu.inline.domain

import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.Disposer
import com.ioannuwu.inline.domain.wrapper.RangeHighlighterWrapper
import com.ioannuwu.inline.domain.wrapper.WrapperComparator

interface ViewModel {

    fun add(highlighter: RangeHighlighterWrapper)

    fun remove(highlighter: RangeHighlighterWrapper)

    class Impl(
        private val renderElementsProvider: RenderElementsProvider,
        private val editor: Editor,
        private val maxPerLine: MaxErrorsPerLine,
        private val highlightersValidator: HighlightersValidator,
    ) : ViewModel {

        private val map = HashMap<RangeHighlighterWrapper, List<Disposable>>()

        override fun add(highlighter: RangeHighlighterWrapper) {

            hideFromMapAndRemoveInvalid()

            map[highlighter] = emptyList()

            displayHighlightersOnCurrentLineAndUpdateMap(highlighter.lineNumber)
        }

        override fun remove(highlighter: RangeHighlighterWrapper) {
            val dis = map.remove(highlighter) ?: return
            dis.forEach(Disposable::dispose)

            hideFromMapAndRemoveInvalid()

            val lineNumber = if (highlighter.isValidInDocument())
                highlighter.lineNumber
            else
                editor.document.lineCount

            displayHighlightersOnCurrentLineAndUpdateMap(lineNumber)
        }

        private fun displayHighlightersOnCurrentLineAndUpdateMap(currentLine: Int) {

            val highlightersOnCurrentLineSorted = map.keys.asSequence()
                .filter { it.lineNumber == currentLine }
                .sortedWith(WrapperComparator.ByPriority)
                .toList()

            highlightersOnCurrentLineSorted.forEach {
                val disposables = map[it] ?: emptyList()
                disposables.forEach(Disposer::dispose)
                map[it] = emptyList()
            }

            val topN = highlightersOnCurrentLineSorted.asSequence()
                .filter { highlightersValidator.isValid(it) }
                .take(maxPerLine.maxPerLine)
                .toList()

            val lineStartOffset = editor.document.getLineStartOffset(currentLine)

            val lineRenderElements = renderElementsProvider.provide(lineStartOffset, topN)

            val disposablesAfterRender = mutableListOf<List<Disposable>>()

            for (renderElementCollection in lineRenderElements) {
                val innerList = mutableListOf<Disposable>()
                for (elem in renderElementCollection) {
                    val dis = elem.render(editor)
                    innerList.add(dis)
                }
                disposablesAfterRender.add(innerList)
            }

            for (i in disposablesAfterRender.indices) {
                val fromTop: RangeHighlighterWrapper = topN[i]
                map[fromTop] = disposablesAfterRender[i]
            }
        }

        private fun hideFromMapAndRemoveInvalid() {

            map.keys
                .filter { !it.isValidInDocument() }
                .forEach {
                    val correspondingDisposables = map.remove(it) ?: emptyList()
                    correspondingDisposables.forEach(Disposer::dispose)
                }
        }
    }
}
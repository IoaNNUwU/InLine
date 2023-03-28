package com.ioannuwu.inline.domain.wrapper

import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.markup.RangeHighlighter

class RangeHighlighterAdapter(private val rangeHighlighter: RangeHighlighter): Disposable {
    override fun dispose() {
        rangeHighlighter.dispose()
    }

    override fun toString(): String {
        return "Adapter { $rangeHighlighter }"
    }
}
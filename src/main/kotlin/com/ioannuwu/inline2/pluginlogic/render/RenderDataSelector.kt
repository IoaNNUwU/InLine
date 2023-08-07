package com.ioannuwu.inline2.pluginlogic.render

import com.intellij.openapi.editor.markup.RangeHighlighter
import com.ioannuwu.inline2.pluginlogic.render.metrics.RenderData

@FunctionalInterface
interface RenderDataSelector {

    operator fun invoke(highlighter: RangeHighlighter): RenderData?
}
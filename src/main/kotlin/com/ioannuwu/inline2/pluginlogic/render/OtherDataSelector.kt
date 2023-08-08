package com.ioannuwu.inline2.pluginlogic.render

import com.intellij.openapi.editor.markup.RangeHighlighter
import com.ioannuwu.inline2.pluginlogic.render.metrics.OtherData

interface OtherDataSelector {

     fun selectOtherData(highlighter: RangeHighlighter): OtherData
}
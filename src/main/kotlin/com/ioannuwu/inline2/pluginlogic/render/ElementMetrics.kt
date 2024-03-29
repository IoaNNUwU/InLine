package com.ioannuwu.inline2.pluginlogic.render

import com.ioannuwu.inline2.pluginlogic.render.metrics.FontMetrics
import com.ioannuwu.inline2.pluginlogic.render.metrics.LineMetrics
import com.ioannuwu.inline2.pluginlogic.render.metrics.RenderData
import com.ioannuwu.inline2.pluginlogic.render.metrics.otherdata.NumberOfWhitespaces

/**
 * Represents text element in editor
 *
 * Provides all information needed to place and paint text element
 * in editor
 */
interface ElementMetrics : RenderData, LineMetrics, FontMetrics {
    fun whitespaces(n: NumberOfWhitespaces): ElementMetrics
}
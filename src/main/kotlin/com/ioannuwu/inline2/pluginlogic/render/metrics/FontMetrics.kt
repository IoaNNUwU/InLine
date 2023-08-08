package com.ioannuwu.inline2.pluginlogic.render.metrics

import com.ioannuwu.inline2.pluginlogic.render.metrics.renderdata.FontSupplier

interface FontMetrics : FontSupplier {

    fun charWidth(): Int

    fun stringWidth(string: String): Int
}
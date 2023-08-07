package com.ioannuwu.inline2.pluginlogic.render.metrics

interface FontMetrics : FontSupplier {

    fun charWidth(): Int

    fun stringWidth(string: String): Int
}
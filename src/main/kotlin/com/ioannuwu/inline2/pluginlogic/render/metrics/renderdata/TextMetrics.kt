package com.ioannuwu.inline2.pluginlogic.render.metrics.renderdata

import java.awt.Color

interface TextMetrics {

    fun textColor(): Color?

    fun backgroundColor(): Color?

    fun effectColor(): Color?

    fun text(): String
}
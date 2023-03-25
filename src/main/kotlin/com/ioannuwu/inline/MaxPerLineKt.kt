package com.ioannuwu.inline

import com.ioannuwu.inline.data.MySettingsService

interface MaxPerLineKt {

    val maxPerLine: Int


    class BySettings(private val settingsService: MySettingsService) : MaxPerLineKt {

        override val maxPerLine: Int
            get() = settingsService.state.maxErrorsPerLine

    }
}
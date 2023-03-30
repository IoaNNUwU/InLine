package com.ioannuwu.inline.domain

import com.ioannuwu.inline.data.DefaultSettings
import com.ioannuwu.inline.data.MySettingsService
import com.ioannuwu.inline.domain.settings.SettingsChangeEvent
import com.ioannuwu.inline.domain.settings.SettingsChangeListener
import com.ioannuwu.inline.domain.settings.SettingsChangeObservable

interface MaxErrorsPerLine {

    val maxPerLine: Int


    object BySettings : MaxErrorsPerLine, SettingsChangeListener {

        init {
            MySettingsService.OBSERVABLE.subscribe(this, SettingsChangeObservable.Priority.DEFAULT)
        }

        private var _maxPerLine: Int = DefaultSettings.MAX_ERRORS_PER_LINE

        override val maxPerLine: Int
            get() = _maxPerLine

        override fun onSettingsChange(event: SettingsChangeEvent) {
            _maxPerLine = event.newSettingsState.maxErrorsPerLine
        }

    }
}
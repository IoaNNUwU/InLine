package com.ioannuwu.inline.domain

import com.ioannuwu.inline.data.MySettingsService
import com.ioannuwu.inline.domain.settings.SettingsChangeEvent
import com.ioannuwu.inline.domain.settings.SettingsChangeListener
import com.ioannuwu.inline.domain.settings.SettingsChangeObservable

interface NumberOfWhitespacesProvider {

    fun provide(): Int


    object BySettings : NumberOfWhitespacesProvider, SettingsChangeListener {

        init {
            MySettingsService.OBSERVABLE.subscribe(this, SettingsChangeObservable.Priority.DEFAULT)
        }

        private var currentNumberOfWhitespaces: Int = 0

        override fun provide(): Int = currentNumberOfWhitespaces

        override fun onSettingsChange(event: SettingsChangeEvent) {
            currentNumberOfWhitespaces = event.newSettingsState.numberOfWhitespaces
        }

    }
}
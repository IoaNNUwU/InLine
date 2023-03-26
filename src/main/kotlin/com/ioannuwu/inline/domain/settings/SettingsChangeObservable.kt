package com.ioannuwu.inline.domain.settings

import com.ioannuwu.inline.data.SettingsState

interface SettingsChangeObservable {

    fun subscribe(listener: SettingsChangeListener, priority: Priority)

    fun unsubscribe(listener: SettingsChangeListener)

    fun notify(newSettingsState: SettingsState)

    enum class Priority { FIRST, DEFAULT, LAST }
}
package com.ioannuwu.inline2.settings.event

import com.ioannuwu.inline2.settings.data.SettingsState
import java.util.EventListener

interface SettingsChangeListener : EventListener {

    fun onSettingsChange(newSettingsState: SettingsState)
}
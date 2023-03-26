package com.ioannuwu.inline.domain.settings

import com.ioannuwu.inline.data.SettingsState

interface SettingsChangeEvent {

    val newSettingsState: SettingsState
}
package com.ioannuwu.inline.domain.settings

import com.ioannuwu.inline.data.SettingsState
import com.ioannuwu.inline.domain.settings.SettingsChangeEvent

class SettingsChangeEventImpl(
    override val newSettingsState: SettingsState,
) : SettingsChangeEvent
package com.ioannuwu.inline2.settings.ui

import com.ioannuwu.inline2.settings.data.MutableState
import com.ioannuwu.inline2.settings.data.SettingsState
import javax.swing.JComponent

interface SettingsUIView : MutableState<SettingsState> {

    fun createComponent(): JComponent
}
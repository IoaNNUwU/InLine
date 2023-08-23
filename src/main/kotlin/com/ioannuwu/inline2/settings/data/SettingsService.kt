package com.ioannuwu.inline2.settings.data

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import com.ioannuwu.inline2.settings.event.SettingsChangeDispatcher

@State(
    name = "com.ioannuwu.inline.data.SettingsState",
    storages = [Storage("com.ioannuwu.inline.Settings.xml")]
)
class SettingsService : PersistentStateComponent<SettingsState>{

    private var settingsState: SettingsState = SettingsState()

    override fun getState(): SettingsState = settingsState

    override fun loadState(state: SettingsState) {
        XmlSerializerUtil.copyBean(state, settingsState)
    }

    override fun initializeComponent() {
        SettingsChangeDispatcher.notify { settingsState }
    }
}
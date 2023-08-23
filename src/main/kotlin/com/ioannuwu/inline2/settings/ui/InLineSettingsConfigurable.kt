package com.ioannuwu.inline2.settings.ui

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.options.Configurable
import com.ioannuwu.inline2.settings.data.SettingsService
import com.ioannuwu.inline2.settings.event.SettingsChangeDispatcher
import javax.swing.JComponent

class InLineSettingsConfigurable : Configurable {

    private val settingsService: SettingsService =
        ApplicationManager.getApplication().getService(SettingsService::class.java)

    override fun getDisplayName(): String = "InLine Settings"

    private val settingsUIView: SettingsUIView = SettingsUIViewImpl(settingsService.state)

    override fun createComponent(): JComponent = settingsUIView.createComponent()

    override fun isModified(): Boolean = settingsService.state != settingsUIView.state

    override fun apply() {
        val newState = settingsUIView.state

        settingsService.loadState(newState)
        SettingsChangeDispatcher.notify { newState }
    }
}
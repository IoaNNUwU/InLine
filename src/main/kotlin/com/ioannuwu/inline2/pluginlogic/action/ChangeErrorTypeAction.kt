package com.ioannuwu.inline2.pluginlogic.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.ioannuwu.inline.data.TextStyle
import com.ioannuwu.inline2.settings.data.SettingsService
import com.ioannuwu.inline2.settings.event.SettingsChangeDispatcher

class ChangeErrorTypeAction : AnAction() {

    private val settingsService: SettingsService =
        ApplicationManager.getApplication().getService(SettingsService::class.java)

    override fun actionPerformed(e: AnActionEvent) {
        SettingsChangeDispatcher.notify { oldSettingsState ->
            val oldTextStyle = oldSettingsState.textStyle

            val copy = oldSettingsState.copy()

            val newTextStyle = when (oldTextStyle) {
                TextStyle.AFTER_LINE -> TextStyle.UNDER_LINE
                TextStyle.UNDER_LINE -> TextStyle.AFTER_LINE
            }
            copy.textStyle = newTextStyle

            settingsService.loadState(copy)
            copy
        }
    }
}
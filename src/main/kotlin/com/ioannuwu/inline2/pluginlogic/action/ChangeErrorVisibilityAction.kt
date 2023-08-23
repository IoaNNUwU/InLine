package com.ioannuwu.inline2.pluginlogic.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.ioannuwu.inline2.settings.data.SettingsService
import com.ioannuwu.inline2.settings.event.SettingsChangeDispatcher

class ChangeErrorVisibilityAction : AnAction() {

    private val settingsService: SettingsService =
        ApplicationManager.getApplication().getService(SettingsService::class.java)

    override fun actionPerformed(e: AnActionEvent) {
        SettingsChangeDispatcher.notify { oldSettingsState ->

            val newSettingsState = oldSettingsState.copy()

            val isShowText = !oldSettingsState.error.showText

            newSettingsState.error.showText = isShowText
            newSettingsState.warning.showText = isShowText
            newSettingsState.weakWarning.showText = isShowText
            newSettingsState.information.showText = isShowText
            newSettingsState.serverError.showText = isShowText
            newSettingsState.otherError.showText = isShowText

            settingsService.loadState(newSettingsState)

            newSettingsState
        }
    }
}
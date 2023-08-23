package com.ioannuwu.inline2.settings.event

import com.ioannuwu.inline2.pluginlogic.FileOpenedListener
import com.ioannuwu.inline2.settings.data.SettingsState

object SettingsChangeDispatcher {

    private val firstQueue: MutableList<SettingsChangeListener> = mutableListOf()
    private val defaultQueue: MutableList<SettingsChangeListener> = mutableListOf()
    private val lastQueue: MutableList<SettingsChangeListener> = mutableListOf()

    fun subscribe(listener: SettingsChangeListener, priority: SettingsChangePriority) {

        when (priority) {
            SettingsChangePriority.FIRST -> firstQueue.add(listener)
            SettingsChangePriority.DEFAULT -> defaultQueue.add(listener)
            SettingsChangePriority.LAST -> lastQueue.add(listener)
        }
    }

    fun subscribe(listener: SettingsChangeListener) = subscribe(listener, SettingsChangePriority.DEFAULT)

    fun unsubscribe(listener: SettingsChangeListener) {
        firstQueue.remove(listener)
        defaultQueue.remove(listener)
        lastQueue.remove(listener)
    }

    private var oldSettingsState: SettingsState = SettingsState()

    fun notify(newStateFromOld: (SettingsState) -> SettingsState) {

        val newState = newStateFromOld(oldSettingsState)

        firstQueue.forEach { it.onSettingsChange(newState) }
        defaultQueue.forEach { it.onSettingsChange(newState) }
        lastQueue.forEach { it.onSettingsChange(newState) }

        FileOpenedListener.updateAllListeners()

        oldSettingsState = newState
    }
}
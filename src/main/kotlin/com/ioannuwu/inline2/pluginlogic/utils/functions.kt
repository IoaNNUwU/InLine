package com.ioannuwu.inline2.pluginlogic.utils

import com.intellij.openapi.application.ApplicationManager

/**
 * Shortcut to run action on EDT
 */
inline fun runOnEDT(runnable: Runnable) =
    ApplicationManager.getApplication().invokeLater(runnable)

/**
 * Shortcut to run action on EDT ignoring IndexOutOfBoundsException
 */
inline fun runOnEDTIgnoringIndexOutOfBounds(runnable: Runnable) =
    ApplicationManager.getApplication().invokeLater {
        try {
            runnable.run()
        } catch (_: IndexOutOfBoundsException) {

        }
    }


package com.ioannuwu.inline2.pluginlogic.utils

import com.intellij.openapi.application.ApplicationManager

/**
 * Shortcut to run action on EDT
 */
inline fun runOnEDT(runnable: Runnable) =
    ApplicationManager.getApplication().invokeLater(runnable)


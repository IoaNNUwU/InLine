package com.ioannuwu.inline2.pluginlogic.utils

import com.intellij.openapi.Disposable

/**
 * Wraps any object in Disposable using provided dispose method
 *
 * Used for types that can be disposed but don't implement Disposable
 */
inline fun <T> T.makeDisposable(crossinline disposeMethod: (T) -> Unit): Disposable =
    Disposable {
        disposeMethod(this)
    }
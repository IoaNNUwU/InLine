package com.ioannuwu.inline.domain.elements

import com.intellij.openapi.editor.markup.GutterIconRenderer
import javax.swing.Icon

class MyGutterRenderer(private val icon: Icon) : GutterIconRenderer() {

    override fun getIcon(): Icon = icon

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GutterIconRenderer) return false
        return other.icon.hashCode() == hashCode()
    }

    override fun hashCode(): Int = icon.hashCode()
}
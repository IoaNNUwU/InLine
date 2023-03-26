package com.ioannuwu.inline.data

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.Disposer
import com.intellij.util.ui.UIUtilities
import com.ioannuwu.inline.domain.settings.SettingsChangeEvent
import com.ioannuwu.inline.domain.settings.SettingsChangeListener
import com.ioannuwu.inline.domain.settings.SettingsChangeObservable
import java.awt.Font
import java.awt.FontMetrics
import java.awt.GraphicsEnvironment

interface FontDataProvider {

    val font: Font
    val fontMetrics: FontMetrics
    val lineHeight: Int

    class BySettings(
        private val editor: Editor,
        graphicsEnvironment: GraphicsEnvironment,

        parentDisposable: Disposable,
    ) : FontDataProvider, Disposable, SettingsChangeListener {

        private val allFonts: Array<Font> = graphicsEnvironment.allFonts

        init {
            MySettingsService.OBSERVABLE.subscribe(this, SettingsChangeObservable.Priority.DEFAULT)
            Disposer.register(parentDisposable, this)
        }

        private var underivedFont: Font = calculateUnderivedFont(
            ApplicationManager.getApplication().getService(MySettingsService::class.java).state.font.fontName
        )

        override val font: Font
            get() = underivedFont.deriveFont(editor.colorsScheme.editorFontSize.toFloat())


        private fun calculateUnderivedFont(settingsFontName: String): Font {

            for (font in allFonts)
                if (font.fontName == settingsFontName)
                    return font

            for (font in allFonts)
                if (font.fontName == editor.colorsScheme.editorFontName)
                    return font

            return allFonts[0]
        }

        override val fontMetrics: FontMetrics
            get() = UIUtilities.getFontMetrics(editor.component, font)

        override val lineHeight: Int
            get() = editor.lineHeight

        override fun onSettingsChange(event: SettingsChangeEvent) {
            val settingsFontName = event.newSettingsState.font.fontName!!

            underivedFont = calculateUnderivedFont(settingsFontName)
        }

        override fun dispose() {
            MySettingsService.OBSERVABLE.unsubscribe(this)
        }
    }
}
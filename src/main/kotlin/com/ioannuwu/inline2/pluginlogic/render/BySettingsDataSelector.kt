package com.ioannuwu.inline2.pluginlogic.render

import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.icons.AllIcons
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.ioannuwu.inline.data.DefaultSettings
import com.ioannuwu.inline.data.EffectType
import com.ioannuwu.inline.data.SeverityLevelState
import com.ioannuwu.inline.data.TextStyle
import com.ioannuwu.inline2.pluginlogic.render.metrics.OtherData
import com.ioannuwu.inline2.pluginlogic.render.metrics.RenderData
import com.ioannuwu.inline2.settings.data.SettingsState
import com.ioannuwu.inline2.settings.event.SettingsChangeListener

import java.awt.Color
import java.awt.Font
import java.awt.GraphicsEnvironment
import javax.swing.Icon

class BySettingsDataSelector(
    initialState: SettingsState
): RenderDataSelector, SettingsChangeListener, OtherDataSelector {

    init {
        onSettingsChange(initialState)
    }

    private lateinit var currentFont: Font
    private lateinit var currentSettingsState: SettingsState

    override operator fun invoke(highlighter: RangeHighlighter): RenderData? {

        val info: HighlightInfo = highlighter.errorStripeTooltip as? HighlightInfo ?: return null
        if (info.description.isNullOrBlank()) return null

        val description: String = info.description

        val state = currentSettingsState

        if (state.ignoreList.any { description.contains(it) }) return null

        val severity: SeverityLevelState = when (info.severity) {
            HighlightSeverity.ERROR -> state.error
            HighlightSeverity.WARNING -> state.warning
            HighlightSeverity.WEAK_WARNING -> state.weakWarning
            HighlightSeverity.INFORMATION -> state.information
            HighlightSeverity.GENERIC_SERVER_ERROR_OR_WARNING -> state.serverError
            else -> state.otherError
        }

        val textColor: Color? = if (severity.showText) severity.textColor else null
        val backgroundColor: Color? = if (severity.showBackground) severity.backgroundColor else null
        val effectColor: Color? = if (severity.showEffect) severity.effectColor else null

        val tmpIcon: Icon = when (info.severity) {
            HighlightSeverity.ERROR -> DefaultSettings.Icons.ERROR
            HighlightSeverity.WARNING -> DefaultSettings.Icons.WARNING
            HighlightSeverity.WEAK_WARNING -> DefaultSettings.Icons.WEAK_WARNING
            HighlightSeverity.INFORMATION -> DefaultSettings.Icons.INFORMATION
            HighlightSeverity.GENERIC_SERVER_ERROR_OR_WARNING -> DefaultSettings.Icons.SERVER_ERROR
            else -> DefaultSettings.Icons.OTHER_ERROR
        }

        val gutterIcon: Icon? = if (severity.showGutterIcon) tmpIcon else null

        return object : RenderData {
            override fun textColor(): Color? = textColor
            override fun backgroundColor(): Color? = backgroundColor
            override fun effectColor(): Color? = effectColor
            override fun text(): String = description
            override fun font(): Font = currentFont
            override fun gutterIcon(): Icon? = gutterIcon
        }
    }

    override fun selectOtherData(highlighter: RangeHighlighter): OtherData {

        val state = currentSettingsState

        return object : OtherData {
            override fun effectType(): EffectType = state.effectType
            override fun maxErrorsPerLine(): Int = state.maxErrorsPerLine
            override fun numberOfWhitespaces(): Int = state.numberOfWhitespaces
            override fun showOnlyOneGutter(): Boolean = state.oneGutterMode
            override fun textStyle(): TextStyle = state.textStyle
        }
    }

    override fun onSettingsChange(newSettingsState: SettingsState) {

        currentSettingsState = newSettingsState

        val allFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().allFonts

        val font: Font = allFonts.asSequence()
            .find { it.name == newSettingsState.font.fontName }
            ?: allFonts.find { it.name.contains("JetBrains", ignoreCase = false) }
            ?: allFonts.find { it.name.contains("Mono", ignoreCase = true) }
            ?: allFonts.find { it.name.contains("plain", ignoreCase = true) }
            ?: allFonts[0]

        currentFont = font
    }
}
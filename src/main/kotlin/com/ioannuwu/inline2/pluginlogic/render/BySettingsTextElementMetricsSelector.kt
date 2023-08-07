package com.ioannuwu.inline2.pluginlogic.render

import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.icons.AllIcons
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.ioannuwu.inline.data.SeverityLevelState
import com.ioannuwu.inline2.pluginlogic.render.metrics.RenderData
import com.ioannuwu.inline2.settings.data.SettingsState
import com.ioannuwu.inline2.settings.event.SettingsChangeListener
import java.awt.Color
import java.awt.Font
import java.awt.GraphicsEnvironment
import javax.swing.Icon

class BySettingsTextElementMetricsSelector(
    initialState: SettingsState
): RenderDataSelector, SettingsChangeListener {

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
            HighlightSeverity.ERROR -> AllIcons.General.Error
            HighlightSeverity.WARNING -> AllIcons.General.Warning
            HighlightSeverity.WEAK_WARNING -> AllIcons.General.Warning
            HighlightSeverity.INFORMATION -> AllIcons.General.Information
            HighlightSeverity.GENERIC_SERVER_ERROR_OR_WARNING -> AllIcons.General.ArrowDown
            else -> AllIcons.General.Add
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

    override fun onSettingsChange(newSettingsState: SettingsState) {

        currentSettingsState = newSettingsState

        val allFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().allFonts

        val font: Font = allFonts.asSequence()
            .find { it.name == newSettingsState.font.fontName }
            ?: allFonts.find { it.name.contains("dialog", ignoreCase = true) }
            ?: allFonts.find { it.name.contains("JetBrains", ignoreCase = false) }
            ?: allFonts.find { it.name.contains("Mono", ignoreCase = true) }
            ?: allFonts[0]

        currentFont = font
    }
}
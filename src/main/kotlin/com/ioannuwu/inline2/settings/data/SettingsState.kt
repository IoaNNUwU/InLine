package com.ioannuwu.inline2.settings.data

import com.ioannuwu.inline.data.*
import java.util.*

class SettingsState {

    var numberOfWhitespaces: Int = DefaultSettings.NUMBER_OF_WHITESPACES

    var effectType: EffectType = DefaultSettings.EFFECT_TYPE

    var maxErrorsPerLine: Int = DefaultSettings.MAX_ERRORS_PER_LINE

    var font: FontSettingsState = DefaultSettings.FONT

    var textStyle: TextStyle = DefaultSettings.TEXT_STYLE
    var oneGutterMode: Boolean = DefaultSettings.ONE_GUTTER_MODE

    var error: SeverityLevelState = DefaultSettings.ERROR
    var warning: SeverityLevelState = DefaultSettings.WARNING
    var weakWarning: SeverityLevelState = DefaultSettings.WEAK_WARNING
    var information: SeverityLevelState = DefaultSettings.INFORMATION
    var serverError: SeverityLevelState = DefaultSettings.SERVER_ERROR
    var otherError: SeverityLevelState = DefaultSettings.OTHER_ERROR

    var ignoreList: Array<String> = DefaultSettings.IGNORE_LIST

    fun copy(): SettingsState {
        val state = SettingsState()
        state.numberOfWhitespaces = numberOfWhitespaces
        state.effectType = effectType
        state.maxErrorsPerLine = maxErrorsPerLine
        state.font = font
        state.textStyle = textStyle
        state.oneGutterMode = oneGutterMode
        state.error = error
        state.warning = warning
        state.weakWarning = weakWarning
        state.information = information
        state.serverError = serverError
        state.otherError = otherError
        state.ignoreList = ignoreList
        return state
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as SettingsState
        return numberOfWhitespaces == that.numberOfWhitespaces && maxErrorsPerLine == that.maxErrorsPerLine
                && error == that.error && warning == that.warning && weakWarning == that.weakWarning
                && information == that.information && serverError == that.serverError
                && otherError == that.otherError
                && ignoreList.contentEquals(that.ignoreList)
                && effectType == that.effectType && font == that.font && textStyle == that.textStyle
                && oneGutterMode == that.oneGutterMode
    }

    override fun hashCode(): Int {
        var result = Objects.hash(
            numberOfWhitespaces, maxErrorsPerLine, error, warning, weakWarning,
            information, serverError, otherError, effectType, font, textStyle, oneGutterMode
        )
        result = 31 * result + ignoreList.contentHashCode()
        return result
    }

    override fun toString(): String = """
        SettingsState{
        numberOfWhitespaces=$numberOfWhitespaces
        effectType=$effectType
        maxErrorsPerLine=$maxErrorsPerLine
        font=$font
        textStyle=$textStyle
        oneGutterMode=$oneGutterMode
        error=$error
        warning=$warning
        weakWarning=$weakWarning
        information=$information
        serverError=$serverError
        otherError=$otherError
        ignoreList=${ignoreList.contentToString()}}
        """.trimIndent()
}
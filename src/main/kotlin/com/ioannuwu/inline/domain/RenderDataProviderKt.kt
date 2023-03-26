package com.ioannuwu.inline.domain

import com.intellij.lang.annotation.HighlightSeverity
import com.ioannuwu.inline.data.DefaultSettings
import com.ioannuwu.inline.data.MySettingsService
import com.ioannuwu.inline.data.SettingsState
import com.ioannuwu.inline.domain.settings.SettingsChangeEvent
import com.ioannuwu.inline.domain.settings.SettingsChangeListener
import com.ioannuwu.inline.domain.settings.SettingsChangeObservable
import com.ioannuwu.inline.domain.wrapper.RangeHighlighterWrapper
import java.awt.Color

interface RenderDataProviderKt {

    fun provide(highlighter: RangeHighlighterWrapper): RenderData?

    fun isValid(highlighter: RangeHighlighterWrapper): Boolean


    object BySettings : RenderDataProviderKt, SettingsChangeListener {

        init {
            MySettingsService.OBSERVABLE.subscribe(this, SettingsChangeObservable.Priority.DEFAULT)
        }

        private var state: SettingsState = SettingsState.NONE

        /**
         * @return null if RenderDataProvider.isValid(highlighter) == false
         */
        override fun provide(highlighter: RangeHighlighterWrapper): RenderData? {

            if (!highlighter.isSufficient()) return null

            val (levelState, icon) = when (highlighter.priority) {
                in ERROR..Int.MAX_VALUE -> Pair(state.error, DefaultSettings.Icons.ERROR)
                in WARNING..ERROR -> Pair(state.warning, DefaultSettings.Icons.WARNING)
                in WEAK_WARNING..WARNING -> Pair(state.weakWarning, DefaultSettings.Icons.WEAK_WARNING)
                in SERVER_ERROR..WEAK_WARNING -> Pair(state.weakWarning, DefaultSettings.Icons.SERVER_ERROR)
                in INFORMATION..SERVER_ERROR -> Pair(state.serverError, DefaultSettings.Icons.INFORMATION)
                else -> Pair(state.otherError, DefaultSettings.Icons.OTHER_ERROR)
            }
            val bc = levelState.backgroundColor
            val backGroundColor = Color(bc.red, bc.green, bc.blue, 60)

            return RenderData(
                levelState.showGutterIcon, levelState.showText, levelState.showBackground, levelState.showEffect,
                levelState.textColor, backGroundColor, levelState.effectColor,
                state.numberOfWhitespaces, state.maxErrorsPerLine, state.effectType,
                highlighter.description, icon
            )
        }

        override fun isValid(highlighter: RangeHighlighterWrapper): Boolean {

            var descriptionIsInIgnoreList = false
            for (str in state.ignoreList)
                if (highlighter.description.contains(str)) descriptionIsInIgnoreList = true

            return highlighter.isSufficient() && !descriptionIsInIgnoreList
        }

        override fun onSettingsChange(event: SettingsChangeEvent) {
            state = event.newSettingsState
        }
    }
}

private val ERROR = HighlightSeverity.ERROR.myVal
private val WARNING = HighlightSeverity.WARNING.myVal
private val WEAK_WARNING = HighlightSeverity.ERROR.myVal
private val SERVER_ERROR = HighlightSeverity.ERROR.myVal
private val INFORMATION = HighlightSeverity.ERROR.myVal

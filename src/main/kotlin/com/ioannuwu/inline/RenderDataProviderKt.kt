package com.ioannuwu.inline

import com.intellij.lang.annotation.HighlightSeverity
import com.ioannuwu.inline.data.DefaultSettings
import com.ioannuwu.inline.data.MySettingsService
import com.ioannuwu.inline.domain.render.RenderData
import com.ioannuwu.inline.wrapper.RangeHighlighterWrapper
import java.awt.Color

interface RenderDataProviderKt {

    fun provide(highlighter: RangeHighlighterWrapper): RenderData?

    fun isValid(highlighter: RangeHighlighterWrapper): Boolean


    class BySettings(private val settingsService: MySettingsService) : RenderDataProviderKt {

        /**
         * @return null if RenderDataProvider.isValid(highlighter) == false
         */
        override fun provide(highlighter: RangeHighlighterWrapper): RenderData? {

            if (!highlighter.isSufficient()) return null

            val state = settingsService.state

            val (levelState, icon) = when(highlighter.priority) {
                in ERROR..Int.MAX_VALUE -> Pair(state.error, DefaultSettings.Icons.ERROR)
                in WARNING..ERROR -> Pair(state.warning, DefaultSettings.Icons.WARNING)
                in WEAK_WARNING..WARNING -> Pair(state.weakWarning, DefaultSettings.Icons.WEAK_WARNING)
                in SERVER_ERROR..WEAK_WARNING -> Pair(state.weakWarning, DefaultSettings.Icons.SERVER_ERROR)
                in INFORMATION..SERVER_ERROR -> Pair(state.serverError, DefaultSettings.Icons.INFORMATION)
                else -> Pair(state.otherError, DefaultSettings.Icons.OTHER_ERROR)
            }
            val bc = levelState.backgroundColor
            val backGroundColor = Color(bc.red,bc.green, bc.blue, 60)

            return RenderData(
                levelState.showGutterIcon, levelState.showText, levelState.showBackground, levelState.showEffect,
                levelState.textColor, backGroundColor, levelState.effectColor,
                state.numberOfWhitespaces, state.maxErrorsPerLine, state.effectType,
                highlighter.description, icon)
        }

        override fun isValid(highlighter: RangeHighlighterWrapper): Boolean {

            var descriptionIsInIgnoreList = false
            for (str in settingsService.state.ignoreList)
                if (highlighter.description.contains(str)) descriptionIsInIgnoreList = true

            return highlighter.isSufficient() && !descriptionIsInIgnoreList
        }
    }
}

private val ERROR = HighlightSeverity.ERROR.myVal
private val WARNING = HighlightSeverity.WARNING.myVal
private val WEAK_WARNING = HighlightSeverity.ERROR.myVal
private val SERVER_ERROR = HighlightSeverity.ERROR.myVal
private val INFORMATION = HighlightSeverity.ERROR.myVal

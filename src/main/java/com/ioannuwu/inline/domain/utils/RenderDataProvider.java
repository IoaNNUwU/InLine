package com.ioannuwu.inline.domain.utils;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.ioannuwu.inline.data.DefaultSettings;
import com.ioannuwu.inline.data.MySettingsService;
import com.ioannuwu.inline.data.SettingsState;
import com.ioannuwu.inline.data.SeverityLevelState;
import com.ioannuwu.inline.domain.render.RenderData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public interface RenderDataProvider {

    @Nullable RenderData provide(@NotNull RangeHighlighter highlighter);


    class BySettings implements RenderDataProvider {

        private final MySettingsService settingsService;

        public BySettings(MySettingsService settingsService) {
            this.settingsService = settingsService;
        }

        // null if filtered out by logic or ignore list
        @Override
        public @Nullable RenderData provide(@NotNull RangeHighlighter highlighter) {
            // Filter by logic
            if (highlighter.getErrorStripeTooltip() == null) return null;
            if (!(highlighter.getErrorStripeTooltip() instanceof HighlightInfo)) return null;
            HighlightInfo info = (HighlightInfo) highlighter.getErrorStripeTooltip();
            if (info.getDescription() == null) return null;

            SettingsState settingsState = settingsService.getState();

            // Filter by ignore list
            String[] ignoreList = settingsState.ignoreList;
            for (String str : ignoreList) {
                if (info.getDescription().contains(str)) return null;
            }

            // new RenderData by settings state
            SeverityLevelState levelState;
            Icon icon;

            HighlightSeverity severity = info.getSeverity();

            if (severity.myVal >= HighlightSeverity.ERROR.myVal) {
                levelState = settingsState.error;
                icon = DefaultSettings.Icons.ERROR;
            }
            else if (severity.myVal >= HighlightSeverity.WARNING.myVal) {
                levelState = settingsState.warning;
                icon = DefaultSettings.Icons.WARNING;
            }
            else if (severity.myVal >= HighlightSeverity.WEAK_WARNING.myVal) {
                levelState = settingsState.weakWarning;
                icon = DefaultSettings.Icons.WEAK_WARNING;
            }
            else if (severity.myVal >= HighlightSeverity.GENERIC_SERVER_ERROR_OR_WARNING.myVal) {
                levelState = settingsState.serverError;
                icon = DefaultSettings.Icons.SERVER_ERROR;
            }
            else if (severity.myVal >= HighlightSeverity.INFORMATION.myVal) {
                levelState = settingsState.information;
                icon = DefaultSettings.Icons.INFORMATION;
            }
            else {
                levelState = settingsState.otherError;
                icon = DefaultSettings.Icons.OTHER_ERROR;
            }
            Color backgroundColor = levelState.backgroundColor;
            Color newBackgroundColor = new Color(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue(), 60);

            return new RenderData(levelState.showGutterIcon, levelState.showText, levelState.showBackground,
                    levelState.showEffect, levelState.textColor, newBackgroundColor, levelState.effectColor,
                    settingsState.numberOfWhitespaces, settingsState.maxErrorsPerLine, settingsState.effectType,
                    info.getDescription(), icon);
        }
    }

}

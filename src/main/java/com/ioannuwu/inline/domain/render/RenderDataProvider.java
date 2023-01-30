package com.ioannuwu.inline.domain.render;

import com.intellij.lang.annotation.HighlightSeverity;
import com.ioannuwu.inline.data.DefaultSettings;
import com.ioannuwu.inline.data.MySettingsService;
import com.ioannuwu.inline.data.SettingsState;
import com.ioannuwu.inline.data.SeverityLevelState;
import com.ioannuwu.inline.domain.Filter;
import com.ioannuwu.inline.domain.RangeHighlighterWrapper;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public interface RenderDataProvider {

    @NotNull RenderData provide(@NotNull RangeHighlighterWrapper rangeHighlighter);

    int getNumberOfWhitespaces();

    int getBorder();

    class BySettings implements RenderDataProvider {

        private final MySettingsService mySettingsService;
        private final Filter filter;

        public BySettings(MySettingsService mySettingsService) {
            this.mySettingsService = mySettingsService;
            filter = new Filter.ByIgnoreList(mySettingsService.getState().ignoreList);
        }

        @Override
        public @NotNull RenderData provide(@NotNull RangeHighlighterWrapper rangeHighlighter) {

            if (!filter.accept(rangeHighlighter)) return RenderData.EMPTY;

            final SettingsState settingsState = mySettingsService.getState();
            SeverityLevelState levelState = null;
            Icon icon = null;
            HighlightSeverity severity = rangeHighlighter.getSeverity();
            if (severity == HighlightSeverity.ERROR) {
                levelState = settingsState.error;
                icon = DefaultSettings.Icons.ERROR;
            }
            if (severity == HighlightSeverity.WARNING) {
                levelState = settingsState.warning;
                icon = DefaultSettings.Icons.WARNING;
            }
            if (severity == HighlightSeverity.WEAK_WARNING) {
                levelState = settingsState.error;
                icon = DefaultSettings.Icons.WEAK_WARNING;
            }
            if (severity == HighlightSeverity.INFORMATION) {
                levelState = settingsState.warning;
                icon = DefaultSettings.Icons.INFORMATION;
            }
            if (severity == HighlightSeverity.GENERIC_SERVER_ERROR_OR_WARNING) {
                levelState = settingsState.error;
                icon = DefaultSettings.Icons.SERVER_ERROR;
            }
            if (levelState == null) {
                levelState = settingsState.otherError;
                icon = DefaultSettings.Icons.OTHER_ERROR;
            }
            Color textColor = (levelState.showText) ? levelState.textColor : null;
            Color backgroundColor = (levelState.showBackground) ? levelState.backgroundColor : null;
            Color effectColor = (levelState.showEffect) ? levelState.effectColor : null;

            return new RenderData(rangeHighlighter.getDescription(),
                    textColor, effectColor, backgroundColor, icon);
        }

        @Override
        public int getNumberOfWhitespaces() {
            return mySettingsService.getState().numberOfWhitespaces;
        }

        @Override
        public int getBorder() {
            return mySettingsService.getState().border;
        }
    }
}

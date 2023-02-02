package com.ioannuwu.inline.domain.render;

import com.intellij.lang.annotation.HighlightSeverity;
import com.ioannuwu.inline.data.*;
import com.ioannuwu.inline.domain.utils.Filter;
import com.ioannuwu.inline.domain.RangeHighlighterWrapper;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class BySettingsRenderDataProvider implements RenderDataProvider, SetSettingsSource {

    private MySettingsService settingsSource;
    private Filter filter;

    public BySettingsRenderDataProvider(MySettingsService settingsSource) {
        this.settingsSource = settingsSource;
        this.filter = new Filter.ByIgnoreList(settingsSource.getState().ignoreList);
    }

    @Override
    public @NotNull RenderData provide(@NotNull RangeHighlighterWrapper rangeHighlighter) {

        if (!filter.accept(rangeHighlighter)) return RenderData.EMPTY;

        final SettingsState settingsState = settingsSource.getState();
        SeverityLevelState levelState;
        Icon icon;
        HighlightSeverity severity = rangeHighlighter.getSeverity();

        if (severity.myVal >= HighlightSeverity.ERROR.myVal) {
            levelState = settingsState.error;
            icon = DefaultSettings.Icons.ERROR;
        }
        else if (severity.myVal >= HighlightSeverity.WARNING.myVal) {
            levelState = settingsState.warning;
            icon = DefaultSettings.Icons.WARNING;
        }
        else if (severity.myVal >= HighlightSeverity.WEAK_WARNING.myVal) {
            levelState = settingsState.error;
            icon = DefaultSettings.Icons.WEAK_WARNING;
        }
        else if (severity.myVal >= HighlightSeverity.GENERIC_SERVER_ERROR_OR_WARNING.myVal) {
            levelState = settingsState.error;
            icon = DefaultSettings.Icons.SERVER_ERROR;
        }
        else if (severity.myVal >= HighlightSeverity.INFORMATION.myVal) {
            levelState = settingsState.warning;
            icon = DefaultSettings.Icons.INFORMATION;
        }
        else {
            levelState = settingsState.otherError;
            icon = DefaultSettings.Icons.OTHER_ERROR;
        }

        return new RenderData(levelState.showGutterIcon, levelState.showText, levelState.showBackground,
                levelState.showEffect, levelState.textColor, levelState.backgroundColor, levelState.effectColor,
                settingsState.numberOfWhitespaces, settingsState.effectType, rangeHighlighter.getDescription(), icon);
    }

    @Override
    public void setSettingsSource(MySettingsService settingsSource) {
        this.settingsSource = settingsSource;
        this.filter = new Filter.ByIgnoreList(settingsSource.getState().ignoreList);
    }
}

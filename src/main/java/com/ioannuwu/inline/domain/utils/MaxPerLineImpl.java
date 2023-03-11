package com.ioannuwu.inline.domain.utils;

import com.ioannuwu.inline.data.MySettingsService;

public class MaxPerLineImpl implements MaxPerLine {

    private final MySettingsService settingsService;

    public MaxPerLineImpl(MySettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @Override
    public int maxPerLine() {
        return settingsService.getState().maxErrorsPerLine;
    }
}

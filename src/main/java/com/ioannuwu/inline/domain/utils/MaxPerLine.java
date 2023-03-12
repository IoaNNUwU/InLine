package com.ioannuwu.inline.domain.utils;

import com.ioannuwu.inline.data.MySettingsService;

public interface MaxPerLine {

    int maxPerLine();


    class BySettings implements MaxPerLine {

        private final MySettingsService settingsService;

        public BySettings(MySettingsService settingsService) {
            this.settingsService = settingsService;
        }

        @Override
        public int maxPerLine() {
            return settingsService.getState().maxErrorsPerLine;
        }
    }

}

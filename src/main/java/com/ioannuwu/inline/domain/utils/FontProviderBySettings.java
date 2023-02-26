package com.ioannuwu.inline.domain.utils;

import com.ioannuwu.inline.data.MySettingsService;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class FontProviderBySettings implements FontProvider {

    private final MySettingsService settingsService;

    private final Font[] allFonts;

    public FontProviderBySettings(MySettingsService settingsService, Font[] allFonts) {
        this.allFonts = allFonts;
        this.settingsService = settingsService;
    }

    @Override
    public @Nullable Font provide() {
        String fontName = settingsService.getState().font.fontName;
        for (final var font : allFonts) {
            if (font.getFontName().equals(fontName)) return font;
        }
        return null;
    }
}

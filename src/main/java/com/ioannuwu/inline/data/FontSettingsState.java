package com.ioannuwu.inline.data;

import java.util.Objects;

public class FontSettingsState {

    public FontSettingsState() {}

    public FontSettingsState(String fontName, String textSample) {
        this.fontName = fontName;
        this.textSample = textSample;
    }

    public String fontName;
    public String textSample;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FontSettingsState that = (FontSettingsState) o;
        return Objects.equals(fontName, that.fontName) && Objects.equals(textSample, that.textSample);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fontName, textSample);
    }

}

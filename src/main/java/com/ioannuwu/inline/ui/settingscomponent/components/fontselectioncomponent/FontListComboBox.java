package com.ioannuwu.inline.ui.settingscomponent.components.fontselectioncomponent;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.util.Pair;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class FontListComboBox extends ComboBox<String> {

    private final Font[] allAvailableFonts;
    private Font[] currentFonts;

    private String textSample;

    public FontListComboBox(Font[] fonts) {
        super(Arrays.stream(fonts)
                .map(Font::getFontName)
                .toArray(String[]::new));
        allAvailableFonts = fonts;
        currentFonts = allAvailableFonts;
    }

    void retainOnlySupporting(String fontSample) {
        textSample = fontSample;
        currentFonts = Arrays.stream(allAvailableFonts)
                .filter(filterBySample(fontSample))
                .filter(distinctBy(Font::getFontName))
                .toArray(Font[]::new);

        this.removeAllItems();
        Arrays.stream(currentFonts).forEach(font -> this.addItem(font.getFontName()));
    }
    private static Predicate<Font> filterBySample(String fontSample) {
        return fontSample == null || fontSample.isBlank()
                ? font -> true
                : font -> font.canDisplayUpTo(fontSample) == -1;
    }
    private static <T> Predicate<T> distinctBy(Function<? super T, ?> keyExtractor) {
        HashMap<Object, Boolean> seen = new HashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }


    Pair<Font, String> getSelectedFontAndTextSample() {
        return new Pair<>(
                currentFonts[this.getSelectedIndex()],
                textSample);
    }

    public void setSelectedFont(String fontName) {
        int setIndex = 0;
        for (int i = 0; i < currentFonts.length; i++) {
            if (currentFonts[i].getFontName().equals(fontName)) setIndex = i;
        }
        this.setSelectedIndex(setIndex);
    }
}

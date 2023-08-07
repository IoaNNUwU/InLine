package com.ioannuwu.inline.ui.settingscomponent.components;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.util.ui.FormBuilder;
import com.ioannuwu.inline.data.TextStyle;
import com.ioannuwu.inline.ui.settingscomponent.Component;
import com.ioannuwu.inline2.settings.data.MutableState;

import javax.swing.*;
import java.util.ArrayList;

public class TextStyleSelectionComponent implements Component, MutableState<TextStyle> {

    private static final String[] values = getValues();

    private final ComboBox<String> stylesComboBox;

    public TextStyleSelectionComponent(TextStyle textStyle) {
        this.stylesComboBox = new ComboBox<>(values);

        for (int i = 0; i < values.length; i++)
            if (values[i].equals(textStyle.name())) this.stylesComboBox.setSelectedIndex(i);
    }

    @Override
    public void addToBuilder(FormBuilder formBuilder) {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Text style"));
        panel.add(stylesComboBox);
        formBuilder.addLabeledComponent(panel, new JLabel());

    }

    @Override
    public TextStyle getState() {
        int index = stylesComboBox.getSelectedIndex();
        return TextStyle.valueOf(values[index]);
    }

    private static String[] getValues() {
        ArrayList<String> styles = new ArrayList<>(2);
        for (TextStyle style : TextStyle.values()) {
            styles.add(style.name());
        }
        return styles.toArray(new String[0]);
    }
}

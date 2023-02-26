package com.ioannuwu.inline.ui.settingscomponent.components.fontselectioncomponent;

import com.intellij.ui.ContextHelpLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.ioannuwu.inline.data.FontSettingsState;
import com.ioannuwu.inline.ui.settingscomponent.Component;
import com.ioannuwu.inline.ui.settingscomponent.State;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class FontSelectionComponent implements Component, State<FontSettingsState> {

    private final FontListComboBox comboBoxElement;

    private final JBTextField textField;

    public FontSelectionComponent(@Nullable String textSample, String selectedFontName, Font[] allAvailableFonts) {

        textField = new JBTextField(textSample);

        comboBoxElement = new FontListComboBox(allAvailableFonts);
        comboBoxElement.retainOnlySupporting(textSample);

        comboBoxElement.setSelectedFont(selectedFontName);

        textField.addActionListener(listener -> comboBoxElement.retainOnlySupporting(listener.getActionCommand()));
    }

    @Override
    public void addToBuilder(FormBuilder formBuilder) {
        formBuilder.addComponent(ContextHelpLabel.create("INPUT CHARACTER FROM YOUR LANGUAGE AND PRESS ENTER TO UPDATE LIST OF FONTS SUPPORTING THAT LANGUAGE"));
        formBuilder.addComponent(textField);
        formBuilder.addComponent(comboBoxElement);
    }

    @Override
    public FontSettingsState getState() {
        final var fontAndExample = comboBoxElement.getSelectedFontAndTextSample();
        return new FontSettingsState(
                fontAndExample.first.getFontName(),
                fontAndExample.second);
    }
}

package com.ioannuwu.inline.ui.settingscomponent.components.fontselectioncomponent;

import com.intellij.ui.ContextHelpLabel;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.ioannuwu.inline.data.FontSettingsState;
import com.ioannuwu.inline.ui.settingscomponent.Component;
import com.ioannuwu.inline.ui.settingscomponent.State;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
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
        JPanel panel = new JPanel();
        panel.add(new JBLabel("Hints font"));
        panel.add(textField);
        panel.add(ContextHelpLabel.create("<h2 id=\"press-enter-to-update-list-of-fonts-after-entering-characters\">PRESS ENTER TO UPDATE LIST OF FONTS AFTER ENTERING CHARACTERS</h2>\n" +
                "<h2 id=\"input-character-from-your-language-and-press-enter-to-update-list-of-fonts-that-support-this-character-language-\">Input character from your language and press <code>  Enter  </code> to update list of fonts that support this character (language)</h2>\n" +
                "<h3 id=\"note-default-editor-font-jetbrains-mono-cannot-display-chinese-and-possible-other-characters-for-some-reason-if-you-are-seeing-question-marks-instead-of-some-characters-change-font-\">Note: default editor font (JetBrains Mono) cannot display Chinese (and possible other) characters for some reason. If you are seeing question marks instead of some characters, change font.</h3>"));
        panel.add(new JBLabel("                                          "));
        panel.add(comboBoxElement);
        formBuilder.addLabeledComponent(panel, new JLabel());
    }

    @Override
    public FontSettingsState getState() {
        final var fontAndExample = comboBoxElement.getSelectedFontAndTextSample();
        return new FontSettingsState(
                fontAndExample.first.getFontName(),
                fontAndExample.second);
    }
}

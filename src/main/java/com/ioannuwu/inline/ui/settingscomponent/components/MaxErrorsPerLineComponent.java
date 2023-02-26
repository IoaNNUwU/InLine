package com.ioannuwu.inline.ui.settingscomponent.components;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.ioannuwu.inline.data.DefaultSettings;
import com.ioannuwu.inline.ui.settingscomponent.Component;
import com.ioannuwu.inline.ui.settingscomponent.State;

import javax.swing.*;

public class MaxErrorsPerLineComponent implements Component, State<Integer> {

    private final JBTextField maxErrorsPerLineField;

    public MaxErrorsPerLineComponent(int maxErrorsPerLine) {
        this.maxErrorsPerLineField = new JBTextField("" + maxErrorsPerLine);
    }

    @Override
    public void addToBuilder(FormBuilder formBuilder) {
        JPanel panel = new JPanel();
        panel.add(new JBLabel("Max number of errors per line                                   "));
        panel.add(maxErrorsPerLineField);
        formBuilder.addLabeledComponent(panel, new JBLabel());
    }

    @Override
    public Integer getState() {
        String text = maxErrorsPerLineField.getText();
        int num = DefaultSettings.MAX_ERRORS_PER_LINE;
        try {
            num = Integer.parseInt(text);
        } catch (NumberFormatException ignored) {
        }
        return num;
    }
}

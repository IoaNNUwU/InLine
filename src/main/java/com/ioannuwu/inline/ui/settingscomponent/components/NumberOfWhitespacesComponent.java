package com.ioannuwu.inline.ui.settingscomponent.components;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.ioannuwu.inline.ui.settingscomponent.Component;
import com.ioannuwu.inline2.settings.data.MutableState;

import javax.swing.*;

public class NumberOfWhitespacesComponent implements Component, MutableState<Integer> {

    private final JBTextField numberOfWhitespacesField;

    public NumberOfWhitespacesComponent(int numberOfWhitespaces) {
        this.numberOfWhitespacesField = new JBTextField(String.valueOf(numberOfWhitespaces));
    }

    @Override
    public void addToBuilder(FormBuilder formBuilder) {
        JPanel panel = new JPanel();
        panel.add(new JBLabel("Number of whitespaces after end of the line"));
        panel.add(numberOfWhitespacesField);
        formBuilder.addLabeledComponent(panel, new JBLabel());
    }

    @Override
    public Integer getState() {
        String text = numberOfWhitespacesField.getText();
        int num = 0;
        try {
            num = Integer.parseInt(text);
        } catch (NumberFormatException ignored) {
        }
        return num;
    }
}

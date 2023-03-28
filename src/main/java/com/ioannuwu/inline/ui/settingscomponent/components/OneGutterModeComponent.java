package com.ioannuwu.inline.ui.settingscomponent.components;

import com.intellij.ui.components.JBCheckBox;
import com.intellij.util.ui.FormBuilder;
import com.ioannuwu.inline.ui.settingscomponent.Component;
import com.ioannuwu.inline.ui.settingscomponent.State;

import javax.swing.*;

public class OneGutterModeComponent implements Component, State<Boolean> {

    private final JBCheckBox checkBox;

    public OneGutterModeComponent(boolean oneGutterMode) {
        checkBox = new JBCheckBox();
        checkBox.setSelected(oneGutterMode);
    }

    @Override
    public void addToBuilder(FormBuilder formBuilder) {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Gutter destruction-free mode                                                           "));
        panel.add(checkBox);
        formBuilder.addLabeledComponent(panel, new JLabel());
    }

    @Override
    public Boolean getState() {
        return checkBox.isSelected();
    }
}

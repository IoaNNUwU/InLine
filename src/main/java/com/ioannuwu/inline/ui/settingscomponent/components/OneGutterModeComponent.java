package com.ioannuwu.inline.ui.settingscomponent.components;

import com.intellij.ui.ContextHelpLabel;
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
        ContextHelpLabel contextHelpLabel = ContextHelpLabel.create("Show only one gutter icon per line");
        JPanel panel = new JPanel();
        panel.add(new JLabel("Gutter destruction-free mode"));
        panel.add(contextHelpLabel);
        panel.add(checkBox);
        formBuilder.addLabeledComponent(panel, new JLabel());
    }

    @Override
    public Boolean getState() {
        return checkBox.isSelected();
    }
}

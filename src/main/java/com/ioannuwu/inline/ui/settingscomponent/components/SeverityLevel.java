package com.ioannuwu.inline.ui.settingscomponent.components;

import com.intellij.ui.ColorPanel;
import com.intellij.ui.ContextHelpLabel;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.FormBuilder;
import com.ioannuwu.inline.data.SeverityLevelState;
import com.ioannuwu.inline.ui.settingscomponent.Component;
import com.ioannuwu.inline2.settings.data.MutableState;

import javax.swing.*;

public class SeverityLevel implements Component, MutableState<SeverityLevelState> {

    private static final String PREFIX = " >   ";

    private final ColorPanel textColorPanel;
    private final ColorPanel backgroundColorPanel;
    private final ColorPanel effectColorPanel;

    private final JBCheckBox showTextCheckBox;
    private final JBCheckBox showBackgroundCheckBox;
    private final JBCheckBox showEffectCheckBox;

    private final JBCheckBox showGutterIcon;

    private final String name;
    private final String helpDescription;

    public SeverityLevel(SeverityLevelState data, String name, String helpDescription) {
        this.name = name;
        this.helpDescription = helpDescription;

        this.showGutterIcon = new JBCheckBox("Show gutter icon", data.showGutterIcon);

        this.textColorPanel = new ColorPanel();
        this.textColorPanel.setSelectedColor(data.textColor);
        this.backgroundColorPanel = new ColorPanel();
        this.backgroundColorPanel.setSelectedColor(data.backgroundColor);
        this.effectColorPanel = new ColorPanel();
        this.effectColorPanel.setSelectedColor(data.effectColor);

        this.showTextCheckBox = new JBCheckBox("Text color", data.showText);
        this.showBackgroundCheckBox = new JBCheckBox("Background color", data.showBackground);
        this.showEffectCheckBox = new JBCheckBox("Effect color", data.showEffect);
    }

    @Override
    public void addToBuilder(FormBuilder formBuilder) {
        JPanel panel = new JPanel();
        ContextHelpLabel contextHelpLabel = ContextHelpLabel.create(helpDescription);
        JLabel label = new JBLabel(PREFIX + name + " settings: ");
        panel.add(label);
        panel.add(contextHelpLabel);

        JPanel panel1 = new JPanel();
        panel1.add(showGutterIcon);

        JPanel panel2 = new JPanel();
        panel2.add(showTextCheckBox);
        panel2.add(textColorPanel);

        JPanel panel3 = new JPanel();
        panel3.add(showBackgroundCheckBox);
        panel3.add(backgroundColorPanel);

        JPanel panel4 = new JPanel();
        panel4.add(showEffectCheckBox);
        panel4.add(effectColorPanel);

        formBuilder
                .addLabeledComponent(panel, new JLabel())
                .addLabeledComponent(panel1, new JLabel())
                .addLabeledComponent(panel2, new JLabel())
                .addLabeledComponent(panel3, new JLabel())
                .addLabeledComponent(panel4, new JLabel());
    }

    @Override
    public SeverityLevelState getState() {
        return new SeverityLevelState(
                showGutterIcon.isSelected(),
                showTextCheckBox.isSelected(),
                showBackgroundCheckBox.isSelected(),
                showEffectCheckBox.isSelected(),
                textColorPanel.getSelectedColor(),
                backgroundColorPanel.getSelectedColor(),
                effectColorPanel.getSelectedColor()
        );
    }
}

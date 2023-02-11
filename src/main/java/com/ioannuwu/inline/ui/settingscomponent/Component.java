package com.ioannuwu.inline.ui.settingscomponent;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.ColorPanel;
import com.intellij.ui.ContextHelpLabel;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextArea;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.UIUtil;
import com.ioannuwu.inline.data.DefaultSettings;
import com.ioannuwu.inline.data.EffectType;
import com.ioannuwu.inline.data.SeverityLevelState;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.util.ArrayList;
import java.util.Arrays;

public interface Component {

    void addToBuilder(FormBuilder formBuilder);


    class SeverityLevel implements Component, State<SeverityLevelState> {

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

            this.showTextCheckBox = new JBCheckBox(      "Text color                                                             ", data.showText);
            this.showBackgroundCheckBox = new JBCheckBox("Background color                                               ", data.showBackground);
            this.showEffectCheckBox = new JBCheckBox(    "Effect color                                                          ", data.showEffect);
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

    class NumberOfWhitespacesComponent implements Component, State<Integer> {

        private final JBTextField numberOfWhitespacesField;

        public NumberOfWhitespacesComponent(int numberOfWhitespaces) {
            this.numberOfWhitespacesField = new JBTextField("" + numberOfWhitespaces);
        }

        @Override
        public void addToBuilder(FormBuilder formBuilder) {
            JPanel panel = new JPanel();
            panel.add(new JBLabel("Number of whitespaces after end of the line          "));
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

    class MaxErrorsPerLineComponent implements Component, State<Integer> {

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

    class EffectTypeComponent implements Component, State<EffectType> {

        private static final String[] values = getValues();

        private final ComboBox<String> effectTypeComboBox;

        public EffectTypeComponent(EffectType effectType) {

            this.effectTypeComboBox = new ComboBox<>(values);

            for (int i = 0; i < values.length; i++) {
                assert values[i] != null;
                assert effectType != null;
                if (values[i].equals(effectType.name())) this.effectTypeComboBox.setSelectedIndex(i);
            }
        }

        @Override
        public void addToBuilder(FormBuilder formBuilder) {
            JPanel panel = new JPanel();
            panel.add(new JBLabel("Effect type                                                                    "));
            panel.add(effectTypeComboBox);
            formBuilder.addLabeledComponent(panel, new JLabel());
        }

        @Override
        public EffectType getState() {int index = effectTypeComboBox.getSelectedIndex();
            return EffectType.valueOf(values[index]);
        }

        private static String[] getValues() {
            ArrayList<String> effects = new ArrayList<>(5);
            for (EffectType type : EffectType.values()) {
                effects.add(type.name());
            }
            return effects.toArray(new String[0]);
        }
    }

    class IgnoreListComponent implements Component, State<String[]> {

        private final JBTextArea textComponent;

        public IgnoreListComponent(String[] ignoreList) {

            if (ignoreList.length == 0) {
                textComponent = new JBTextArea();
            } else {
                StringBuilder builder = new StringBuilder();
                for (String text : ignoreList) {
                    builder.append(text).append('\n');
                }
                textComponent = new JBTextArea(builder.toString());
            }
        }

        @Override
        public void addToBuilder(FormBuilder formBuilder) {
            JPanel panel = new JPanel();
            panel.add(new JBLabel(PREFIX + "Ignore: "));
            panel.add(ContextHelpLabel.create(description));
            formBuilder.addLabeledComponent(panel, new JLabel());
            formBuilder.addComponent(textComponent);
        }

        @Override
        public String[] getState() {
            String fulltext = textComponent.getText();
            String[] split = fulltext.split("\n");
            return Arrays.stream(split).filter(line -> !line.isBlank()).toArray(String[]::new);
        }

        private static final String description = "<h2 id=\"list-of-strings-divided-by-new-line\">List of strings divided by new line</h1>" +
                "<h3 id=\"hint-will-be-ignored-if-it-s-description-contains-one-of-them\">hint will be ignored if it&#39;s description contains one of them</h2>";
    }
    String PREFIX = " >   ";
}





































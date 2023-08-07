package com.ioannuwu.inline.ui.settingscomponent.components;

import com.intellij.ui.ContextHelpLabel;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.FormBuilder;
import com.ioannuwu.inline.ui.settingscomponent.Component;
import com.ioannuwu.inline2.settings.data.MutableState;

import javax.swing.*;
import java.util.Arrays;

public class IgnoreListComponent implements Component, MutableState<String[]> {

    private static final String PREFIX = " >   ";

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

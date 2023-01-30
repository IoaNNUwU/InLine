package com.ioannuwu.inline.ui;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class MyConfigurable implements Configurable {

    @Override
    public @Nullable @NonNls String getHelpTopic() {
        return "Help topic";
    }

    @Override
    public String getDisplayName() {
        return "IntelliJ Plugin";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return new JPanel();
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }
}

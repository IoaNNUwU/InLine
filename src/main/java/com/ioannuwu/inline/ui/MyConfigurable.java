package com.ioannuwu.inline.ui;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.Configurable;
import com.ioannuwu.inline.data.MySettingsService;
import com.ioannuwu.inline.domain.settings.SettingsChangeObservable;
import com.ioannuwu.inline.ui.settingscomponent.SettingsComponentProvider;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class MyConfigurable implements Configurable {

    private final MySettingsService settingsService = ApplicationManager.getApplication().getService(MySettingsService.class);

    private final SettingsComponentProvider settingsComponentProvider =
            new SettingsComponentProvider.Main(settingsService.getState());

    @Override
    public @Nullable @NonNls String getHelpTopic() {
        return "Help topic";
    }

    @Override
    public String getDisplayName() {
        return "InLine Settings";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return settingsComponentProvider.createComponent();
    }

    @Override
    public boolean isModified() {
        return !settingsService.getState().equals(settingsComponentProvider.getState());
    }

    @Override
    public void apply() {
        final var newState = settingsComponentProvider.getState();

        settingsService.loadState(newState);
        MySettingsService.OBSERVABLE.notify(newState);
    }
}

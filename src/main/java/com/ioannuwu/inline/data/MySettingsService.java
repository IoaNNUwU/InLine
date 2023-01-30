package com.ioannuwu.inline.data;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

@State(
  name = "com.ioannuwu.intellijerrors.data.SettingsState",
  storages = @Storage("com.ioannuwu.intellijerrors.data.SettingsState.xml")
)
public class MySettingsService implements PersistentStateComponent<SettingsState> {

    private SettingsState settingsState = new SettingsState();

    @Override
    public @NotNull SettingsState getState() {
        return settingsState;
    }

    @Override
    public void loadState(@NotNull SettingsState state) {
        XmlSerializerUtil.copyBean(state, settingsState);
    }

    private static final MySettingsService instance = ApplicationManager.getApplication().getService(MySettingsService.class);

    public static MySettingsService getInstance() {
        return instance;
    }
}

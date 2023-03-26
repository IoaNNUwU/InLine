package com.ioannuwu.inline.data;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.ioannuwu.inline.domain.settings.SettingsChangeListener;
import com.ioannuwu.inline.domain.settings.SettingsChangeObservable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@State(
        name = "com.ioannuwu.inline.data.SettingsState",
        storages = @Storage("com.ioannuwu.inline.Settings.xml")
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

    @Override
    public void initializeComponent() {
        OBSERVABLE.notify(settingsState);
    }

    public static final SettingsChangeObservable OBSERVABLE = new SettingsChangeObservable() {

        private final List<SettingsChangeListener> firstSubscribers = new ArrayList<>();
        private final List<SettingsChangeListener> defaultSubscribers = new ArrayList<>();
        private final List<SettingsChangeListener> lastSubscribers = new ArrayList<>();

        @Override
        public void subscribe(@NotNull SettingsChangeListener listener, @NotNull Priority priority) {
            switch (priority) {
                case FIRST: firstSubscribers.add(listener); break;
                case DEFAULT: defaultSubscribers.add(listener); break;
                case LAST: lastSubscribers.add(listener); break;
            }
        }

        @Override
        public void unsubscribe(@NotNull SettingsChangeListener listener) {
            firstSubscribers.remove(listener);
            defaultSubscribers.remove(listener);
            lastSubscribers.remove(listener);
        }

        @Override
        public void notify(@NotNull final SettingsState newSettingsState) {
            for (var sub : firstSubscribers) {
                sub.onSettingsChange(() -> newSettingsState);
            }
            for (var sub : defaultSubscribers) {
                sub.onSettingsChange(() -> newSettingsState);
            }
            for (var sub : lastSubscribers) {
                sub.onSettingsChange(() -> newSettingsState);
            }
        }
    };
}

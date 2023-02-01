package com.ioannuwu.inline.ui.settingscomponent;

import com.intellij.util.ui.FormBuilder;
import com.ioannuwu.inline.data.EffectType;
import com.ioannuwu.inline.data.SettingsState;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public interface SettingsComponentProvider extends State<SettingsState> {

    JComponent createComponent();

    @Override
    @NotNull SettingsState getState();

    class Main implements SettingsComponentProvider {

        private final SettingsState settingsState;

        public Main(SettingsState settingsState) {
            this.settingsState = settingsState;
        }

        private Component.NumberOfWhitespaces numberOfWhitespacesComponent;

        private Component.EffectTypeComponent effectTypeComponent;

        private Component.SeverityLevel errorComponent;
        private Component.SeverityLevel warningComponent;
        private Component.SeverityLevel weakWarningComponent;
        private Component.SeverityLevel informationComponent;
        private Component.SeverityLevel serverErrorComponent;
        private Component.SeverityLevel otherErrorComponent;

        private Component.IgnoreListComponent ignoreListComponent;

        @Override
        public JComponent createComponent() {
            numberOfWhitespacesComponent = new Component.NumberOfWhitespaces(settingsState.numberOfWhitespaces);

            effectTypeComponent = new Component.EffectTypeComponent(settingsState.effectType);

            errorComponent = new Component.SeverityLevel(settingsState.error,
                    "Error", "Compilation errors");
            warningComponent = new Component.SeverityLevel(settingsState.warning,
                    "Warning", "Warning description");
            weakWarningComponent = new Component.SeverityLevel(settingsState.weakWarning,
                    "Weak warning", "Weak warning description");
            informationComponent = new Component.SeverityLevel(settingsState.information,
                    "Information", "Information description");
            serverErrorComponent = new Component.SeverityLevel(settingsState.serverError,
                    "Server error", "Connection error");
            otherErrorComponent = new Component.SeverityLevel(settingsState.otherError,
                    "Other error", "Small hints");

            ignoreListComponent = new Component.IgnoreListComponent(settingsState.ignoreList);

            final FormBuilder formBuilder = FormBuilder.createFormBuilder();

            numberOfWhitespacesComponent.addToBuilder(formBuilder);

            effectTypeComponent.addToBuilder(formBuilder);

            errorComponent.addToBuilder(formBuilder);
            formBuilder.addComponent(new JPanel());
            warningComponent.addToBuilder(formBuilder);
            formBuilder.addComponent(new JPanel());
            weakWarningComponent.addToBuilder(formBuilder);
            formBuilder.addComponent(new JPanel());
            informationComponent.addToBuilder(formBuilder);
            formBuilder.addComponent(new JPanel());
            serverErrorComponent.addToBuilder(formBuilder);
            formBuilder.addComponent(new JPanel());
            otherErrorComponent.addToBuilder(formBuilder);

            ignoreListComponent.addToBuilder(formBuilder);

            return formBuilder.getPanel();
        }

        @Override
        public @NotNull SettingsState getState() {
            SettingsState state = new SettingsState();

            state.numberOfWhitespaces = numberOfWhitespacesComponent.getState();
            state.effectType = effectTypeComponent.getState();

            state.error = errorComponent.getState();
            state.warning = warningComponent.getState();
            state.weakWarning = weakWarningComponent.getState();
            state.information = informationComponent.getState();
            state.serverError = serverErrorComponent.getState();
            state.otherError = otherErrorComponent.getState();

            state.ignoreList = ignoreListComponent.getState();

            return state;
        }
    }

}

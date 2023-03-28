package com.ioannuwu.inline.ui.settingscomponent;

import com.intellij.util.ui.FormBuilder;
import com.ioannuwu.inline.data.SettingsState;
import com.ioannuwu.inline.ui.settingscomponent.components.*;
import com.ioannuwu.inline.ui.settingscomponent.components.fontselectioncomponent.FontSelectionComponent;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public interface SettingsComponentProvider extends State<SettingsState> {

    JComponent createComponent();

    @Override
    @NotNull SettingsState getState();

    class Main implements SettingsComponentProvider {

        private final SettingsState settingsState;

        public Main(SettingsState settingsState) {
            this.settingsState = settingsState;
        }

        private NumberOfWhitespacesComponent numberOfWhitespacesComponent;

        private MaxErrorsPerLineComponent maxErrorsPerLineComponent;

        private EffectTypeComponent effectTypeComponent;

        private FontSelectionComponent fontSelectionComponent;

        private OneGutterModeComponent oneGutterModeComponent;
        private TextStyleSelectionComponent textStyleSelectionComponent;

        private SeverityLevel errorComponent;
        private SeverityLevel warningComponent;
        private SeverityLevel weakWarningComponent;
        private SeverityLevel informationComponent;
        private SeverityLevel serverErrorComponent;
        private SeverityLevel otherErrorComponent;

        private IgnoreListComponent ignoreListComponent;

        @Override
        public JComponent createComponent() {
            numberOfWhitespacesComponent = new NumberOfWhitespacesComponent(settingsState.numberOfWhitespaces);

            effectTypeComponent = new EffectTypeComponent(settingsState.effectType);

            maxErrorsPerLineComponent = new MaxErrorsPerLineComponent(settingsState.maxErrorsPerLine);

            fontSelectionComponent = new FontSelectionComponent(
                    settingsState.font.textSample,
                    settingsState.font.fontName,
                    GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts());

            oneGutterModeComponent = new OneGutterModeComponent(settingsState.oneGutterMode);
            textStyleSelectionComponent = new TextStyleSelectionComponent(settingsState.textStyle);

            errorComponent = new SeverityLevel(settingsState.error,
                    "Error", "Compilation errors");
            warningComponent = new SeverityLevel(settingsState.warning,
                    "Warning", "Logic errors and big improvements");
            weakWarningComponent = new SeverityLevel(settingsState.weakWarning,
                    "Weak warning", "Small improvements");
            informationComponent = new SeverityLevel(settingsState.information,
                    "Information", "Small information messages");
            serverErrorComponent = new SeverityLevel(settingsState.serverError,
                    "Server error", "Connection error");
            otherErrorComponent = new SeverityLevel(settingsState.otherError,
                    "Other error", "Small hints");

            ignoreListComponent = new IgnoreListComponent(settingsState.ignoreList);

            final FormBuilder formBuilder = FormBuilder.createFormBuilder();

            numberOfWhitespacesComponent.addToBuilder(formBuilder);
            maxErrorsPerLineComponent.addToBuilder(formBuilder);

            effectTypeComponent.addToBuilder(formBuilder);

            fontSelectionComponent.addToBuilder(formBuilder);

            oneGutterModeComponent.addToBuilder(formBuilder);
            textStyleSelectionComponent.addToBuilder(formBuilder);

            formBuilder.addComponent(new JPanel());

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

            state.maxErrorsPerLine = maxErrorsPerLineComponent.getState();

            state.font = fontSelectionComponent.getState();

            state.textStyle = textStyleSelectionComponent.getState();
            state.oneGutterMode = oneGutterModeComponent.getState();

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

package com.ioannuwu.inline2.settings.ui

import com.intellij.util.ui.FormBuilder
import com.ioannuwu.inline.ui.settingscomponent.components.*
import com.ioannuwu.inline.ui.settingscomponent.components.fontselectioncomponent.FontSelectionComponent
import com.ioannuwu.inline2.settings.data.SettingsState
import java.awt.GraphicsEnvironment
import javax.swing.JComponent

class SettingsUIViewImpl(initialState: SettingsState) : SettingsUIView {

    private var settingsState = initialState

    override val state: SettingsState
        get() {
            val state = SettingsState()

            with(state) {
                numberOfWhitespaces = numberOfWhitespacesComponent.state

                maxErrorsPerLine = maxErrorsPerLineComponent.state
                effectType = effectTypeComponent.state
                font = fontSelectionComponent.state
                oneGutterMode = oneGutterModeComponent.state
                textStyle = textStyleSelectionComponent.state

                error = errorComponent.state
                warning = warningComponent.state
                weakWarning = weakWarningComponent.state
                information = informationComponent.state
                serverError = serverErrorComponent.state
                otherError = otherErrorComponent.state

                ignoreList = ignoreListComponent.state
            }

            return state
        }


    private lateinit var numberOfWhitespacesComponent: NumberOfWhitespacesComponent

    private lateinit var maxErrorsPerLineComponent: MaxErrorsPerLineComponent

    private lateinit var effectTypeComponent: EffectTypeComponent

    private lateinit var fontSelectionComponent: FontSelectionComponent

    private lateinit var oneGutterModeComponent: OneGutterModeComponent
    private lateinit var textStyleSelectionComponent: TextStyleSelectionComponent

    private lateinit var errorComponent: SeverityLevel
    private lateinit var warningComponent: SeverityLevel
    private lateinit var weakWarningComponent: SeverityLevel
    private lateinit var informationComponent: SeverityLevel
    private lateinit var serverErrorComponent: SeverityLevel
    private lateinit var otherErrorComponent: SeverityLevel

    private lateinit var ignoreListComponent: IgnoreListComponent


    override fun createComponent(): JComponent {
        numberOfWhitespacesComponent = NumberOfWhitespacesComponent(settingsState.numberOfWhitespaces)

        effectTypeComponent = EffectTypeComponent(settingsState.effectType)

        maxErrorsPerLineComponent = MaxErrorsPerLineComponent(settingsState.maxErrorsPerLine)

        fontSelectionComponent = FontSelectionComponent(
            settingsState.font.textSample,
            settingsState.font.fontName,
            GraphicsEnvironment.getLocalGraphicsEnvironment().allFonts
        )

        oneGutterModeComponent = OneGutterModeComponent(settingsState.oneGutterMode)
        textStyleSelectionComponent = TextStyleSelectionComponent(settingsState.textStyle)

        errorComponent = SeverityLevel(
            settingsState.error,
            "Error", "Compilation errors"
        )
        warningComponent = SeverityLevel(
            settingsState.warning,
            "Warning", "Logic errors and big improvements"
        )
        weakWarningComponent = SeverityLevel(
            settingsState.weakWarning,
            "Weak warning", "Small improvements"
        )
        informationComponent = SeverityLevel(
            settingsState.information,
            "Information", "Small information messages"
        )
        serverErrorComponent = SeverityLevel(
            settingsState.serverError,
            "Server error", "Connection error"
        )
        otherErrorComponent = SeverityLevel(
            settingsState.otherError,
            "Other error", "Small hints"
        )

        ignoreListComponent = IgnoreListComponent(settingsState.ignoreList)

        val formBuilder = FormBuilder.createFormBuilder()

        formBuilder.addSeparator()
        fontSelectionComponent.addToBuilder(formBuilder)
        formBuilder.addSeparator()
        textStyleSelectionComponent.addToBuilder(formBuilder)
        effectTypeComponent.addToBuilder(formBuilder)

        formBuilder.addSeparator()
        oneGutterModeComponent.addToBuilder(formBuilder)
        formBuilder.addSeparator()
        maxErrorsPerLineComponent.addToBuilder(formBuilder)
        numberOfWhitespacesComponent.addToBuilder(formBuilder)

        formBuilder.addSeparator()
        errorComponent.addToBuilder(formBuilder)
        formBuilder.addSeparator()
        warningComponent.addToBuilder(formBuilder)
        formBuilder.addSeparator()
        weakWarningComponent.addToBuilder(formBuilder)
        formBuilder.addSeparator()
        informationComponent.addToBuilder(formBuilder)
        formBuilder.addSeparator()
        serverErrorComponent.addToBuilder(formBuilder)
        formBuilder.addSeparator()
        otherErrorComponent.addToBuilder(formBuilder)
        formBuilder.addSeparator()

        ignoreListComponent.addToBuilder(formBuilder)
        formBuilder.addSeparator()

        return formBuilder.panel
    }
}
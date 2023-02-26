package com.ioannuwu.inline.ui.settingscomponent.components;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.FormBuilder;
import com.ioannuwu.inline.data.EffectType;
import com.ioannuwu.inline.ui.settingscomponent.Component;
import com.ioannuwu.inline.ui.settingscomponent.State;

import javax.swing.*;
import java.util.ArrayList;

public class EffectTypeComponent implements Component, State<EffectType> {

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
    public EffectType getState() {
        int index = effectTypeComboBox.getSelectedIndex();
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

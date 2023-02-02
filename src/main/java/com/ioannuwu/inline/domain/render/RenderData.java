package com.ioannuwu.inline.domain.render;

import com.ioannuwu.inline.data.EffectType;
import com.ioannuwu.inline.data.SettingsState;
import com.ioannuwu.inline.data.SeverityLevelState;

import javax.swing.*;
import java.awt.*;

public class RenderData extends SeverityLevelState {

    public final int numberOfWhitespaces;
    public final EffectType effectType;
    public final String description;
    public final Icon icon;

    public RenderData(SeverityLevelState severityLevelState, SettingsState settingsState, String description, Icon icon) {
        super(severityLevelState);
        this.numberOfWhitespaces = settingsState.numberOfWhitespaces;
        this.effectType = settingsState.effectType;
        this.description = description;
        this.icon = icon;
    }

    public RenderData(SeverityLevelState severityLevelState, int numberOfWhitespaces, EffectType effectType, String description, Icon icon) {
        super(severityLevelState);
        this.numberOfWhitespaces = numberOfWhitespaces;
        this.effectType = effectType;
        this.description = description;
        this.icon = icon;
    }

    public RenderData(boolean showGutterIcon, boolean showText, boolean showBackground, boolean showEffect,
                      Color textColor, Color backgroundColor, Color effectColor, int numberOfWhitespaces,
                      EffectType effectType, String description, Icon icon) {
        this.showGutterIcon = showGutterIcon;
        this.showText = showText;
        this.showBackground = showBackground;
        this.showEffect = showEffect;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        this.effectColor = effectColor;
        this.numberOfWhitespaces = numberOfWhitespaces;
        this.effectType = effectType;
        this.description = description;
        this.icon = icon;
    }

    public static final RenderData EMPTY = new RenderData(false, false, false, false, Color.RED, Color.GREEN, Color.BLUE, 0, EffectType.NONE, "", null);
}

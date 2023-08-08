package com.ioannuwu.inline.data;

import com.intellij.icons.AllIcons;

import javax.swing.*;
import java.awt.*;

public class DefaultSettings {
    public static final int NUMBER_OF_WHITESPACES = 0;

    public static final EffectType EFFECT_TYPE = EffectType.NONE;

    public static final int MAX_ERRORS_PER_LINE = 5;

    public static final FontSettingsState FONT = new FontSettingsState("Dialog.plain", "sample");

    public static final TextStyle TEXT_STYLE = TextStyle.AFTER_LINE;
    public static final boolean ONE_GUTTER_MODE = false;

    public static final SeverityLevelState ERROR = defaultSeverityLevelState(new Color(183, 43, 43));
    public static final SeverityLevelState WARNING = defaultSeverityLevelState(new Color(189, 115, 37));
    public static final SeverityLevelState WEAK_WARNING = defaultSeverityLevelState(new Color(183, 155, 41));
    public static final SeverityLevelState INFORMATION = defaultSeverityLevelState(new Color(61, 108, 201));
    public static final SeverityLevelState SERVER_ERROR = defaultSeverityLevelState(new Color(128, 29, 185));
    public static final SeverityLevelState OTHER_ERROR = defaultSeverityLevelState(new Color(141, 169, 169));

    public static final String[] IGNORE_LIST = new String[]{
            "TODO",
            "Typo",
            "Automatically declared based on the expected type",
            "Value captured in a closure",
            "Expression should use clarifying parentheses",
            "Missing trailing comma",
    };

    public static class Icons {
        public static final Icon ERROR = AllIcons.General.Error;
        public static final Icon WARNING = AllIcons.General.Warning;
        public static final Icon WEAK_WARNING = AllIcons.General.Warning;
        public static final Icon INFORMATION = AllIcons.General.Information;
        public static final Icon SERVER_ERROR = AllIcons.General.ArrowDown;
        public static final Icon OTHER_ERROR = AllIcons.General.Add;
    }

    private static SeverityLevelState defaultSeverityLevelState(Color color) {
        Color backgroundColor = color.darker().darker();
        Color newBackgroundColor = new Color(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue(), 80);
        return new SeverityLevelState(
                color,
                newBackgroundColor,
                color.darker()
        );
    }
}

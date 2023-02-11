package com.ioannuwu.inline.data;

import com.intellij.icons.AllIcons;

import javax.swing.*;
import java.awt.*;

public class DefaultSettings {
    public static final int NUMBER_OF_WHITESPACES = 2;

    public static final EffectType EFFECT_TYPE = EffectType.NONE;

    public static final SeverityLevelState ERROR = defaultSeverityLevelState(new Color(183, 43, 43));
    public static final SeverityLevelState WARNING = defaultSeverityLevelState(new Color(189, 115, 37));
    public static final SeverityLevelState WEAK_WARNING = defaultSeverityLevelState(new Color(183, 155, 41));
    public static final SeverityLevelState INFORMATION = defaultSeverityLevelState(new Color(61, 108, 201));
    public static final SeverityLevelState SERVER_ERROR = defaultSeverityLevelState(new Color(128, 29, 185));
    public static final SeverityLevelState OTHER_ERROR = defaultSeverityLevelState(new Color(141, 169, 169));

    public static final String[] IGNORE_LIST = new String[]{"TYPO", "TODO"};

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

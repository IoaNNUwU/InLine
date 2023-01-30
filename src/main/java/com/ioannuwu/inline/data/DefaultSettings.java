package com.ioannuwu.inline.data;


import com.intellij.icons.AllIcons;

import javax.swing.*;
import java.awt.*;

public class DefaultSettings {
    public static final int NUMBER_OF_WHITESPACES = 2;

    public static final int BORDER = 2;

    public static final SeverityLevelState ERROR = new SeverityLevelState(
            true, true, true, true,
            new Color(185, 29, 29), new Color(108, 48, 48), new Color(220, 56, 0));

    public static final SeverityLevelState WARNING = new SeverityLevelState(
            true, true, true, true,
            new Color(185, 94, 29), new Color(93, 55, 39), new Color(222, 153, 0));

    public static final SeverityLevelState WEAK_WARNING = new SeverityLevelState(
            true, true, true, true,
            new Color(210, 174, 30), new Color(80, 70, 33), new Color(152, 60, 5));

    public static final SeverityLevelState INFORMATION = new SeverityLevelState(
            true, true, true, true,
            new Color(40, 95, 208), new Color(56, 75, 119), new Color(0, 168, 215));

    public static final SeverityLevelState SERVER_ERROR = new SeverityLevelState(
            true, true, true, true,
            new Color(128, 29, 185), new Color(62, 36, 91), new Color(171, 9, 167));

    public static final SeverityLevelState OTHER_ERROR = new SeverityLevelState(
            true, true, true, true,
            new Color(197, 197, 197), new Color(61, 61, 61), new Color(153, 185, 184));

    public static final String[] IGNORE_LIST = new String[]{"TYPO", "TODO"};

    public static class Icons {
        public static final Icon ERROR = AllIcons.General.Error;
        public static final Icon WARNING = AllIcons.General.Warning;
        public static final Icon WEAK_WARNING = AllIcons.General.Warning;
        public static final Icon INFORMATION = AllIcons.General.Information;
        public static final Icon SERVER_ERROR = AllIcons.General.ArrowDown;
        public static final Icon OTHER_ERROR = AllIcons.General.Add;
    }
}

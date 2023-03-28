package com.ioannuwu.inline.domain;

import com.ioannuwu.inline.data.EffectType;
import com.ioannuwu.inline.data.SeverityLevelState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class RenderData extends SeverityLevelState {

    public final int numberOfWhitespaces;
    public final int maxErrorsPerLine;
    public final @NotNull EffectType effectType;
    public final @NotNull String description;
    public final @Nullable Icon icon;

    public final @NotNull TextStyle textStyle;
    public final Boolean oneGutterMode;

    public RenderData(boolean showGutterIcon, boolean showText, boolean showBackground, boolean showEffect,
                      @NotNull Color textColor, @NotNull Color backgroundColor, @NotNull Color effectColor, int numberOfWhitespaces,
                      int maxErrorsPerLine, @NotNull EffectType effectType, @NotNull String description, @Nullable Icon icon, @NotNull TextStyle textStyle, Boolean oneGutterMode) {
        this.textStyle = textStyle;
        this.oneGutterMode = oneGutterMode;
        this.showGutterIcon = showGutterIcon;
        this.showText = showText;
        this.showBackground = showBackground;
        this.showEffect = showEffect;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        this.effectColor = effectColor;
        this.numberOfWhitespaces = numberOfWhitespaces;
        this.maxErrorsPerLine = maxErrorsPerLine;
        this.effectType = effectType;
        this.description = description;
        this.icon = icon;
    }
}

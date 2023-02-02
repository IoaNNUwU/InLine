package com.ioannuwu.inline.data;

import com.intellij.util.xmlb.annotations.Attribute;
import com.intellij.util.xmlb.annotations.OptionTag;
import org.apache.xmlgraphics.java2d.color.DefaultColorConverter;

import java.awt.*;
import java.io.Serializable;
import java.util.Objects;

public class SeverityLevelState implements Serializable {

    public SeverityLevelState() {}

    public SeverityLevelState(boolean showGutterIcon, boolean showText, boolean showBackground, boolean showEffect,
                              Color textColor, Color backgroundColor, Color effectColor) {
        this.showGutterIcon = showGutterIcon;
        this.showText = showText;
        this.showBackground = showBackground;
        this.showEffect = showEffect;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        this.effectColor = effectColor;
    }

    public SeverityLevelState(Color textColor, Color backgroundColor, Color effectColor) {
        this(true, true, true, true, textColor, backgroundColor, effectColor);
    }

    public SeverityLevelState(SeverityLevelState other) {
        this.showGutterIcon = other.showGutterIcon;
        this.showText = other.showText;
        this.showBackground = other.showBackground;
        this.showEffect = other.showEffect;
        this.textColor = other.textColor;
        this.backgroundColor = other.backgroundColor;
        this.effectColor = other.effectColor;
    }

    public boolean showGutterIcon = true;

    public boolean showText = true;
    public boolean showBackground = true;
    public boolean showEffect = true;
    @Attribute(converter = MyColorConverter.class)
    public Color textColor = Color.RED;
    @Attribute(converter = MyColorConverter.class)
    public Color backgroundColor = Color.GREEN;
    @Attribute(converter = MyColorConverter.class)
    public Color effectColor = Color.ORANGE;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeverityLevelState that = (SeverityLevelState) o;
        return showGutterIcon == that.showGutterIcon && showText == that.showText && showBackground == that.showBackground && showEffect == that.showEffect && Objects.equals(textColor, that.textColor) && Objects.equals(backgroundColor, that.backgroundColor) && Objects.equals(effectColor, that.effectColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(showGutterIcon, showText, showBackground, showEffect, textColor, backgroundColor, effectColor);
    }

    @Override
    public String toString() {
        return "SeverityLevelState{" +
                "showGutterIcon=" + showGutterIcon +
                ", showText=" + showText +
                ", showBackground=" + showBackground +
                ", showEffect=" + showEffect +
                ", textColor=" + textColor +
                ", backgroundColor=" + backgroundColor +
                ", effectColor=" + effectColor +
                '}';
    }
}

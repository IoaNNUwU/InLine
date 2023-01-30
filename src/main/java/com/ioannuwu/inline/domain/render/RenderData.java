package com.ioannuwu.inline.domain.render;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class RenderData {

    public final @Nullable String text;
    public final @Nullable Color textColor;
    public final @Nullable Color effectColor;
    public final @Nullable Color backgroundColor;
    public final @Nullable Icon gutterIcon;

    public RenderData(@Nullable String text, @Nullable Color textColor, @Nullable Color effectColor,
                      @Nullable Color backgroundColor, @Nullable Icon gutterIcon) {
        this.text = text;
        this.textColor = textColor;
        this.effectColor = effectColor;
        this.backgroundColor = backgroundColor;
        this.gutterIcon = gutterIcon;
    }

    public static RenderData EMPTY = new RenderData(null, null, null, null, null);

    @Override
    public String toString() {
        return "RenderData{" +
                "text='" + text + '\'' +
                ", textColor=" + textColor +
                ", effectColor=" + effectColor +
                ", backgroundColor=" + backgroundColor +
                ", gutterIcon=" + gutterIcon +
                '}';
    }
}

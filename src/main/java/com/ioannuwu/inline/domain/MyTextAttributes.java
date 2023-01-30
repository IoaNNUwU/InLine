package com.ioannuwu.inline.domain;

import com.intellij.openapi.editor.markup.TextAttributes;

import java.awt.*;

public class MyTextAttributes extends TextAttributes {

    public MyTextAttributes(Color backgroundColor) {
        super(null, backgroundColor, null, null, Font.PLAIN);
    }
}

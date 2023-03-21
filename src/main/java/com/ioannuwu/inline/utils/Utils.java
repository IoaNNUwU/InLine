package com.ioannuwu.inline.utils;

import com.intellij.openapi.Disposable;

import java.awt.*;

public interface Utils {

    Disposable EMPTY_DISPOSABLE = () -> {};

    GraphicsEnvironment GRAPHICS_ENVIRONMENT = GraphicsEnvironment.getLocalGraphicsEnvironment();
}

package com.ioannuwu.inline.utils;

import com.intellij.openapi.Disposable;
import com.ioannuwu.inline.domain.utils.modes.Entity;
import com.ioannuwu.inline.domain.utils.modes.EntityComparator;

import java.awt.*;
import java.util.Comparator;

public class Utils {
    private Utils() {}


    public static final Disposable EMPTY_DISPOSABLE = () -> {};

    public static final GraphicsEnvironment GRAPHICS_ENVIRONMENT = GraphicsEnvironment.getLocalGraphicsEnvironment();

    public static final Comparator<Entity> ENTITY_COMPARATOR_BY_OFFSET = new EntityComparator.ByOffset().reversed();

    public static final Comparator<Entity> ENTITY_COMPARATOR = new EntityComparator.BySeverity()
            .thenComparing(ENTITY_COMPARATOR_BY_OFFSET)
            .thenComparing(new EntityComparator.ByDescription());
}

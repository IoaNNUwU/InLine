package com.ioannuwu.inline.graphics

import java.awt.Color
import java.awt.Graphics
import java.awt.Rectangle

interface GraphicsComponentKt : Width {

    /**
     * This property tells if component is fancy, if so, it is
     * possible to draw special parts of it in special color
     * with GraphicsComponentKt.drawFancy(Graphics, Rectangle, Color)
     */
    val priority: Int
        get() = 0

    /**
     * This method draws special parts of a component in special color.
     *
     * This method must be overridden by components that are intended to be used
     * by other components.
     *
     * For example, effect components use a text component to
     * draw a shadow.
     *
     * # This method should not be used outside component.
     */
    fun drawFancy(g: Graphics, targetRegion: Rectangle, color: Color) {}

    /**
     * This method draws component in its original color
     */
    fun draw(g: Graphics, targetRegion: Rectangle)
}
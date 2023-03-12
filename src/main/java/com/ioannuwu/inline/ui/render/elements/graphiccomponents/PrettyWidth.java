package com.ioannuwu.inline.ui.render.elements.graphiccomponents;

public interface PrettyWidth {
    /**
     * @return width of element with necessary spaces
     */
    default int width() {
        return 0;
    }
}

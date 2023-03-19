package com.ioannuwu.inline.ui.render.elements;

import com.intellij.openapi.Disposable;
import com.ioannuwu.inline.utils.Utils;

/**
 * This interface represents PART of all that rendered in editor for
 * SPECIFIC error (hint). Multiple RenderElements can be associated with
 * SINGLE error (hint).
 * <p>
 * This includes background highlighter, gutter icon renderer,
 * text highlighter etc.
 */
public interface RenderElement {
    /**
     * @return Disposable associated with that render element. It should be
     * removed when associated error is removed
     */
    default Disposable render() {
        return Utils.EMPTY_DISPOSABLE;
    }

    RenderElement EMPTY = new RenderElement() { };
}

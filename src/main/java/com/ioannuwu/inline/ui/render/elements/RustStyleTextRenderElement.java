package com.ioannuwu.inline.ui.render.elements;

import com.intellij.openapi.Disposable;
import com.ioannuwu.inline.utils.DisposableUtils;

public class RustStyleTextRenderElement implements RenderElement {

    @Override
    public Disposable render() {
        return DisposableUtils.EMPTY;
    }
}

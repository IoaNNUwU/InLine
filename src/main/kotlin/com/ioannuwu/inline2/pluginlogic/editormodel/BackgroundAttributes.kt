package com.ioannuwu.inline2.pluginlogic.editormodel

import com.intellij.openapi.editor.markup.TextAttributes
import java.awt.Color

class BackgroundAttributes(backgroundColor: Color) : TextAttributes(
    null,
    backgroundColor,
    null,
    null,
    0
)
package com.ioannuwu.inline.utils

import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.RangeMarker
import com.intellij.openapi.util.Key

object TestDocument : Document {

    val LINE_COUNT = 10

    override fun getLineCount(): Int = LINE_COUNT
    override fun getLineNumber(offset: Int): Int = offset


    override fun <T : Any?> getUserData(key: Key<T>): T = TEST()
    override fun <T : Any?> putUserData(key: Key<T>, value: T?) = TEST()
    override fun getImmutableCharSequence(): CharSequence = TEST()
    override fun getLineStartOffset(line: Int): Int = TEST()
    override fun getLineEndOffset(line: Int): Int = TEST()
    override fun insertString(offset: Int, s: CharSequence) = TEST()
    override fun deleteString(startOffset: Int, endOffset: Int) = TEST()
    override fun replaceString(startOffset: Int, endOffset: Int, s: CharSequence) = TEST()
    override fun isWritable(): Boolean = TEST()
    override fun getModificationStamp(): Long = TEST()
    override fun createRangeMarker(startOffset: Int, endOffset: Int, surviveOnExternalChange: Boolean): RangeMarker = TEST()
    override fun createGuardedBlock(startOffset: Int, endOffset: Int): RangeMarker = TEST()
    override fun setText(text: CharSequence) = TEST()
}
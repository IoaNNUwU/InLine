package com.ioannuwu.inline

import com.intellij.openapi.editor.ex.MarkupModelEx
import com.intellij.openapi.editor.ex.RangeHighlighterEx
import com.intellij.openapi.editor.impl.DocumentMarkupModel
import com.intellij.openapi.editor.impl.event.MarkupModelListener
import com.intellij.openapi.fileEditor.*
import com.intellij.openapi.util.Pair
import com.intellij.openapi.vfs.VirtualFile
import com.ioannuwu.inline.data.MySettingsService
import com.ioannuwu.inline.domain.utils.MaxPerLine
import com.ioannuwu.inline.domain.utils.RenderDataProvider
import com.ioannuwu.inline.domain.utils.RenderElementsProvider
import com.ioannuwu.inline.domain.utils.modes.CouplePerLineWithHighestPriorityMode
import com.ioannuwu.inline.domain.utils.modes.Mode
import com.ioannuwu.inline.help.ElementsRendererMarkupModelListener
import com.ioannuwu.inline.ui.render.EditorElementsRenderer
import com.ioannuwu.inline.utils.Utils

class EditorOpenedListenerKt : FileEditorManagerListener {
    override fun fileOpenedSync(
        source: FileEditorManager,
        file: VirtualFile,
        editors: Pair<Array<FileEditor>, Array<FileEditorProvider>>
    ) {
        for (fileEditor in editors.first) {

            if (fileEditor !is TextEditor) continue

            val markupModel = DocumentMarkupModel.forDocument(
                fileEditor.editor.document, fileEditor.editor.project, false
            )

            markupModel as? MarkupModelEx ?: continue

            val mode: Mode = CouplePerLineWithHighestPriorityMode(
                RenderElementsProvider.BySettings(
                    renderDataProvider, fileEditor.editor,
                    settingsService, Utils.GRAPHICS_ENVIRONMENT
                ),
                EditorElementsRenderer.Impl(), MaxPerLine.BySettings(settingsService)
            )
            val markupModelListener = ElementsRendererMarkupModelListener(mode)

            markupModel.addMarkupModelListener(fileEditor, markupModelListener)
            map[fileEditor] = markupModelListener
        }
    }

    companion object {

        private val map: MutableMap<TextEditor, MarkupModelListener> = HashMap()

        private val settingsService = MySettingsService.getInstance()
        private val renderDataProvider: RenderDataProvider = RenderDataProvider.BySettings(settingsService)

        @JvmStatic
        fun updateActiveListeners() {
            for ((key, listener) in map) {
                val editor = key.editor
                val document = editor.document
                val markupModel = DocumentMarkupModel.forDocument(document, editor.project, false)
                val allHighlighters = markupModel.allHighlighters
                for (highlighter in allHighlighters) {
                    if (highlighter !is RangeHighlighterEx) return
                    listener.attributesChanged(highlighter, false, true)
                }
            }
        }
    }
}
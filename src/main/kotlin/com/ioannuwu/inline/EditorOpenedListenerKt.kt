package com.ioannuwu.inline

import com.intellij.openapi.editor.ex.MarkupModelEx
import com.intellij.openapi.editor.ex.RangeHighlighterEx
import com.intellij.openapi.editor.impl.DocumentMarkupModel
import com.intellij.openapi.editor.impl.event.MarkupModelListener
import com.intellij.openapi.fileEditor.*
import com.intellij.openapi.util.Pair
import com.intellij.openapi.vfs.VirtualFile
import com.ioannuwu.inline.data.MySettingsService
import com.ioannuwu.inline.domain.utils.RenderDataProvider
import java.awt.GraphicsEnvironment

class EditorOpenedListenerKt : FileEditorManagerListener {
    override fun fileOpenedSync(
        source: FileEditorManager,
        file: VirtualFile,
        editors: Pair<Array<FileEditor>, Array<FileEditorProvider>>
    ) {
        for (fileEditor in editors.first) {

            if (fileEditor !is TextEditor) continue

            val editor = fileEditor.editor
            val document = editor.document
            val markupModel = DocumentMarkupModel.forDocument(document, editor.project, false)

            markupModel as? MarkupModelEx ?: continue

            val view = View.EditorView(editor)
            val renderElementsProvider = RenderElementsProviderKt.BySettings(
                renderDataProvider, settingsService, GraphicsEnvironment.getLocalGraphicsEnvironment(), editor
            )

            val viewModel = ViewModel.Impl(
                view, renderElementsProvider, document, MaxPerLineKt.BySettings(settingsService)
            )

            val markupModelListener = MarkupModelListenerKt(EditorCallback.ViewModelEditorCallback(viewModel))

            markupModel.addMarkupModelListener(fileEditor, markupModelListener)
            map[fileEditor] = markupModelListener
        }
    }

    companion object {

        private val map: MutableMap<TextEditor, MarkupModelListener> = HashMap()

        private val settingsService = MySettingsService.getInstance()
        private val renderDataProvider: RenderDataProviderKt = RenderDataProviderKt.BySettings(settingsService)

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
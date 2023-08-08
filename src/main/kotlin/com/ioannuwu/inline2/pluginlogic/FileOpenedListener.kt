package com.ioannuwu.inline2.pluginlogic

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.ex.MarkupModelEx
import com.intellij.openapi.editor.ex.RangeHighlighterEx
import com.intellij.openapi.editor.impl.DocumentMarkupModel
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileOpenedSyncListener
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.fileEditor.ex.FileEditorWithProvider
import com.intellij.openapi.vfs.VirtualFile
import com.ioannuwu.inline2.pluginlogic.editormodel.EditorModelImpl
import com.ioannuwu.inline2.pluginlogic.render.BySettingsDataSelector
import com.ioannuwu.inline2.pluginlogic.utils.runOnEDT
import com.ioannuwu.inline2.settings.data.SettingsService
import com.ioannuwu.inline2.settings.event.SettingsChangeDispatcher

class FileOpenedListener : FileOpenedSyncListener {

    override fun fileOpenedSync(
        source: FileEditorManager,
        file: VirtualFile,
        editorsWithProviders: List<FileEditorWithProvider>
    ) {
        for (fileEditorWithProvider in editorsWithProviders) {

            val fileEditor = fileEditorWithProvider.fileEditor
                    as? TextEditor ?: continue

            val editor = fileEditor.editor
            val document = editor.document
            val markupModel = DocumentMarkupModel.forDocument(document, editor.project, false)

            markupModel as? MarkupModelEx ?: continue

            val dataSelector = BySettingsDataSelector(
                ApplicationManager.getApplication().getService(SettingsService::class.java).state
            )

            SettingsChangeDispatcher.subscribe(dataSelector)

            val markupModelListener =
                ErrorMarkupModelListener(EditorModelImpl(editor), dataSelector, dataSelector, editor)

            listeners.add(Pair(markupModel, markupModelListener))

            markupModel.addMarkupModelListener(fileEditor, markupModelListener)
        }
    }

    companion object {

        private val listeners: MutableList<Pair<MarkupModelEx, ErrorMarkupModelListener>> = mutableListOf()

        fun updateAllListeners() {
            listeners.forEach { (model, listener) ->
                model.allHighlighters
                    .filterIsInstance(RangeHighlighterEx::class.java)
                    .forEach {
                        listener.attributesChanged(it, true, true)
                    }
            }
        }
    }
}
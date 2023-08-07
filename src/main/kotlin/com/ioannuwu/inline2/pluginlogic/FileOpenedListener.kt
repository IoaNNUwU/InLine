package com.ioannuwu.inline2.pluginlogic

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.ex.MarkupModelEx
import com.intellij.openapi.editor.impl.DocumentMarkupModel
import com.intellij.openapi.editor.impl.event.MarkupModelListener
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileOpenedSyncListener
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.fileEditor.ex.FileEditorWithProvider
import com.intellij.openapi.vfs.VirtualFile
import com.ioannuwu.inline2.pluginlogic.editormodel.EditorModelImpl
import com.ioannuwu.inline2.pluginlogic.render.BySettingsTextElementMetricsSelector
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

            val supplier = BySettingsTextElementMetricsSelector(
                ApplicationManager.getApplication().getService(SettingsService::class.java).state
            )

            SettingsChangeDispatcher.subscribe(supplier)

            val markupModelListener =
                ErrorMarkupModelListener(
                    EditorModelImpl(editor), supplier, editor
                )

            listeners.add(markupModelListener)

            markupModel.addMarkupModelListener(fileEditor, markupModelListener)
        }
    }

    companion object {

        private val listeners: MutableList<ErrorMarkupModelListener> = mutableListOf()

        fun updateAllListeners() {
            listeners.forEach { it.updateAllHighlighters() }
        }
    }
}
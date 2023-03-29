package com.ioannuwu.inline

import com.intellij.openapi.editor.ex.MarkupModelEx
import com.intellij.openapi.editor.ex.RangeHighlighterEx
import com.intellij.openapi.editor.impl.DocumentMarkupModel
import com.intellij.openapi.editor.impl.event.MarkupModelListener
import com.intellij.openapi.fileEditor.*
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.Pair
import com.intellij.openapi.vfs.VirtualFile
import com.ioannuwu.inline.data.FontDataProvider
import com.ioannuwu.inline.data.MySettingsService
import com.ioannuwu.inline.domain.*
import com.ioannuwu.inline.domain.settings.SettingsChangeEvent
import com.ioannuwu.inline.domain.settings.SettingsChangeListener
import com.ioannuwu.inline.domain.settings.SettingsChangeObservable
import java.awt.GraphicsEnvironment

class EditorOpenedListenerKt : FileEditorManagerListener, SettingsChangeListener {

    init {
        MySettingsService.OBSERVABLE.subscribe(this, SettingsChangeObservable.Priority.LAST)
    }

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

            val graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment()!!

            val fontDataProvider = FontDataProvider.BySettings(editor, graphicsEnvironment, fileEditor)
            val renderElementsProvider =
                RenderElementsProvider.Impl(renderDataProvider, fontDataProvider, FontDataProvider.ByEditor(editor), NumberOfWhitespacesProvider.BySettings)

            val viewModel = ViewModel.Impl(renderElementsProvider, editor, maxPerLine, renderDataProvider)

            val markupModelListener = MarkupModelListenerKt(EditorCallback.ViewModelEditorCallback(viewModel))

            markupModel.addMarkupModelListener(fileEditor, markupModelListener)
            map[fileEditor] = markupModelListener

            Disposer.register(fileEditor) { map.remove(fileEditor) }
        }
    }

    override fun onSettingsChange(event: SettingsChangeEvent) {
        for ((key, markupModelListener) in map) {
            val editor = key.editor
            val document = editor.document
            val markupModel = DocumentMarkupModel.forDocument(document, editor.project, false)
            val allHighlighters = markupModel.allHighlighters
            for (highlighter in allHighlighters) {
                if (highlighter !is RangeHighlighterEx) return
                markupModelListener.attributesChanged(highlighter, false, true)
            }
        }
    }

    companion object {
        private val map: MutableMap<TextEditor, MarkupModelListener> = HashMap()

        private val renderDataProvider: RenderDataProviderKt = RenderDataProviderKt.BySettings
        private val maxPerLine: MaxErrorsPerLineProvider = MaxErrorsPerLineProvider.BySettings
    }
}
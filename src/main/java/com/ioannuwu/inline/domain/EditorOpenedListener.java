package com.ioannuwu.inline.domain;

import com.intellij.openapi.editor.ex.MarkupModelEx;
import com.intellij.openapi.editor.impl.DocumentMarkupModel;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import com.ioannuwu.inline.data.MySettingsService;
import com.ioannuwu.inline.domain.render.RenderDataProvider;
import org.jetbrains.annotations.NotNull;

public class EditorOpenedListener implements FileEditorManagerListener {

    private final RenderDataProvider renderDataProvider = new RenderDataProvider.BySettings(MySettingsService.getInstance());

    @Override
    public void fileOpenedSync(@NotNull FileEditorManager source, @NotNull VirtualFile file,
                               @NotNull Pair<FileEditor[], FileEditorProvider[]> editors) {
        FileEditor[] fileEditors = editors.first;
        for (FileEditor fileEditor : fileEditors) {
            if (!(fileEditor instanceof TextEditor)) continue;
            TextEditor textEditor = (TextEditor) fileEditor;

            MarkupModel markupModel = DocumentMarkupModel.forDocument(
                    textEditor.getEditor().getDocument(), textEditor.getEditor().getProject(), false);

            if (!(markupModel instanceof MarkupModelEx)) continue;
            MarkupModelEx markupModelEx = (MarkupModelEx) markupModel;
            MyMarkupModelListener markupModelListener = new MyMarkupModelListener(textEditor, renderDataProvider);

            markupModelEx.addMarkupModelListener(() -> {}, markupModelListener);
        }
    }
}
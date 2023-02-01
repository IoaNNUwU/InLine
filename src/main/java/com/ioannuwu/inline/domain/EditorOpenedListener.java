package com.ioannuwu.inline.domain;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ex.MarkupModelEx;
import com.intellij.openapi.editor.ex.RangeHighlighterEx;
import com.intellij.openapi.editor.impl.DocumentMarkupModel;
import com.intellij.openapi.editor.impl.event.MarkupModelListener;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import com.ioannuwu.inline.data.MySettingsService;
import com.ioannuwu.inline.domain.render.RenderDataProvider;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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

            markupModelEx.addMarkupModelListener(textEditor, markupModelListener);
            list.add(new Pair<>(textEditor, markupModelListener));
        }
    }

    private static final ArrayList<Pair<TextEditor, MarkupModelListener>> list = new ArrayList<>();

    public static void updateActiveListeners() {
        for (Pair<TextEditor, MarkupModelListener> pair : list) {
            Editor editor = pair.first.getEditor();
            Document document = editor.getDocument();
            MarkupModelListener listener = pair.second;

            MarkupModel markupModel = DocumentMarkupModel.forDocument(document, editor.getProject(), false);

            RangeHighlighter[] allHighlighters = markupModel.getAllHighlighters();

            for (RangeHighlighter highlighter : allHighlighters) {
                if (!(highlighter instanceof RangeHighlighterEx)) return;
                RangeHighlighterEx highlighterEx = (RangeHighlighterEx) highlighter;
                listener.attributesChanged(highlighterEx, false, false);
            }
        }
    }
}
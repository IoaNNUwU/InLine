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
import com.ioannuwu.inline.domain.utils.MaxPerLine;
import com.ioannuwu.inline.domain.utils.RenderDataProvider;
import com.ioannuwu.inline.domain.utils.RenderElementsProvider;
import com.ioannuwu.inline.domain.utils.modes.CouplePerLineWithHighestPriorityMode;
import com.ioannuwu.inline.domain.utils.modes.Entity;
import com.ioannuwu.inline.domain.utils.modes.EntityComparator;
import com.ioannuwu.inline.domain.utils.modes.Mode;
import com.ioannuwu.inline.ui.render.EditorElementsRenderer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class EditorOpenedListener implements FileEditorManagerListener {

    private static final MySettingsService settingsService = MySettingsService.getInstance();

    private static final GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();

    private static final RenderDataProvider renderDataProvider = new RenderDataProvider.BySettings(settingsService);

    private static final Comparator<Entity> comparator = new EntityComparator.BySeverity()
            .thenComparing(new EntityComparator.ByOffset().reversed());

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

            Mode mode = new CouplePerLineWithHighestPriorityMode(
                    new RenderElementsProvider.BySettings(renderDataProvider, textEditor.getEditor(),
                            settingsService, graphicsEnvironment),
                    new EditorElementsRenderer.Impl(), new MaxPerLine.BySettings(settingsService), comparator);

            ElementsRendererMarkupModelListener markupModelListener = new ElementsRendererMarkupModelListener(mode);

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
                listener.attributesChanged(highlighterEx, false, true);
            }
        }
    }
}
package com.ioannuwu.inline.help;

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
import com.ioannuwu.inline.domain.utils.modes.Mode;
import com.ioannuwu.inline.ui.render.EditorElementsRenderer;
import com.ioannuwu.inline.utils.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class HelpEditorOpenedListener implements FileEditorManagerListener {

    private static final MySettingsService settingsService = MySettingsService.getInstance();

    private static final RenderDataProvider renderDataProvider = new RenderDataProvider.BySettings(settingsService);

    private static final HashMap<TextEditor, MarkupModelListener> map = new HashMap<>();

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
                            settingsService, Utils.GRAPHICS_ENVIRONMENT),
                    new EditorElementsRenderer.Impl(), new MaxPerLine.BySettings(settingsService));

            ElementsRendererMarkupModelListener markupModelListener = new ElementsRendererMarkupModelListener(mode);

            markupModelEx.addMarkupModelListener(textEditor, markupModelListener);
            map.put(textEditor, markupModelListener);
        }
    }

    public static void updateActiveListeners() {
        for (var entry : map.entrySet()) {
            Editor editor = entry.getKey().getEditor();
            Document document = editor.getDocument();
            MarkupModelListener listener = entry.getValue();

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
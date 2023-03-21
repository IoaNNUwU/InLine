package com.ioannuwu.inline.ui.render.elements.graphiccomponents;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.EditorFontType;
import com.intellij.util.ui.UIUtilities;
import com.ioannuwu.inline.data.MySettingsService;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public interface FontData extends FontMetricsProvider, FontProvider {

    @Override
    @NotNull Font font();

    float editorFontSize();

    @Override
    @NotNull FontMetrics fontMetrics();

    int lineHeight();


    class BySettings implements FontData {

        private final MySettingsService settingsService;
        private final Editor editor;
        private final GraphicsEnvironment graphicsEnvironment;

        public BySettings(MySettingsService settingsService, Editor editor, GraphicsEnvironment graphicsEnvironment) {
            this.editor = editor;
            this.settingsService = settingsService;
            this.graphicsEnvironment = graphicsEnvironment;
        }

        @Override
        public @NotNull Font font() { // should use font cache if performance needed
            String fontName = settingsService.getState().font.fontName;
            var allFonts = graphicsEnvironment.getAllFonts();
            for (final var font : allFonts) {
                if (font.getFontName().equals(fontName)) return deriveFont(font);
            }
            for (final var font : allFonts) {
                if (font.getFontName().equals(editor.getColorsScheme().getEditorFontName())) return deriveFont(font);
            }
            return allFonts[0];
        }
        private Font deriveFont(Font font) {
            return (font == null) ? editor.getColorsScheme().getFont(EditorFontType.PLAIN) :
                    font.deriveFont((float) editor.getColorsScheme().getEditorFontSize());
        }

        @Override
        public float editorFontSize() {
            return editor.getColorsScheme().getEditorFontSize();
        }

        @Override
        public @NotNull FontMetrics fontMetrics() {
            return UIUtilities.getFontMetrics(editor.getComponent(), font());
        }

        @Override
        public int lineHeight() {
            return editor.getLineHeight();
        }
    }
}

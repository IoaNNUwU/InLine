package com.ioannuwu.inline.domain;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface RangeHighlighterWrapper {

    @NotNull String getDescription();

    @NotNull HighlightSeverity getSeverity();

    static @Nullable RangeHighlighterWrapper tryFrom(@NotNull RangeHighlighter rangeHighlighter) {
        if (rangeHighlighter.getErrorStripeTooltip() == null) return null;
        if (!(rangeHighlighter.getErrorStripeTooltip() instanceof HighlightInfo)) return null;

        HighlightInfo highlightInfo = (HighlightInfo) rangeHighlighter.getErrorStripeTooltip();

        if (highlightInfo.getDescription() == null || highlightInfo.getDescription().isEmpty()) return null;

        return new Impl(rangeHighlighter);
    }

    class Impl implements RangeHighlighterWrapper {

        private final @NotNull RangeHighlighter rangeHighlighter;

        private final @NotNull String description;

        private final @NotNull HighlightSeverity severity;

        private Impl(@NotNull RangeHighlighter rangeHighlighter) {
            this.rangeHighlighter = rangeHighlighter;
            this.severity = ((HighlightInfo) rangeHighlighter.getErrorStripeTooltip()).getSeverity();
            this.description = ((HighlightInfo) rangeHighlighter.getErrorStripeTooltip()).getDescription();
        }

        @Override
        public @NotNull String getDescription() {
            return this.description;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (!(other instanceof RangeHighlighterWrapper)) return false;
            return this.hashCode() == other.hashCode();
        }

        @Override
        public int hashCode() {
            return 31 * rangeHighlighter.hashCode();
        }

        @Override
        public @NotNull HighlightSeverity getSeverity() {
            return this.severity;
        }
    }
}

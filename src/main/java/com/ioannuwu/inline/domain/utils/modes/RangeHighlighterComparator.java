package com.ioannuwu.inline.domain.utils.modes;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.openapi.editor.markup.RangeHighlighter;

import java.util.Comparator;

public interface RangeHighlighterComparator extends Comparator<RangeHighlighter> {

    Comparator<RangeHighlighter> BY_SEVERITY = (h1, h2) -> {
        HighlightInfo o1Info = ((HighlightInfo) h1.getErrorStripeTooltip());
        HighlightInfo o2Info = ((HighlightInfo) h2.getErrorStripeTooltip());

        return -o1Info.getSeverity().compareTo(o2Info.getSeverity());
    };

    Comparator<RangeHighlighter> BY_DESCRIPTION = (h1, h2) -> {
        HighlightInfo o1Info = ((HighlightInfo) h1.getErrorStripeTooltip());
        HighlightInfo o2Info = ((HighlightInfo) h2.getErrorStripeTooltip());
        return -o1Info.getDescription().compareTo(o2Info.getDescription());
    };

    Comparator<RangeHighlighter> BY_OFFSET = Comparator.comparingInt(RangeMarker::getStartOffset);

    Comparator<RangeHighlighter> BY_OFFSET_REVERSED = BY_OFFSET.reversed();

    Comparator<RangeHighlighter> BY_SEVERITY_THEN_OFFSET_THEN_DESCRIPTION =
            BY_SEVERITY.thenComparing(BY_OFFSET).thenComparing(BY_DESCRIPTION);
}

package com.ioannuwu.inline.domain.utils.modes;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;

import java.util.Comparator;

public interface EntityComparator extends Comparator<Entity> {

    class BySeverity implements EntityComparator {

        @Override
        public int compare(Entity o1, Entity o2) {
            HighlightInfo o1Info = ((HighlightInfo) o1.rangeHighlighter.getErrorStripeTooltip());
            HighlightInfo o2Info = ((HighlightInfo) o2.rangeHighlighter.getErrorStripeTooltip());

            return o2Info.getSeverity().compareTo(o1Info.getSeverity());
        }
    }

    class ByOffset implements EntityComparator {

        @Override
        public int compare(Entity o1, Entity o2) {
            return o1.rangeHighlighter.getStartOffset() - o2.rangeHighlighter.getStartOffset();
        }
    }
}

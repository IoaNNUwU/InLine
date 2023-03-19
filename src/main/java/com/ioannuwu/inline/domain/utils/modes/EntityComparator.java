package com.ioannuwu.inline.domain.utils.modes;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;

import java.util.Comparator;

public interface EntityComparator extends Comparator<Entity> {

    class BySeverity implements EntityComparator {

        @Override
        public int compare(Entity o1, Entity o2) {
            HighlightInfo o1Info = ((HighlightInfo) o1.rangeHighlighter.getErrorStripeTooltip());
            HighlightInfo o2Info = ((HighlightInfo) o2.rangeHighlighter.getErrorStripeTooltip());

            return -o1Info.getSeverity().compareTo(o2Info.getSeverity());
        }
    }

    class ByOffset implements EntityComparator {

        @Override
        public int compare(Entity o1, Entity o2) {
            return Integer.compare(
                    o1.rangeHighlighter.getStartOffset(),
                    o2.rangeHighlighter.getStartOffset());
        }
    }

    class ByDescription implements EntityComparator {

        @Override
        public int compare(Entity o1, Entity o2) {
            HighlightInfo o1Info = ((HighlightInfo) o1.rangeHighlighter.getErrorStripeTooltip());
            HighlightInfo o2Info = ((HighlightInfo) o2.rangeHighlighter.getErrorStripeTooltip());

            return o2Info.getDescription().compareTo(o1Info.getDescription());
        }
    }
}

package com.ioannuwu.inline.wrapper

interface WrapperComparator : Comparator<RangeHighlighterWrapper> {

    override fun compare(h1: RangeHighlighterWrapper, h2: RangeHighlighterWrapper): Int

    object BySeverity : WrapperComparator {
        override fun compare(h1: RangeHighlighterWrapper, h2: RangeHighlighterWrapper) =
            h1.priority.compareTo(h2.priority)
    }

    object ByDescription : WrapperComparator {
        override fun compare(h1: RangeHighlighterWrapper, h2: RangeHighlighterWrapper) =
            h1.description.compareTo(h2.description)
    }

    object ByOffset : WrapperComparator {
        override fun compare(h1: RangeHighlighterWrapper, h2: RangeHighlighterWrapper) =
            h1.offset.compareTo(h2.offset)
    }

    object ByOffsetReversed : WrapperComparator {

        private val comp = ByOffset.reversed()

        override fun compare(h1: RangeHighlighterWrapper, h2: RangeHighlighterWrapper) =
            comp.compare(h1, h2)
    }

    object BySeverityThenOffsetThenDescription : WrapperComparator {

        private val comp = BySeverity then ByOffset then ByDescription

        override fun compare(h1: RangeHighlighterWrapper, h2: RangeHighlighterWrapper) =
            comp.compare(h1, h2)
    }
}
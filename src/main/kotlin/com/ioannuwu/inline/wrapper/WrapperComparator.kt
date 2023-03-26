package com.ioannuwu.inline.wrapper

interface WrapperComparator : Comparator<RangeHighlighterWrapper> {

    override fun compare(h1: RangeHighlighterWrapper, h2: RangeHighlighterWrapper): Int

    object ByPriority : WrapperComparator {
        override fun compare(h1: RangeHighlighterWrapper, h2: RangeHighlighterWrapper) =
            -h1.priority.compareTo(h2.priority)
    }

    object ByDescription : WrapperComparator {
        override fun compare(h1: RangeHighlighterWrapper, h2: RangeHighlighterWrapper) =
            h1.description.compareTo(h2.description)
    }

    /**
     * lowest offset is first in the list
     */
    object ByOffsetLowestIsFirstLikeOnTheLine : WrapperComparator {

        override fun compare(h1: RangeHighlighterWrapper, h2: RangeHighlighterWrapper) =
            h1.offset.compareTo(h2.offset)
    }

    /**
     * lowest offset is last in the list
     */
    object ByOffsetLowestIsLast : WrapperComparator {

        private val comp = ByOffsetLowestIsFirstLikeOnTheLine.reversed()

        override fun compare(h1: RangeHighlighterWrapper, h2: RangeHighlighterWrapper) =
            comp.compare(h1, h2)
    }
}
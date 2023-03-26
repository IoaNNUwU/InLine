package com.ioannuwu.inline.domain

import com.ioannuwu.inline.utils.TEST
import com.ioannuwu.inline.domain.wrapper.RangeHighlighterWrapper
import com.ioannuwu.inline.domain.wrapper.WrapperComparator
import org.junit.Test

class WrapperComparatorTest {

    private val testValues: List<RangeHighlighterWrapper> =
        listOf(OkWrapper, BadWrapper, GoodWrapper, BadWrapper, GoodWrapper)

    @Test
    fun `we should take from line end`() {
        val wrapper1 = TestWrapper(100, " ", 10)
        val wrapper2 = TestWrapper(100, " ", 9)
        val wrapper3 = TestWrapper(100, " ", 8)
        val wrapper4 = TestWrapper(100, " ", 7)
        val wrapper5 = TestWrapper(50, " ", 6)
        val wrapper6 = TestWrapper(50, " ", 11)

        val testValues2 = listOf(wrapper1, wrapper6, wrapper3, wrapper2, wrapper5, wrapper4)

        val sorted1 = testValues2.asSequence()
            .sortedWith(WrapperComparator.ByPriority then WrapperComparator.ByOffsetLowestIsLast)
            .take(3)
            .toList()

        for (w in sorted1) assert(w.priority == 100)

        assert(sorted1[0].offset == 10)
        assert(sorted1[1].offset == 9)
        assert(sorted1[2].offset == 8)
    }

    @Test
    fun `greater priority should be first in list`() {
        val sortedList = testValues.sortedWith(WrapperComparator.ByPriority)

        assert(sortedList[0] === GoodWrapper)
        assert(sortedList[1] === GoodWrapper)
        assert(sortedList[2] === OkWrapper)
        assert(sortedList[3] === BadWrapper)
        assert(sortedList[4] === BadWrapper)
    }

    @Test
    fun `first in alphabet should be first in the list`() {
        val sortedList = testValues.sortedWith(WrapperComparator.ByDescription)

        assert(sortedList[0] === GoodWrapper)
        assert(sortedList[1] === GoodWrapper)
        assert(sortedList[2] === OkWrapper)
        assert(sortedList[3] === BadWrapper)
        assert(sortedList[4] === BadWrapper)
    }

    @Test
    fun `lowest is last`() {
        val sortedList = testValues
            .sortedWith(WrapperComparator.ByOffsetLowestIsLast)

        assert(sortedList[0] === GoodWrapper)
        assert(sortedList[1] === GoodWrapper)
        assert(sortedList[2] === OkWrapper)
        assert(sortedList[3] === BadWrapper)
        assert(sortedList[4] === BadWrapper)
    }

    @Test
    fun `lowest is first`() {
        val sortedList = testValues
            .sortedWith(WrapperComparator.ByOffsetLowestIsFirstLikeOnTheLine)

        assert(sortedList[0] === BadWrapper)
        assert(sortedList[1] === BadWrapper)
        assert(sortedList[2] === OkWrapper)
        assert(sortedList[3] === GoodWrapper)
        assert(sortedList[4] === GoodWrapper)
    }
}

private val END_OF_ALPHABET = "zzz"
private val MIDDLE_OF_APLHABET = "ggg"
private val FIRST_IN_ALPHABET = "aaa"

private object BadWrapper : RangeHighlighterWrapper {

    override val priority: Int = 10
    override val description: String = END_OF_ALPHABET
    override val offset: Int = 10

    override val lineNumber: Int get() = TEST()

    override fun isSufficient(): Boolean = TEST()
    override fun isValidInDocument(): Boolean = TEST()
}

private object OkWrapper : RangeHighlighterWrapper {
    override val priority: Int = 300
    override val description: String = MIDDLE_OF_APLHABET
    override val offset: Int = 300

    override val lineNumber: Int get() = TEST()

    override fun isSufficient(): Boolean = TEST()
    override fun isValidInDocument(): Boolean = TEST()
}

private object GoodWrapper : RangeHighlighterWrapper {

    override val priority: Int = 1000
    override val description: String = FIRST_IN_ALPHABET
    override val offset: Int = 1000

    override val lineNumber: Int get() = TEST()

    override fun isSufficient(): Boolean = TEST()
    override fun isValidInDocument(): Boolean = TEST()
}

private class TestWrapper(
    override val priority: Int,
    override val description: String,
    override val offset: Int
) : RangeHighlighterWrapper {


    override val lineNumber: Int get() = TEST()

    override fun isSufficient(): Boolean = TEST()
    override fun isValidInDocument(): Boolean = TEST()
}
package com.ioannuwu.inline.utils.simpletests

import org.junit.Test

class SortingTest {

    @Test
    fun `objects with lowest priorities should be rendered first`() {
        val list = listOf(
            LowPriorityObject, HighPriorityObject, MidPriorityObject, LowPriorityObject,
            HighPriorityObject, MidPriorityObject, LowPriorityObject, LowPriorityObject,
        )

        val sortedList = list.sortedBy { it.priority }
        assert(sortedList.first().priority == -100)
        assert(sortedList.last().priority == 100)
    }
}

private object LowPriorityObject : SortingHelper {
    override val priority: Int = -100
}

private object HighPriorityObject : SortingHelper {
    override val priority: Int = 100
}

private object MidPriorityObject : SortingHelper {
    override val priority: Int = 10
}

private interface SortingHelper {
    val priority: Int
}
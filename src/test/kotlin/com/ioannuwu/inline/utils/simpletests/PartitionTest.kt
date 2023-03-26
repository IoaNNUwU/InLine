package com.ioannuwu.inline.utils.simpletests

import org.junit.Test
import kotlin.test.assertTrue

class PartitionTest {

    @Test
    fun partitionShouldDoItsWork() {
        val list = listOf(UglyObject, UglyObject, FancyObject)

        val (fancy, ugly) = list.partition(PartitionHelper::isFancy)

        assertTrue { fancy.count() == 1 }
        assertTrue { ugly.count() == 2 }
    }
}

private object FancyObject : PartitionHelper {
    override val isFancy = true
}

private object UglyObject : PartitionHelper {
    override val isFancy = false
}

private interface PartitionHelper {
    val isFancy: Boolean
}
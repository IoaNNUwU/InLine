package com.ioannuwu.inline.utils.simpletests

import com.ioannuwu.inline.utils.TEST
import org.junit.Test
import kotlin.test.assertFails

class TestingUtilsTest {

    @Test
    fun `object should be able to use foo`() {
        assert(ObjectUsesFoo.foo())
    }

    @Test
    fun `using bar should crash`() {
        assertFails { ObjectUsesFoo.bar() }
    }
}

private object ObjectUsesFoo : TestingUtilsHelper {
    override fun foo() = true
    override fun bar() = TEST()
}

private interface TestingUtilsHelper {
    fun foo(): Boolean
    fun bar(): Boolean
}

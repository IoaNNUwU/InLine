package com.ioannuwu.inline.utils

fun TEST(): Nothing {
    throw Error("Should not be used in the test")
}

fun TEST(description: String): Nothing {
    throw Error("Should not be reachable in tests: $description")
}
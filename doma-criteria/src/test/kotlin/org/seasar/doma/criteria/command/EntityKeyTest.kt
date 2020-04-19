package org.seasar.doma.criteria.command

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class EntityKeyTest {

    @Test
    fun test() {
        val key1 = EntityKey(listOf(1, "a"))
        val key2 = EntityKey(listOf(1, "a"))
        assertTrue(key1 == key2)
    }
}

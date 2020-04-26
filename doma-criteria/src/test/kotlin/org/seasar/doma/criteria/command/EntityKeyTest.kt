package org.seasar.doma.criteria.command

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class EntityKeyTest {

    @Test
    fun test() {
        val key1 = EntityKey(listOf(1, "a"))
        val key2 = EntityKey(listOf(1, "a"))
        assertEquals(key1, key2)
    }
}

package org.seasar.doma.it.criteria

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.seasar.doma.it.IntegrationTestEnvironment
import org.seasar.doma.jdbc.Config
import org.seasar.doma.kotlin.jdbc.criteria.KEntityql

@ExtendWith(IntegrationTestEnvironment::class)
class KEntityqlBatchDeleteTest(config: Config) {

    private val entityql = KEntityql(config)

    @Test
    fun test() {
        val e = Employee_()
        val employees = entityql.from(e).where { `in`(e.employeeId, listOf(5, 6)) }.fetch()
        val result = entityql.delete(e, employees).execute()
        assertArrayEquals(intArrayOf(1, 1), result.counts)
        assertEquals(employees, result.entities)
        val employees2 = entityql.from(e).where { `in`(e.employeeId, listOf(5, 6)) }.fetch()
        assertTrue(employees2.isEmpty())
    }

    @Test
    fun suppressOptimisticLockException() {
        val e = Employee_()
        val employees = entityql.from(e).where { `in`(e.employeeId, listOf(5, 6)) }.fetch()
        employees.forEach { it: Employee -> it.employeeId = 100 }
        val result = entityql
            .delete(e, employees) { suppressOptimisticLockException = true }
            .execute()
        assertArrayEquals(intArrayOf(0, 0), result.counts)
    }

    @Test
    fun empty() {
        val e = Employee_()
        val result = entityql.delete(e, emptyList()).execute()
        assertTrue(result.entities.isEmpty())
    }
}

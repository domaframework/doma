package org.seasar.doma.it.criteria

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.seasar.doma.it.IntegrationTestEnvironment
import org.seasar.doma.jdbc.Config
import org.seasar.doma.kotlin.jdbc.criteria.KEntityql

@ExtendWith(IntegrationTestEnvironment::class)
class KEntityqlDeleteTest(config: Config) {
    private val entityql = KEntityql(config)

    @Test
    fun test() {
        val e = Employee_()
        val employee = entityql.from(e).where { eq(e.employeeId, 5) }.fetchOne()
        val result = entityql.delete(e, employee).execute()
        assertEquals(1, result.count)
        assertEquals(employee, result.entity)
        val employees = entityql.from(e).where { eq(e.employeeId, 5) }.fetch()
        assertTrue(employees.isEmpty())
    }

    @Test
    fun suppressOptimisticLockException() {
        val e = Employee_()
        val employee = entityql.from(e).where { eq(e.employeeId, 5) }.fetchOne()
        checkNotNull(employee)
        employee.employeeId = 100
        val result = entityql
            .delete(e, employee, { suppressOptimisticLockException = true })
            .execute()
        assertEquals(0, result.count)
    }
}

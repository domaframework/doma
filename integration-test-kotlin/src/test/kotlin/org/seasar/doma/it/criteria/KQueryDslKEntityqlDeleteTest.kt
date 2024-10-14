package org.seasar.doma.it.criteria

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.seasar.doma.it.IntegrationTestEnvironment
import org.seasar.doma.jdbc.Config
import org.seasar.doma.kotlin.jdbc.criteria.KQueryDsl

@ExtendWith(IntegrationTestEnvironment::class)
class KQueryDslKEntityqlDeleteTest(config: Config) {
    private val dsl = KQueryDsl(config)

    @Test
    fun test() {
        val e = Employee_()
        val employee = dsl.from(e).where { eq(e.employeeId, 5) }.fetchOne()
        val result = dsl.delete(e).single(employee).execute()
        assertEquals(1, result.count)
        assertEquals(employee, result.entity)
        val employees = dsl.from(e).where { eq(e.employeeId, 5) }.fetch()
        assertTrue(employees.isEmpty())
    }

    @Test
    fun suppressOptimisticLockException() {
        val e = Employee_()
        val employee = dsl.from(e).where { eq(e.employeeId, 5) }.fetchOne()
        checkNotNull(employee)
        employee.employeeId = 100
        val result = dsl
            .delete(e) { suppressOptimisticLockException = true }.single(employee)
            .execute()
        assertEquals(0, result.count)
    }
}

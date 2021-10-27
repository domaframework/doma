package org.seasar.doma.it.criteria

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.seasar.doma.it.IntegrationTestEnvironment
import org.seasar.doma.jdbc.Config
import org.seasar.doma.kotlin.jdbc.criteria.KEntityql
import java.math.BigDecimal

@ExtendWith(IntegrationTestEnvironment::class)
class KEntityqlUpdateTest(config: Config) {

    private val entityql = KEntityql(config)

    @Test
    fun test() {
        val e = Employee_()
        val employee = entityql.from(e).where { eq(e.employeeId, 5) }.fetchOne()
        checkNotNull(employee)
        employee.employeeName = "aaa"
        employee.salary = Salary("2000")

        val result = entityql.update(e, employee).execute()
        assertEquals(1, result.count)
        assertEquals(employee, result.entity)

        val employee2 = entityql.from(e).where { eq(e.employeeId, 5) }.fetchOne()
        checkNotNull(employee2)
        assertEquals("aaa", employee2.employeeName)
        assertEquals(0, BigDecimal("2000").compareTo(employee2.salary?.value))
    }

    @Test
    fun suppressOptimisticLockException() {
        val e = Employee_()
        val employee = entityql.from(e).where { eq(e.employeeId, 5) }.fetchOne()
        checkNotNull(employee)
        employee.employeeId = 100
        employee.employeeName = "aaa"
        val result = entityql
            .update(e, employee, { suppressOptimisticLockException = true })
            .execute()
        assertEquals(0, result.count)
    }
}

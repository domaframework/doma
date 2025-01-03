/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.it.criteria

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.seasar.doma.it.IntegrationTestEnvironment
import org.seasar.doma.jdbc.Config
import org.seasar.doma.kotlin.jdbc.criteria.KQueryDsl
import java.math.BigDecimal

@ExtendWith(IntegrationTestEnvironment::class)
class KQueryDslEntityUpdateTest(config: Config) {

    private val dsl = KQueryDsl(config)

    @Test
    fun test() {
        val e = Employee_()
        val employee = dsl.from(e).where { eq(e.employeeId, 5) }.fetchOne()
        checkNotNull(employee)
        employee.employeeName = "aaa"
        employee.salary = Salary("2000")

        val result = dsl.update(e).single(employee).execute()
        assertEquals(1, result.count)
        assertEquals(employee, result.entity)

        val employee2 = dsl.from(e).where { eq(e.employeeId, 5) }.fetchOne()
        checkNotNull(employee2)
        assertEquals("aaa", employee2.employeeName)
        assertEquals(0, BigDecimal("2000").compareTo(employee2.salary?.value))
    }

    @Test
    fun suppressOptimisticLockException() {
        val e = Employee_()
        val employee = dsl.from(e).where { eq(e.employeeId, 5) }.fetchOne()
        checkNotNull(employee)
        employee.employeeId = 100
        employee.employeeName = "aaa"
        val result = dsl
            .update(e) { suppressOptimisticLockException = true }.single(employee)
            .execute()
        assertEquals(0, result.count)
    }
}

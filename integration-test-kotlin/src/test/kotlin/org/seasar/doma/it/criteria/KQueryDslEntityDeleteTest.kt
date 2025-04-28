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
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.extension.ExtendWith
import org.seasar.doma.it.Dbms
import org.seasar.doma.it.IntegrationTestEnvironment
import org.seasar.doma.it.Run
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.Result
import org.seasar.doma.kotlin.jdbc.criteria.KQueryDsl

@ExtendWith(IntegrationTestEnvironment::class)
class KQueryDslEntityDeleteTest(config: Config) {
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

    @Test
    @Run(unless = [Dbms.MYSQL, Dbms.MYSQL8, Dbms.ORACLE])
    fun returning() {
        val e = Employee_()

        val employee =
            dsl.from(e).where { eq(e.employeeId, 5) }.fetchOne()

        val result: Result<Employee> = dsl.delete(e).single(employee).returning().execute()
        assertEquals(1, result.count)
        assertNotEquals(employee, result.entity)

        val resultEntity = result.entity
        assertEquals(5, resultEntity.employeeId)

        val entity =
            dsl.from(e).where { eq(e.employeeId, 5) }.fetchOneOrNull()
        assertNull(entity)
    }
}

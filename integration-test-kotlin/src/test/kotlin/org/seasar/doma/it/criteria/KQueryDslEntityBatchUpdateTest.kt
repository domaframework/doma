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

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.seasar.doma.it.IntegrationTestEnvironment
import org.seasar.doma.jdbc.Config
import org.seasar.doma.kotlin.jdbc.criteria.KQueryDsl

@ExtendWith(IntegrationTestEnvironment::class)
class KQueryDslEntityBatchUpdateTest(config: Config) {

    private val dsl = KQueryDsl(config)

    @Test
    fun test() {
        val e = Employee_()
        val employees = dsl.from(e).where { `in`(e.employeeId, listOf(5, 6)) }.fetch()
        employees.forEach { it.employeeName = "aaa" }
        val result = dsl.update(e).batch(employees).execute()
        assertArrayEquals(intArrayOf(1, 1), result.counts)
        Assertions.assertEquals(employees, result.entities)
        val employees2 = dsl.from(e).where { `in`(e.employeeId, listOf(5, 6)) }.fetch()
        assertTrue(employees2.all { "aaa" == it.employeeName })
    }

    @Test
    fun suppressOptimisticLockException() {
        val e = Employee_()
        val employees = dsl.from(e).where { `in`(e.employeeId, listOf(5, 6)) }.fetch()
        employees.forEach { it: Employee ->
            it.employeeId = 100
            it.employeeName = "aaa"
        }
        val result = dsl
            .update(e) { suppressOptimisticLockException = true }.batch(employees)
            .execute()
        assertArrayEquals(intArrayOf(0, 0), result.counts)
    }

    @Test
    fun empty() {
        val e = Employee_()
        val result = dsl.update(e).batch(emptyList()).execute()
        assertTrue(result.entities.isEmpty())
    }
}

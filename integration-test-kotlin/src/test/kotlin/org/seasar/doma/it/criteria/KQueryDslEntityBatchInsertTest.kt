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
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.seasar.doma.it.IntegrationTestEnvironment
import org.seasar.doma.jdbc.Config
import org.seasar.doma.kotlin.jdbc.criteria.KQueryDsl

@ExtendWith(IntegrationTestEnvironment::class)
class KQueryDslEntityBatchInsertTest(config: Config) {

    private val dsl = KQueryDsl(config)

    @Test
    fun test() {
        val d = Department_()
        val department = Department()
        department.departmentId = 99
        department.departmentNo = 99
        department.departmentName = "aaa"
        department.location = "bbb"

        val department2 = Department()
        department2.departmentId = 100
        department2.departmentNo = 100
        department2.departmentName = "ccc"
        department2.location = "ddd"

        val departments = listOf(department, department2)
        val result = dsl.insert(d).batch(departments).execute()
        assertEquals(departments, result.entities)

        val ids = departments.mapNotNull { it.departmentId }
        val departments2 = dsl
            .from(d)
            .where { `in`(d.departmentId, ids) }
            .orderBy { asc(d.departmentId) }
            .fetch()
        assertEquals(2, departments2.size)
    }

    @Test
    fun empty() {
        val e = Employee_()
        val result = dsl.insert(e).batch(emptyList()).execute()
        assertTrue(result.entities.isEmpty())
    }
}

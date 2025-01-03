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
class KQueryDslImmutableTest(config: Config) {

    private val dsl = KQueryDsl(config)

    @Test
    fun fetch() {
        val e = Emp_()
        val list = dsl.from(e).fetch()
        assertEquals(14, list.size)
    }

    @Test
    fun associateWith() {
        val e = Emp_()
        val m = Emp_()
        val d = Dept_()
        val list = dsl
            .from(e)
            .innerJoin(d) { eq(e.departmentId, d.departmentId) }
            .leftJoin(m) { eq(e.managerId, m.employeeId) }
            .where { eq(d.departmentName, "SALES") }
            .associateWith(e, d) { emp, dept -> emp.copy(department = dept) }
            .associateWith(e, m) { emp, manager -> emp.copy(manager = manager) }
            .fetch()
        assertEquals(6, list.size)
        assertTrue(list.all { it.department?.departmentName == "SALES" })
        assertTrue(list.all { it.manager != null })
    }
}

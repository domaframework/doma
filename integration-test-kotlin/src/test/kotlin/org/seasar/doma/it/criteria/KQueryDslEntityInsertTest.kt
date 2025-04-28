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
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.extension.ExtendWith
import org.seasar.doma.it.Dbms
import org.seasar.doma.it.IntegrationTestEnvironment
import org.seasar.doma.it.Run
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.Result
import org.seasar.doma.kotlin.jdbc.criteria.KQueryDsl

@ExtendWith(IntegrationTestEnvironment::class)
class KQueryDslEntityInsertTest(config: Config) {

    private val dsl = KQueryDsl(config)

    @Test
    fun test() {
        val d = Department_()
        val department = Department()
        department.departmentId = 99
        department.departmentNo = 99
        department.departmentName = "aaa"
        department.location = "bbb"
        val result = dsl.insert(d).single(department).execute()
        assertEquals(department, result.entity)
        val department2 = dsl.from(d).where { eq(d.departmentId, department.departmentId) }.fetchOne()
        checkNotNull(department2)
        assertEquals("aaa", department2.departmentName)
    }

    @Test
    @Run(unless = [Dbms.MYSQL, Dbms.MYSQL8, Dbms.ORACLE])
    fun returning() {
        val d = Department_()

        val department = Department()
        department.departmentId = 99
        department.departmentNo = 99
        department.departmentName = "aaa"
        department.location = "bbb"

        val result: Result<Department> = dsl.insert(d).single(department).returning().execute()
        assertNotEquals(department, result.getEntity())
        assertEquals(1, result.count)
        val resultEntity = result.getEntity()
        assertEquals(department.departmentId, resultEntity.departmentId)
        assertEquals(department.departmentNo, resultEntity.departmentNo)
        assertEquals(department.departmentName, resultEntity.departmentName)
        assertEquals(department.location, resultEntity.location)
        assertEquals(1, resultEntity.version)

        val department2 =
            dsl.from(d)
                .where { eq(d.departmentId, department.departmentId) }
                .fetchOne()
        assertNotNull(department2)
        assertEquals("aaa", department2.departmentName)
    }
}

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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.seasar.doma.it.Dbms
import org.seasar.doma.it.IntegrationTestEnvironment
import org.seasar.doma.it.Run
import org.seasar.doma.jdbc.Config
import org.seasar.doma.kotlin.jdbc.criteria.KQueryDsl

@ExtendWith(IntegrationTestEnvironment::class)
class KQueryDslEntityMultiInsertTest(config: Config) {

    private val dsl = KQueryDsl(config)

    @Test
    @Run(unless = [Dbms.MYSQL, Dbms.MYSQL8, Dbms.ORACLE])
    fun returning() {
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

        val result = dsl.insert(d).multi(departments).returning().execute()
        assertNotEquals(departments, result.entities)
        Assertions.assertEquals(2, result.count)

        val entity1 = result.getEntities().get(0)
        Assertions.assertEquals(99, entity1.departmentId)
        Assertions.assertEquals(99, entity1.departmentNo)
        assertEquals("aaa", entity1.departmentName)
        assertEquals("bbb", entity1.location)
        Assertions.assertEquals(1, entity1.version)

        val entity2 = result.getEntities().get(1)
        Assertions.assertEquals(100, entity2.departmentId)
        Assertions.assertEquals(100, entity2.departmentNo)
        assertEquals("ccc", entity2.departmentName)
        assertEquals("ddd", entity2.location)
        Assertions.assertEquals(1, entity2.version)
    }
}

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
package org.seasar.doma.jdbc.criteria

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.seasar.doma.jdbc.criteria.entity.Emp
import org.seasar.doma.jdbc.criteria.entity.Emp_
import org.seasar.doma.jdbc.criteria.mock.MockConfig
import java.math.BigDecimal

internal class KQueryDslEntityqlUpdateTest {

    private val entityql = org.seasar.doma.kotlin.jdbc.criteria.KQueryDsl(MockConfig())

    @Test
    fun test() {
        val emp = Emp()
        emp.id = 1
        emp.name = "aaa"
        emp.salary = BigDecimal("1000")
        emp.version = 1
        val e = Emp_()
        val stmt = entityql.update(e).single(emp)
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "update EMP set NAME = 'aaa', SALARY = 1000, VERSION = 1 + 1 where ID = 1 and VERSION = 1",
            sql.formattedSql,
        )
    }

    @Test
    fun ignoreVersion() {
        val emp = Emp()
        emp.id = 1
        emp.name = "aaa"
        emp.salary = BigDecimal("1000")
        emp.version = 1
        val e = Emp_()
        val stmt = entityql.update(e) { ignoreVersion = true }.single(emp)
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "update EMP set NAME = 'aaa', SALARY = 1000, VERSION = 1 where ID = 1",
            sql.formattedSql,
        )
    }

    @Test
    fun excludeNull() {
        val emp = Emp()
        emp.id = 1
        emp.name = null
        emp.salary = BigDecimal("1000")
        emp.version = 1
        val e = Emp_()
        val stmt = entityql.update(e) { excludeNull = true }.single(emp)
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "update EMP set SALARY = 1000, VERSION = 1 + 1 where ID = 1 and VERSION = 1",
            sql.formattedSql,
        )
    }
}

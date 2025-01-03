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

internal class KQueryDslEntityInsertTest {
    private val config = MockConfig().apply {
        dialect = org.seasar.doma.jdbc.dialect.PostgresDialect()
    }

    private val dsl = org.seasar.doma.kotlin.jdbc.criteria.KQueryDsl(config)

    @Test
    fun insertInto() {
        val emp = Emp()
        emp.id = 1
        emp.name = "aaa"
        emp.salary = BigDecimal("1000")
        emp.version = 1
        val e = Emp_()
        val stmt = dsl.insert(e).single(emp)
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "insert into EMP (ID, NAME, SALARY, VERSION) values (1, 'aaa', 1000, 1)",
            sql.formattedSql,
        )
    }

    @Test
    fun insertOnDuplicateKeyUpdate() {
        val emp = Emp()
        emp.id = 1
        emp.name = "aaa"
        emp.salary = BigDecimal("1000")
        emp.version = 1
        val e = Emp_()
        val stmt = dsl.insert(e).single(emp).onDuplicateKeyUpdate()
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "insert into EMP as target (ID, NAME, SALARY, VERSION) values (1, 'aaa', 1000, 1) on conflict (ID) do update set NAME = excluded.NAME, SALARY = excluded.SALARY, VERSION = excluded.VERSION",
            sql.formattedSql,
        )
    }

    @Test
    fun insertOnDuplicateKeyIgnore() {
        val emp = Emp()
        emp.id = 1
        emp.name = "aaa"
        emp.salary = BigDecimal("1000")
        emp.version = 1
        val e = Emp_()
        val stmt = dsl.insert(e).single(emp).onDuplicateKeyIgnore()
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "insert into EMP as target (ID, NAME, SALARY, VERSION) values (1, 'aaa', 1000, 1) on conflict do nothing",
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
        val stmt = dsl.insert(e) { excludeNull = true }.single(emp)
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "insert into EMP (ID, SALARY, VERSION) values (1, 1000, 1)",
            sql.formattedSql,
        )
    }
}

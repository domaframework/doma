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

internal class KQueryDslEntityMultiInsertTest {
    private val config = MockConfig().apply {
        dialect = org.seasar.doma.jdbc.dialect.PostgresDialect()
    }

    private val dsl = org.seasar.doma.kotlin.jdbc.criteria.KQueryDsl(config)

    @Test
    fun insert() {
        val emp = Emp()
        emp.id = 1
        emp.name = "aaa"
        emp.salary = BigDecimal("1000")
        emp.version = 1

        val emp2 = Emp()
        emp2.id = 2
        emp2.name = "bbb"
        emp2.salary = BigDecimal("2000")
        emp2.version = 1

        val e = Emp_()
        val stmt = dsl.insert(e).multi(listOf(emp, emp2))
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "insert into EMP (ID, NAME, SALARY, VERSION) values (1, 'aaa', 1000, 1), (2, 'bbb', 2000, 1)",
            sql.formattedSql,
        )
    }

    private val items = listOf(
        Emp().apply {
            id = 1
            name = "aaa"
            salary = BigDecimal("1000")
            version = 1
        },
        Emp().apply {
            id = 2
            name = "eee"
            salary = BigDecimal("2000")
            version = 2
        },
    )

    @Test
    fun onDuplicateKeyUpdate() {
        val e = Emp_()
        val stmt = dsl.insert(e).multi(items).onDuplicateKeyUpdate()
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "insert into EMP as target (ID, NAME, SALARY, VERSION) values (1, 'aaa', 1000, 1), (2, 'eee', 2000, 2) on conflict (ID) do update set NAME = excluded.NAME, SALARY = excluded.SALARY, VERSION = excluded.VERSION",
            sql.formattedSql,
        )
    }

    @Test
    fun onDuplicateKeyIgnore() {
        val e = Emp_()
        val stmt = dsl.insert(e).multi(items).onDuplicateKeyIgnore()
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "insert into EMP as target (ID, NAME, SALARY, VERSION) values (1, 'aaa', 1000, 1), (2, 'eee', 2000, 2) on conflict do nothing",
            sql.formattedSql,
        )
    }
}

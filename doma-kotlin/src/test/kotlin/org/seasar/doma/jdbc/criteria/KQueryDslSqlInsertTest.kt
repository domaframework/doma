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
import org.seasar.doma.jdbc.criteria.entity.Dept_
import org.seasar.doma.jdbc.criteria.entity.Emp_
import org.seasar.doma.jdbc.criteria.mock.MockConfig

internal class KQueryDslSqlInsertTest {
    private val config = MockConfig().apply {
        dialect = org.seasar.doma.jdbc.dialect.PostgresDialect()
    }

    private val dsl = org.seasar.doma.kotlin.jdbc.criteria.KQueryDsl(config)

    @Test
    fun insert() {
        val e = Emp_()
        val stmt = dsl
            .insert(e)
            .values {
                value(e.id, 99)
                value(e.name, "aaa")
                value(e.salary, null)
                value(e.version, 1)
            }
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "insert into EMP (ID, NAME, SALARY, VERSION) values (99, 'aaa', null, 1)",
            sql.formattedSql,
        )
    }

    @Test
    fun insertOnDuplicateKeyUpdate() {
        val e = Emp_()
        val stmt = dsl
            .insert(e)
            .values {
                value(e.id, 99)
                value(e.name, "aaa")
                value(e.salary, null)
                value(e.version, 1)
            }
            .onDuplicateKeyUpdate()
            .keys(e.id)
            .set {
                it.value(e.name, "bbb")
                it.value(e.salary, it.excluded(e.salary))
            }
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "insert into EMP as target (ID, NAME, SALARY, VERSION) values (99, 'aaa', null, 1) on conflict (ID) do update set NAME = 'bbb', SALARY = excluded.SALARY",
            sql.formattedSql,
        )
    }

    @Test
    fun insertOnDuplicateKeyUpdate_unsetKeys() {
        val e = Emp_()
        val stmt = dsl
            .insert(e)
            .values {
                value(e.id, 99)
                value(e.name, "aaa")
                value(e.salary, null)
                value(e.version, 1)
            }
            .onDuplicateKeyUpdate()
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "insert into EMP as target (ID, NAME, SALARY, VERSION) values (99, 'aaa', null, 1) on conflict (ID) do update set NAME = excluded.NAME, SALARY = excluded.SALARY, VERSION = excluded.VERSION",
            sql.formattedSql,
        )
    }

    @Test
    fun insertOnDuplicateKeyUpdate_unsetSet() {
        val e = Emp_()
        val stmt = dsl
            .insert(e)
            .values {
                value(e.id, 99)
                value(e.name, "aaa")
                value(e.salary, null)
                value(e.version, 1)
            }
            .onDuplicateKeyUpdate()
            .keys(e.id)
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "insert into EMP as target (ID, NAME, SALARY, VERSION) values (99, 'aaa', null, 1) on conflict (ID) do update set NAME = excluded.NAME, SALARY = excluded.SALARY, VERSION = excluded.VERSION",
            sql.formattedSql,
        )
    }

    @Test
    fun insertOnDuplicateKeyIgnore() {
        val e = Emp_()
        val stmt = dsl
            .insert(e)
            .values {
                value(e.id, 99)
                value(e.name, "aaa")
                value(e.salary, null)
                value(e.version, 1)
            }
            .onDuplicateKeyIgnore()
            .keys(e.id)
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "insert into EMP as target (ID, NAME, SALARY, VERSION) values (99, 'aaa', null, 1) on conflict (ID) do nothing",
            sql.formattedSql,
        )
    }

    @Test
    fun insertOnDuplicateKeyIgnore_unsetKeys() {
        val e = Emp_()
        val stmt = dsl
            .insert(e)
            .values {
                value(e.id, 99)
                value(e.name, "aaa")
                value(e.salary, null)
                value(e.version, 1)
            }
            .onDuplicateKeyIgnore()
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "insert into EMP as target (ID, NAME, SALARY, VERSION) values (99, 'aaa', null, 1) on conflict do nothing",
            sql.formattedSql,
        )
    }

    @Test
    fun insert_select_entity() {
        val e = Emp_()
        val stmt = dsl.insert(e).select { from(e).select() }
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "insert into EMP (ID, NAME, SALARY, VERSION) select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_",
            sql.formattedSql,
        )
    }

    @Test
    fun insert_select_entity_without_select() {
        val e = Emp_()
        val stmt = dsl.insert(e).select { from(e) }
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "insert into EMP (ID, NAME, SALARY, VERSION) select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_",
            sql.formattedSql,
        )
    }

    @Test
    fun insert_select_entity_join_select() {
        val e = Emp_()
        val d = Dept_()
        val stmt = dsl.insert(e).select { from(d).innerJoin(e) { eq(d.id, e.id) }.select(e) }
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "insert into EMP (ID, NAME, SALARY, VERSION) select t1_.ID, t1_.NAME, t1_.SALARY, t1_.VERSION from CATA.DEPT t0_ inner join EMP t1_ on (t0_.ID = t1_.ID)",
            sql.formattedSql,
        )
    }

    @Test
    fun insert_select_properties() {
        val e = Emp_()
        val stmt = dsl.insert(e).select { from(e).select(e.id, e.name, e.salary, e.version) }
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "insert into EMP (ID, NAME, SALARY, VERSION) select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_",
            sql.formattedSql,
        )
    }
}

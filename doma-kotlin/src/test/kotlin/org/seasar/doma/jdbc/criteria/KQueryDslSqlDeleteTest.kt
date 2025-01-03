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
import org.seasar.doma.jdbc.dialect.MysqlDialect
import org.seasar.doma.kotlin.jdbc.criteria.KQueryDsl

internal class KQueryDslSqlDeleteTest {

    private val dsl = KQueryDsl(MockConfig())

    @Test
    fun deleteFrom() {
        val e = Emp_()
        val stmt = dsl.delete(e).all()
        val sql = stmt.asSql()
        Assertions.assertEquals("delete from EMP t0_", sql.formattedSql)
    }

    @Test
    fun aliasInDeleteClause() {
        val config = MockConfig()
        config.dialect = MysqlDialect()
        val mysqlDsl = KQueryDsl(config)
        val e = Emp_()
        val stmt = mysqlDsl.delete(e).all()
        val sql = stmt.asSql()
        Assertions.assertEquals("delete t0_ from EMP t0_", sql.formattedSql)
    }

    @Test
    fun where() {
        val e = Emp_()
        val stmt = dsl
            .delete(e)
            .where {
                eq(e.name, "a")
                eq(e.version, 1)
            }
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "delete from EMP t0_ where t0_.NAME = 'a' and t0_.VERSION = 1",
            sql.formattedSql,
        )
    }

    @Test
    fun where_in() {
        val e = Emp_()
        val d = Dept_()
        val stmt = dsl.delete(e).where { `in`(e.id, from(d).select(d.id)) }
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "delete from EMP t0_ where t0_.ID in (select t1_.ID from CATA.DEPT t1_)",
            sql.formattedSql,
        )
    }
}

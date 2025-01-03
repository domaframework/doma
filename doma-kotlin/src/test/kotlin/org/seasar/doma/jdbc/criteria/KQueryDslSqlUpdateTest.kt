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
import org.seasar.doma.jdbc.criteria.entity.Emp_
import org.seasar.doma.jdbc.criteria.mock.MockConfig
import org.seasar.doma.jdbc.dialect.MssqlDialect
import org.seasar.doma.kotlin.jdbc.criteria.KQueryDsl
import java.math.BigDecimal

internal class KQueryDslSqlUpdateTest {

    private val dsl = org.seasar.doma.kotlin.jdbc.criteria.KQueryDsl(MockConfig())

    @Test
    fun set() {
        val e = Emp_()
        val stmt = dsl
            .update(e)
            .set {
                value(e.name, "bbb")
                value(e.salary, null)
            }
        val sql = stmt.asSql()
        Assertions.assertEquals("update EMP t0_ set NAME = 'bbb', SALARY = null", sql.formattedSql)
    }

    @Test
    fun where() {
        val e = Emp_()
        val stmt = dsl
            .update(e)
            .set {
                value(e.name, "bbb")
                value(e.salary, BigDecimal("1000"))
            }
            .where { eq(e.id, 1) }
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "update EMP t0_ set NAME = 'bbb', SALARY = 1000 where t0_.ID = 1",
            sql.formattedSql,
        )
    }

    @Test
    fun aliasInUpdateClause() {
        val config = MockConfig()
        config.dialect = MssqlDialect()
        val queryDsl = KQueryDsl(config)
        val e = Emp_()
        val stmt = queryDsl
            .update(e)
            .set {
                value(e.name, "bbb")
                value(e.salary, BigDecimal("1000"))
            }
            .where { eq(e.id, 1) }
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "update t0_ set NAME = 'bbb', SALARY = 1000 from EMP t0_ where t0_.ID = 1",
            sql.formattedSql,
        )
    }
}

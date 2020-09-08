package org.seasar.doma.jdbc.criteria

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.seasar.doma.jdbc.criteria.entity.Emp_
import org.seasar.doma.jdbc.criteria.mock.MockConfig
import org.seasar.doma.jdbc.dialect.MssqlDialect
import java.math.BigDecimal

internal class KNativeSqlUpdateTest {

    private val nativeSql = KNativeSql(MockConfig())

    @Test
    fun set() {
        val e = Emp_()
        val stmt = nativeSql
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
        val stmt = nativeSql
                .update(e)
                .set {
                    value(e.name, "bbb")
                    value(e.salary, BigDecimal("1000"))
                }
                .where { eq(e.id, 1) }
        val sql = stmt.asSql()
        Assertions.assertEquals(
                "update EMP t0_ set NAME = 'bbb', SALARY = 1000 where t0_.ID = 1", sql.formattedSql)
    }

    @Test
    fun aliasInUpdateClause() {
        val config = MockConfig()
        config.dialect = MssqlDialect()
        val nativeSql = KNativeSql(config)
        val e = Emp_()
        val stmt = nativeSql
                .update(e)
                .set {
                    value(e.name, "bbb")
                    value(e.salary, BigDecimal("1000"))
                }
                .where { eq(e.id, 1) }
        val sql = stmt.asSql()
        Assertions.assertEquals(
                "update t0_ set NAME = 'bbb', SALARY = 1000 from EMP t0_ where t0_.ID = 1",
                sql.formattedSql)
    }
}
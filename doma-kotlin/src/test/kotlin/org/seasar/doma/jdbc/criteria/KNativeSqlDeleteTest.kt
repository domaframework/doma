package org.seasar.doma.jdbc.criteria

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.seasar.doma.jdbc.criteria.entity.Dept_
import org.seasar.doma.jdbc.criteria.entity.Emp_
import org.seasar.doma.jdbc.criteria.mock.MockConfig
import org.seasar.doma.jdbc.criteria.statement.Statement
import org.seasar.doma.jdbc.dialect.MysqlDialect

internal class KNativeSqlDeleteTest {

    private val nativeSql = org.seasar.doma.kotlin.jdbc.criteria.KNativeSql(MockConfig())

    @Test
    fun deleteFrom() {
        val e = Emp_()
        val stmt = nativeSql.delete(e)
        val sql = stmt.asSql()
        Assertions.assertEquals("delete from EMP t0_", sql.formattedSql)
    }

    @Test
    fun aliasInDeleteClause() {
        val config = MockConfig()
        config.dialect = MysqlDialect()
        val nativeSql = NativeSql(config)
        val e = Emp_()
        val stmt: Statement<Int> = nativeSql.delete(e)
        val sql = stmt.asSql()
        Assertions.assertEquals("delete t0_ from EMP t0_", sql.formattedSql)
    }

    @Test
    fun where() {
        val e = Emp_()
        val stmt = nativeSql
            .delete(e)
            .where {
                eq(e.name, "a")
                eq(e.version, 1)
            }
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "delete from EMP t0_ where t0_.NAME = 'a' and t0_.VERSION = 1",
            sql.formattedSql
        )
    }

    @Test
    fun where_in() {
        val e = Emp_()
        val d = Dept_()
        val stmt = nativeSql.delete(e).where { `in`(e.id, from(d).select(d.id)) }
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "delete from EMP t0_ where t0_.ID in (select t1_.ID from CATA.DEPT t1_)",
            sql.formattedSql
        )
    }
}

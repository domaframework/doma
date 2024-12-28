package org.seasar.doma.jdbc.criteria

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.seasar.doma.jdbc.criteria.entity.Emp
import org.seasar.doma.jdbc.criteria.entity.Emp_
import org.seasar.doma.jdbc.criteria.mock.MockConfig
import java.math.BigDecimal

internal class KQueryDslEntityBatchDeleteTest {

    private val dsl = org.seasar.doma.kotlin.jdbc.criteria.KQueryDsl(MockConfig())

    @Test
    fun test() {
        val emp = Emp()
        emp.id = 1
        emp.name = "aaa"
        emp.salary = BigDecimal("1000")
        emp.version = 1
        val e = Emp_()
        val stmt = dsl.delete(e).batch(listOf(emp))
        val sql = stmt.asSql()
        Assertions.assertEquals("delete from EMP where ID = 1 and VERSION = 1", sql.formattedSql)
    }

    @Test
    fun ignoreVersion() {
        val emp = Emp()
        emp.id = 1
        emp.name = "aaa"
        emp.salary = BigDecimal("1000")
        emp.version = 1
        val e = Emp_()
        val stmt = dsl.delete(e) { ignoreVersion = true }.batch(listOf(emp))
        val sql = stmt.asSql()
        Assertions.assertEquals("delete from EMP where ID = 1", sql.formattedSql)
    }

    @Test
    fun empty() {
        val e = Emp_()
        val stmt = dsl.delete(e).batch(emptyList())
        val sql = stmt.asSql()
        Assertions.assertEquals("This SQL is empty because target entities are empty.", sql.formattedSql)
    }
}

package org.seasar.doma.jdbc.criteria

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.seasar.doma.jdbc.criteria.entity.Emp
import org.seasar.doma.jdbc.criteria.entity.Emp_
import org.seasar.doma.jdbc.criteria.mock.MockConfig
import java.math.BigDecimal

internal class KEntityqlDeleteTest {

    private val entityql = org.seasar.doma.kotlin.jdbc.criteria.KEntityql(MockConfig())

    @Test
    fun test() {
        val emp = Emp()
        emp.id = 1
        emp.name = "aaa"
        emp.salary = BigDecimal("1000")
        emp.version = 1
        val e = Emp_()
        val stmt = entityql.delete(e, emp)
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
        val stmt = entityql.delete(e, emp) { ignoreVersion = true }
        val sql = stmt.asSql()
        Assertions.assertEquals("delete from EMP where ID = 1", sql.formattedSql)
    }
}

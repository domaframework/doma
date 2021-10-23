package org.seasar.doma.jdbc.criteria

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.seasar.doma.jdbc.criteria.entity.Emp
import org.seasar.doma.jdbc.criteria.entity.Emp_
import org.seasar.doma.jdbc.criteria.mock.MockConfig
import java.math.BigDecimal

internal class KEntityqlUpdateTest {

    private val entityql = org.seasar.doma.kotlin.jdbc.criteria.KEntityql(MockConfig())

    @Test
    fun test() {
        val emp = Emp()
        emp.id = 1
        emp.name = "aaa"
        emp.salary = BigDecimal("1000")
        emp.version = 1
        val e = Emp_()
        val stmt = entityql.update(e, emp)
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "update EMP set NAME = 'aaa', SALARY = 1000, VERSION = 1 + 1 where ID = 1 and VERSION = 1",
            sql.formattedSql
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
        val stmt = entityql.update(e, emp) { ignoreVersion = true }
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "update EMP set NAME = 'aaa', SALARY = 1000, VERSION = 1 where ID = 1",
            sql.formattedSql
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
        val stmt = entityql.update(e, emp) { excludeNull = true }
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "update EMP set SALARY = 1000, VERSION = 1 + 1 where ID = 1 and VERSION = 1",
            sql.formattedSql
        )
    }
}

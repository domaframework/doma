package org.seasar.doma.jdbc.criteria

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.seasar.doma.jdbc.criteria.entity.Emp
import org.seasar.doma.jdbc.criteria.entity.Emp_
import org.seasar.doma.jdbc.criteria.mock.MockConfig
import java.math.BigDecimal

internal class KEntityqlInsertTest {

    private val entityql = KEntityql(MockConfig())

    @Test
    fun insertInto() {
        val emp = Emp()
        emp.id = 1
        emp.name = "aaa"
        emp.salary = BigDecimal("1000")
        emp.version = 1
        val e = Emp_()
        val stmt = entityql.insert(e, emp)
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "insert into EMP (ID, NAME, SALARY, VERSION) values (1, 'aaa', 1000, 1)",
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
        val stmt = entityql.insert(e, emp) { excludeNull = true }
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "insert into EMP (ID, SALARY, VERSION) values (1, 1000, 1)",
            sql.formattedSql
        )
    }
}

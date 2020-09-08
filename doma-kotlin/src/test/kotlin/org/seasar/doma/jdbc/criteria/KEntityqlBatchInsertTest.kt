package org.seasar.doma.jdbc.criteria

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.seasar.doma.jdbc.criteria.entity.Emp
import org.seasar.doma.jdbc.criteria.entity.Emp_
import org.seasar.doma.jdbc.criteria.mock.MockConfig
import java.math.BigDecimal

internal class KEntityqlBatchInsertTest {

    private val entityql = KEntityql(MockConfig())

    @Test
    fun insertInto() {
        val emp = Emp()
        emp.id = 1
        emp.name = "aaa"
        emp.salary = BigDecimal("1000")
        emp.version = 1
        val e = Emp_()
        val stmt = entityql.insert(e, listOf(emp))
        val sql = stmt.asSql()
        Assertions.assertEquals(
                "insert into EMP (ID, NAME, SALARY, VERSION) values (1, 'aaa', 1000, 1)",
                sql.formattedSql)
    }

    @Test
    fun empty() {
        val e = Emp_()
        val stmt = entityql.insert(e, emptyList())
        val sql = stmt.asSql()
        Assertions.assertEquals("This SQL is empty because target entities are empty.", sql.formattedSql)
    }
}
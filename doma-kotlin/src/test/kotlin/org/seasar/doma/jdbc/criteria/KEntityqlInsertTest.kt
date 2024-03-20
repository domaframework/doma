package org.seasar.doma.jdbc.criteria

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.seasar.doma.jdbc.criteria.entity.Emp
import org.seasar.doma.jdbc.criteria.entity.Emp_
import org.seasar.doma.jdbc.criteria.mock.MockConfig
import java.math.BigDecimal

internal class KEntityqlInsertTest {
    private val config = MockConfig().apply {
        dialect = org.seasar.doma.jdbc.dialect.PostgresDialect()
    }

    private val entityql = org.seasar.doma.kotlin.jdbc.criteria.KEntityql(config)

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
            sql.formattedSql,
        )
    }

    @Test
    fun insertOnDuplicateKeyUpdate() {
        val emp = Emp()
        emp.id = 1
        emp.name = "aaa"
        emp.salary = BigDecimal("1000")
        emp.version = 1
        val e = Emp_()
        val stmt = entityql.insert(e, emp).onDuplicateKeyUpdate()
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "insert into EMP as target (ID, NAME, SALARY, VERSION) values (1, 'aaa', 1000, 1) on conflict (ID) do update set NAME = excluded.NAME, SALARY = excluded.SALARY, VERSION = excluded.VERSION",
            sql.formattedSql,
        )
    }

    @Test
    fun insertOnDuplicateKeyIgnore() {
        val emp = Emp()
        emp.id = 1
        emp.name = "aaa"
        emp.salary = BigDecimal("1000")
        emp.version = 1
        val e = Emp_()
        val stmt = entityql.insert(e, emp).onDuplicateKeyIgnore()
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "insert into EMP as target (ID, NAME, SALARY, VERSION) values (1, 'aaa', 1000, 1) on conflict (ID) do nothing",
            sql.formattedSql,
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
            sql.formattedSql,
        )
    }
}

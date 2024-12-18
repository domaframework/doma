package org.seasar.doma.jdbc.criteria

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.seasar.doma.jdbc.criteria.entity.Emp
import org.seasar.doma.jdbc.criteria.entity.Emp_
import org.seasar.doma.jdbc.criteria.mock.MockConfig
import java.math.BigDecimal

internal class KEntityqlMultiInsertTest {
    private val config = MockConfig().apply {
        dialect = org.seasar.doma.jdbc.dialect.PostgresDialect()
    }

    private val entityql = org.seasar.doma.kotlin.jdbc.criteria.KEntityql(config)

    @Test
    fun insert() {
        val emp = Emp()
        emp.id = 1
        emp.name = "aaa"
        emp.salary = BigDecimal("1000")
        emp.version = 1

        val emp2 = Emp()
        emp2.id = 2
        emp2.name = "bbb"
        emp2.salary = BigDecimal("2000")
        emp2.version = 1

        val e = Emp_()
        val stmt = entityql.insertMulti(e, listOf(emp, emp2))
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "insert into EMP (ID, NAME, SALARY, VERSION) values (1, 'aaa', 1000, 1), (2, 'bbb', 2000, 1)",
            sql.formattedSql,
        )
    }

    private val items = listOf(
        Emp().apply {
            id = 1
            name = "aaa"
            salary = BigDecimal("1000")
            version = 1
        },
        Emp().apply {
            id = 2
            name = "eee"
            salary = BigDecimal("2000")
            version = 2
        },
    )

    @Test
    fun onDuplicateKeyUpdate() {
        val e = Emp_()
        val stmt = entityql.insertMulti(e, items).onDuplicateKeyUpdate()
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "insert into EMP as target (ID, NAME, SALARY, VERSION) values (1, 'aaa', 1000, 1), (2, 'eee', 2000, 2) on conflict (ID) do update set NAME = excluded.NAME, SALARY = excluded.SALARY, VERSION = excluded.VERSION",
            sql.formattedSql,
        )
    }

    @Test
    fun onDuplicateKeyIgnore() {
        val e = Emp_()
        val stmt = entityql.insertMulti(e, items).onDuplicateKeyIgnore()
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "insert into EMP as target (ID, NAME, SALARY, VERSION) values (1, 'aaa', 1000, 1), (2, 'eee', 2000, 2) on conflict do nothing",
            sql.formattedSql,
        )
    }
}

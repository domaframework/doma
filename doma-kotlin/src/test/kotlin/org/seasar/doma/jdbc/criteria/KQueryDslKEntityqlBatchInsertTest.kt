package org.seasar.doma.jdbc.criteria

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.seasar.doma.jdbc.criteria.entity.Emp
import org.seasar.doma.jdbc.criteria.entity.Emp_
import org.seasar.doma.jdbc.criteria.mock.MockConfig
import java.math.BigDecimal

internal class KQueryDslKEntityqlBatchInsertTest {
    private val config = MockConfig().apply {
        dialect = org.seasar.doma.jdbc.dialect.PostgresDialect()
    }

    private val dsl = org.seasar.doma.kotlin.jdbc.criteria.KQueryDsl(config)

    @Test
    fun insertInto() {
        val emp = Emp()
        emp.id = 1
        emp.name = "aaa"
        emp.salary = BigDecimal("1000")
        emp.version = 1
        val e = Emp_()
        val stmt = dsl.insert(e).batch(listOf(emp))
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
        val stmt = dsl.insert(e).batch(listOf(emp)).onDuplicateKeyUpdate()
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
        val stmt = dsl.insert(e).batch(listOf(emp)).onDuplicateKeyIgnore()
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "insert into EMP as target (ID, NAME, SALARY, VERSION) values (1, 'aaa', 1000, 1) on conflict do nothing",
            sql.formattedSql,
        )
    }

    @Test
    fun empty() {
        val e = Emp_()
        val stmt = dsl.insert(e).batch(emptyList())
        val sql = stmt.asSql()
        Assertions.assertEquals("This SQL is empty because target entities are empty.", sql.formattedSql)
    }
}

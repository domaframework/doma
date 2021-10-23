package org.seasar.doma.jdbc.criteria

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.seasar.doma.jdbc.criteria.entity.Dept_
import org.seasar.doma.jdbc.criteria.entity.Emp_
import org.seasar.doma.jdbc.criteria.mock.MockConfig

internal class KNativeSqlInsertTest {

    private val nativeSql = org.seasar.doma.kotlin.jdbc.criteria.KNativeSql(MockConfig())

    @Test
    fun insert() {
        val e = Emp_()
        val stmt = nativeSql
            .insert(e)
            .values {
                value(e.id, 99)
                value(e.name, "aaa")
                value(e.salary, null)
                value(e.version, 1)
            }
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "insert into EMP (ID, NAME, SALARY, VERSION) values (99, 'aaa', null, 1)",
            sql.formattedSql
        )
    }

    @Test
    fun insert_select_entity() {
        val e = Emp_()
        val stmt = nativeSql.insert(e).select { from(e).select() }
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "insert into EMP (ID, NAME, SALARY, VERSION) select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_",
            sql.formattedSql
        )
    }

    @Test
    fun insert_select_entity_without_select() {
        val e = Emp_()
        val stmt = nativeSql.insert(e).select { from(e) }
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "insert into EMP (ID, NAME, SALARY, VERSION) select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_",
            sql.formattedSql
        )
    }

    @Test
    fun insert_select_entity_join_select() {
        val e = Emp_()
        val d = Dept_()
        val stmt = nativeSql.insert(e).select { from(d).innerJoin(e) { eq(d.id, e.id) }.select(e) }
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "insert into EMP (ID, NAME, SALARY, VERSION) select t1_.ID, t1_.NAME, t1_.SALARY, t1_.VERSION from CATA.DEPT t0_ inner join EMP t1_ on (t0_.ID = t1_.ID)",
            sql.formattedSql
        )
    }

    @Test
    fun insert_select_properties() {
        val e = Emp_()
        val stmt = nativeSql.insert(e).select { from(e).select(e.id, e.name, e.salary, e.version) }
        val sql = stmt.asSql()
        Assertions.assertEquals(
            "insert into EMP (ID, NAME, SALARY, VERSION) select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_",
            sql.formattedSql
        )
    }
}

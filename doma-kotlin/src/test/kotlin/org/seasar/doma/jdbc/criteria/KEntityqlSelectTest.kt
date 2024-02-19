package org.seasar.doma.jdbc.criteria

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.seasar.doma.DomaException
import org.seasar.doma.jdbc.criteria.entity.Dept_
import org.seasar.doma.jdbc.criteria.entity.Emp_
import org.seasar.doma.jdbc.criteria.mock.MockConfig
import org.seasar.doma.jdbc.criteria.option.AssociationOption
import org.seasar.doma.jdbc.criteria.option.DistinctOption
import org.seasar.doma.message.Message

internal class KEntityqlSelectTest {

    private val entityql = org.seasar.doma.kotlin.jdbc.criteria.KEntityql(MockConfig())
    private val nativeSql = org.seasar.doma.kotlin.jdbc.criteria.KNativeSql(MockConfig())

    @Test
    fun from() {
        val e = Emp_()
        val stmt = entityql.from(e)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_",
            sql.formattedSql,
        )
    }

    @Test
    fun from_with_subQuery() {
        val e = Emp_()
        val subQueryStmt = nativeSql.from(e)
        val stmt = entityql.from(e, subQueryStmt)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from (select t0_.ID AS ID, t0_.NAME AS NAME, t0_.SALARY AS SALARY, t0_.VERSION AS VERSION from EMP t0_) t0_",
            sql.formattedSql,
        )
    }

    @Test
    fun innerJoin() {
        val e = Emp_()
        val d = Dept_()
        val stmt = entityql.from(e).innerJoin(d) { eq(e.id, d.id) }
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ inner join CATA.DEPT t1_ on (t0_.ID = t1_.ID)",
            sql.formattedSql,
        )
    }

    @Test
    fun innerJoin_empty() {
        val e = Emp_()
        val d = Dept_()
        val stmt = entityql.from(e).innerJoin(d) {}
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_",
            sql.formattedSql,
        )
    }

    @Test
    fun leftJoin() {
        val e = Emp_()
        val d = Dept_()
        val stmt = entityql.from(e).leftJoin(d) { eq(e.id, d.id) }
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ left outer join CATA.DEPT t1_ on (t0_.ID = t1_.ID)",
            sql.formattedSql,
        )
    }

    @Test
    fun leftJoin_empty() {
        val e = Emp_()
        val d = Dept_()
        val stmt = entityql.from(e).leftJoin(d) {}
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_",
            sql.formattedSql,
        )
    }

    @Test
    fun associate() {
        val e = Emp_()
        val d = Dept_()
        val stmt = entityql.from(e).innerJoin(d) {
            eq(e.id, d.id)
        }.associate(e, d) { _, _ -> }
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION, t1_.ID, t1_.NAME from EMP t0_ inner join CATA.DEPT t1_ on (t0_.ID = t1_.ID)",
            sql.formattedSql,
        )
    }

    @Test
    fun associate_mandatory() {
        val e = Emp_()
        val d = Dept_()
        try {
            entityql.from(e).innerJoin(d) {}.associate(e, d) { _, _ -> }
            fail<Any>()
        } catch (ex: DomaException) {
            assertEquals(Message.DOMA6001, ex.messageResource)
            println(ex.message)
        }
    }

    @Test
    fun associate_optional() {
        val e = Emp_()
        val d = Dept_()
        val stmt = entityql
            .from(e)
            .innerJoin(d) { }
            .associate(e, d, { _, _ -> }, AssociationOption.optional())
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_",
            sql.formattedSql,
        )
    }

    @Test
    fun associate_more() {
        val e = Emp_()
        val d = Dept_()
        val e2 = Emp_()
        val d2 = Dept_()
        val stmt = entityql
            .from(e)
            .innerJoin(d) { eq(e.id, d.id) }
            .leftJoin(e2) { eq(e.id, e2.id) }
            .leftJoin(d2) { eq(d.id, d2.id) }
            .associate(e, d) { _, _ -> }
            .associate(e, e2) { _, _ -> }
            .associate(d, d2) { _, _ -> }
        val sql = stmt.asSql()
        assertEquals(
            "select " +
                "t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION, " +
                "t1_.ID, t1_.NAME, " +
                "t2_.ID, t2_.NAME, t2_.SALARY, t2_.VERSION, " +
                "t3_.ID, t3_.NAME from EMP t0_ " +
                "inner join CATA.DEPT t1_ on (t0_.ID = t1_.ID) " +
                "left outer join EMP t2_ on (t0_.ID = t2_.ID) " +
                "left outer join CATA.DEPT t3_ on (t1_.ID = t3_.ID)",
            sql.formattedSql,
        )
    }

    @Test
    fun limit() {
        val e = Emp_()
        val stmt = entityql.from(e).limit(5)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ offset 0 rows fetch first 5 rows only",
            sql.formattedSql,
        )
    }

    @Test
    fun limit_null() {
        val e = Emp_()
        val stmt = entityql.from(e).limit(null)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_",
            sql.formattedSql,
        )
    }

    @Test
    fun offset() {
        val e = Emp_()
        val stmt = entityql.from(e).offset(5)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ offset 5 rows",
            sql.formattedSql,
        )
    }

    @Test
    fun offset_null() {
        val e = Emp_()
        val stmt = entityql.from(e).offset(null)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_",
            sql.formattedSql,
        )
    }

    @Test
    fun distinct() {
        val e = Emp_()
        val stmt = entityql.from(e).distinct()
        val sql = stmt.asSql()
        assertEquals(
            "select distinct t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_",
            sql.formattedSql,
        )
    }

    @Test
    fun distinct_basic() {
        val e = Emp_()
        val stmt = entityql.from(e).distinct(DistinctOption.basic())
        val sql = stmt.asSql()
        assertEquals(
            "select distinct t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_",
            sql.formattedSql,
        )
    }

    @Test
    fun distinct_none() {
        val e = Emp_()
        val stmt = entityql.from(e).distinct(DistinctOption.none())
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_",
            sql.formattedSql,
        )
    }

    @Test
    fun select() {
        val e = Emp_()
        val stmt = entityql.from(e).select(e)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_",
            sql.formattedSql,
        )
    }

    @Test
    fun select_join() {
        val e = Emp_()
        val d = Dept_()
        val stmt = entityql.from(e).innerJoin(d) { eq(e.id, d.id) }.select(d)
        val sql = stmt.asSql()
        assertEquals(
            "select t1_.ID, t1_.NAME from EMP t0_ inner join CATA.DEPT t1_ on (t0_.ID = t1_.ID)",
            sql.formattedSql,
        )
    }

    @Test
    fun select_illegal_entityMetamodel() {
        val e = Emp_()
        val d = Dept_()
        val ex = assertThrows(DomaException::class.java) { entityql.from(e).select(d) }
        assertEquals(Message.DOMA6009, ex.messageResource)
        println(ex.message)
    }

    @Test
    fun selectTo() {
        val e = Emp_()
        val stmt = entityql.from(e).selectTo(e, e.name)
        val sql = stmt.asSql()
        assertEquals("select t0_.ID, t0_.NAME from EMP t0_", sql.formattedSql)
    }

    @Test
    fun selectTo_no_propertyMetamodels() {
        val e = Emp_()
        val stmt = entityql.from(e).selectTo(e)
        val sql = stmt.asSql()
        assertEquals("select t0_.ID from EMP t0_", sql.formattedSql)
    }

    @Test
    fun selectTo_illegal_entityMetamodel() {
        val e = Emp_()
        val d = Dept_()
        val ex = assertThrows(DomaException::class.java) { entityql.from(e).selectTo(d, d.id) }
        assertEquals(Message.DOMA6007, ex.messageResource)
        println(ex.message)
    }

    @Test
    fun selectTo_illegal_propertyMetamodel() {
        val e = Emp_()
        val d = Dept_()
        val ex = assertThrows(DomaException::class.java) { entityql.from(e).selectTo(e, d.id) }
        assertEquals(Message.DOMA6008, ex.messageResource)
        println(ex.message)
    }

    @Test
    fun peek() {
        val e = Emp_()
        entityql
            .from(e)
            .peek { println(it) }
            .where { eq(e.name, "SMITH") }
            .peek { println(it) }
            .orderBy { asc(e.salary) }
            .peek { println(it.formattedSql) }
            .fetch()
    }
}

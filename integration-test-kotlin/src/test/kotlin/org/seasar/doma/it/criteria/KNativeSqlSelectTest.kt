package org.seasar.doma.it.criteria

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.seasar.doma.it.Dbms
import org.seasar.doma.it.IntegrationTestEnvironment
import org.seasar.doma.it.Run
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.SqlLogType
import org.seasar.doma.jdbc.criteria.expression.Expressions
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel
import org.seasar.doma.jdbc.criteria.statement.EmptyWhereClauseException
import org.seasar.doma.jdbc.criteria.tuple.Tuple2
import org.seasar.doma.kotlin.jdbc.criteria.KNativeSql
import org.seasar.doma.kotlin.jdbc.criteria.expression.KExpressions
import java.math.BigDecimal
import java.time.LocalDate

@ExtendWith(IntegrationTestEnvironment::class)
class KNativeSqlSelectTest(config: Config) {

    private val nativeSql = KNativeSql(config)

    @Test
    fun settings() {
        val e = Employee_()
        val list = nativeSql
            .from(
                e
            ) {
                comment = "all employees"
                sqlLogType = SqlLogType.RAW
                queryTimeout = 1000
                allowEmptyWhere = true
                fetchSize = 100
                maxRows = 100
            }
            .fetch()
        assertEquals(14, list.size)
    }

    @Test
    fun fetch_allowEmptyWhere_disabled() {
        val e = Employee_()
        assertThrows(
            EmptyWhereClauseException::class.java
        ) { nativeSql.from(e) { allowEmptyWhere = false }.fetch() }
    }

    @Test
    fun from() {
        val e = Employee_()
        val list = nativeSql.from(e).fetch()
        assertEquals(14, list.size)
    }

    @Test
    fun mapStream() {
        val e = Employee_()
        val map = nativeSql
            .from(e)
            .mapSequence { it.groupBy { employee -> employee.departmentId } }
        assertEquals(3, map.size)
    }

    @Test
    fun test_select() {
        val e = Employee_()
        val list = nativeSql.from(e).select().fetch()
        assertEquals(14, list.size)
        val employee = list[0]
        assertEquals("SMITH", employee.employeeName)
    }

    @Test
    fun select_entity() {
        val e = Employee_()
        val list = nativeSql.from(e).select(e).fetch()
        assertEquals(14, list.size)
        val employee = list[0]
        assertEquals("SMITH", employee.employeeName)
    }

    @Test
    fun select_joined_entity() {
        val e = Employee_()
        val d = Department_()
        val list = nativeSql
            .from(e)
            .innerJoin(d) { eq(e.departmentId, d.departmentId) }
            .select(d)
            .fetch()
        assertEquals(14, list.size)
    }

    @Test
    fun select_entities_tuple2() {
        val e = Employee_()
        val d = Department_()
        val list = nativeSql
            .from(e)
            .innerJoin(d) { eq(e.departmentId, d.departmentId) }
            .orderBy { asc(e.employeeId) }
            .select(e, d)
            .fetch()
        assertEquals(14, list.size)
        val tuple2 = list[0]
        val employee = tuple2.item1
        val department = tuple2.item2
        assertEquals("SMITH", employee.employeeName)
        assertEquals("RESEARCH", department.departmentName)
    }

    @Test
    fun select_entities_tuple2_emptyEntity() {
        val e = Employee_()
        val d = Department_()
        val list = nativeSql
            .from(d)
            .leftJoin(e) { eq(d.departmentId, e.departmentId) }
            .where { eq(d.departmentId, 4) }
            .select(d, e)
            .fetch()
        assertEquals(1, list.size)
        val tuple2 = list[0]
        val department = tuple2.item1
        assertEquals("OPERATIONS", department.departmentName)
        val employee = tuple2.item2
        Assertions.assertNull(employee)
    }

    @Test
    fun select_property() {
        val e = Employee_()
        val list = nativeSql.from(e).select(e.employeeName).fetch()
        assertEquals(14, list.size)
        assertEquals("SMITH", list[0])
    }

    @Test
    fun select_properties_tuple2() {
        val e = Employee_()
        val list = nativeSql.from(e).select(e.employeeName, e.employeeNo).fetch()
        assertEquals(14, list.size)
        assertEquals("SMITH", list[0].item1)
    }

    @Test
    fun selectTo() {
        val e = Employee_()
        val list = nativeSql.from(e).selectTo(e, e.employeeNo).fetch()
        assertEquals(14, list.size)
        list.map { it.employeeId }.forEach { println(it) }
        assertTrue(list.map { it.employeeId }.all { it != null })
        assertTrue(list.map { it.employeeNo }.all { it != null })
        assertTrue(list.map { it.employeeName }.all { it == null })
    }

    @Test
    fun select_mapStream() {
        val e = Employee_()
        val count = nativeSql.from(e).select(e.employeeName).mapSequence { it.count() }
        assertEquals(14, count)
    }

    @Test
    fun select_row() {
        val e = Employee_()

        @Suppress("RemoveRedundantSpreadOperator")
        val list = nativeSql
            .from(e)
            .orderBy { asc(e.employeeId) }
            .select(e.employeeId, *arrayOf<PropertyMetamodel<*>>(e.employeeName))
            .fetch()
        assertEquals(14, list.size)
        val row = list[0]
        assertEquals(2, row.size())
        assertTrue(row.containsKey(e.employeeId))
        assertEquals(1, row.get(e.employeeId))
        assertTrue(row.containsKey(e.employeeName))
        assertEquals("SMITH", row.get(e.employeeName))
        assertFalse(row.containsKey(e.hiredate))
        assertNotNull(row.keySet())
        assertNotNull(row.values())
    }

    @Test
    fun selectAsRow() {
        val e = Employee_()

        @Suppress("RemoveRedundantSpreadOperator")
        val list = nativeSql
            .from(e)
            .orderBy { asc(e.employeeId) }
            .selectAsRow(e.employeeId, e.employeeName)
            .fetch()
        assertEquals(14, list.size)
        val row = list[0]
        assertEquals(2, row.size())
        assertTrue(row.containsKey(e.employeeId))
        assertEquals(1, row.get(e.employeeId))
        assertTrue(row.containsKey(e.employeeName))
        assertEquals("SMITH", row.get(e.employeeName))
        assertFalse(row.containsKey(e.hiredate))
        assertNotNull(row.keySet())
        assertNotNull(row.values())
    }

    @Test
    fun where() {
        val e = Employee_()
        val list = nativeSql.from(e).where { eq(e.departmentId, 2) }.fetch()
        assertEquals(5, list.size)
    }

    @Test
    fun aggregate() {
        val e = Employee_()
        val salary = nativeSql.from(e).select(KExpressions.sum(e.salary)).fetchOne()
        checkNotNull(salary)
        assertEquals(0, salary.value.compareTo(BigDecimal("29025")))
    }

    @Test
    fun aggregate_countDistinct() {
        val e = Employee_()
        val count = nativeSql.from(e).select(KExpressions.countDistinct(e.departmentId)).fetchOne()
        assertEquals(3, count)
    }

    @Test
    fun groupBy() {
        val e = Employee_()
        val list = nativeSql.from(e).groupBy(e.departmentId).select(e.departmentId, KExpressions.count()).fetch()
        assertEquals(3, list.size)
    }

    @Test
    fun groupBy_auto_generation() {
        val e = Employee_()
        val list = nativeSql.from(e).select(e.departmentId, KExpressions.count()).fetch()
        assertEquals(3, list.size)
    }

    @Test
    fun having() {
        val e = Employee_()
        val d = Department_()
        val list = nativeSql
            .from(e)
            .innerJoin(d) { eq(e.departmentId, d.departmentId) }
            .having { gt(Expressions.count(), 3L) }
            .orderBy { asc(Expressions.count()) }
            .select(Expressions.count(), d.departmentName)
            .fetch()
        assertEquals(2, list.size)
        assertEquals(Tuple2(5L, "RESEARCH"), list[0])
        assertEquals(Tuple2(6L, "SALES"), list[1])
    }

    @Test
    fun having_multi_conditions() {
        val e = Employee_()
        val d = Department_()
        val list = nativeSql
            .from(e)
            .innerJoin(d) { eq(e.departmentId, d.departmentId) }
            .groupBy(d.departmentName)
            .having {
                gt(KExpressions.count(), 3L)
                or { le(KExpressions.min(e.salary), Salary("2000")) }
            }
            .orderBy { asc(Expressions.count()) }
            .select(KExpressions.count(), d.departmentName)
            .fetch()
        assertEquals(3, list.size)
        assertEquals(Tuple2(3L, "ACCOUNTING"), list[0])
        assertEquals(Tuple2(5L, "RESEARCH"), list[1])
        assertEquals(Tuple2(6L, "SALES"), list[2])
    }

    @Test
    fun limit_offset() {
        val e = Employee_()
        val list = nativeSql.from(e).limit(5).offset(3).orderBy { asc(e.employeeNo) }.fetch()
        assertEquals(5, list.size)
    }

    @Test
    fun forUpdate() {
        val e = Employee_()
        val list = nativeSql.from(e).where { eq(e.employeeId, 1) }.forUpdate().fetch()
        assertEquals(1, list.size)
    }

    @Test
    fun union() {
        val e = Employee_()
        val d = Department_()
        val list = nativeSql
            .from(e)
            .select(e.employeeId, e.employeeName)
            .union(nativeSql.from(d).select(d.departmentId, d.departmentName))
            .fetch()
        assertEquals(18, list.size)
    }

    @Test
    fun union_mapStream() {
        val e = Employee_()
        val d = Department_()
        val count = nativeSql
            .from(e)
            .select(e.employeeId, e.employeeName)
            .union(nativeSql.from(d).select(d.departmentId, d.departmentName))
            .mapSequence { it.count() }
        assertEquals(18, count)
    }

    @Test
    fun unionAll_entity() {
        val d = Department_()
        val list = nativeSql.from(d).unionAll(nativeSql.from(d)).fetch()
        assertEquals(8, list.size)
    }

    @Test
    fun union_orderBy() {
        val e = Employee_()
        val d = Department_()
        val list = nativeSql
            .from(e)
            .select(e.employeeId, e.employeeName)
            .union(nativeSql.from(d).select(d.departmentId, d.departmentName))
            .orderBy { asc(2) }
            .fetch()
        assertEquals(18, list.size)
    }

    @Test
    @Run(unless = [Dbms.MYSQL])
    fun union_multi_orderBy() {
        val e = Employee_()
        val d = Department_()
        val list = nativeSql
            .from(e)
            .select(e.employeeId, e.employeeName)
            .union(nativeSql.from(d).select(d.departmentId, d.departmentName))
            .unionAll(nativeSql.from(e).select(e.employeeId, e.employeeName))
            .orderBy { asc(2) }
            .fetch()
        assertEquals(32, list.size)
    }

    @Test
    fun distinct() {
        val e = Employee_()
        val d = Department_()
        val list = nativeSql
            .from(d)
            .distinct()
            .leftJoin(e) { eq(d.departmentId, e.departmentId) }
            .fetch()
        assertEquals(4, list.size)
    }

    @Test
    fun peek() {
        val d = Department_()
        val locations = nativeSql
            .from(d)
            .peek { println(it) }
            .where { eq(d.departmentName, "SALES") }
            .peek { println(it) }
            .orderBy { asc(d.location) }
            .peek { println(it.formattedSql) }
            .select(d.location)
            .peek { println(it.formattedSql) }
            .fetch()
        assertEquals(1, locations.size)
    }

    @Test
    fun expressions_add() {
        val e = Employee_()
        val list = nativeSql
            .from(e)
            .select(Expressions.add(e.version, 1), Expressions.add(1, e.version), Expressions.add(e.departmentId, e.version))
            .fetch()
        assertEquals(14, list.size)
    }

    @Test
    fun expressions_sub() {
        val e = Employee_()
        val list = nativeSql
            .from(e)
            .select(Expressions.sub(e.version, 1), Expressions.sub(1, e.version), Expressions.sub(e.departmentId, e.version))
            .fetch()
        assertEquals(14, list.size)
    }

    @Test
    fun expressions_mul() {
        val e = Employee_()
        val list = nativeSql
            .from(e)
            .select(Expressions.mul(e.version, 1), Expressions.mul(1, e.version), Expressions.mul(e.departmentId, e.version))
            .fetch()
        assertEquals(14, list.size)
    }

    @Test
    fun expressions_div() {
        val e = Employee_()
        val list = nativeSql
            .from(e)
            .select(Expressions.div(e.version, 1), Expressions.div(1, e.version), Expressions.div(e.departmentId, e.version))
            .fetch()
        assertEquals(14, list.size)
    }

    @Test
    fun expressions_mod() {
        val e = Employee_()
        val list = nativeSql
            .from(e)
            .select(Expressions.mod(e.version, 1), Expressions.mod(1, e.version), Expressions.mod(e.departmentId, e.version))
            .fetch()
        assertEquals(14, list.size)
    }

    @Test
    fun expressions_concat() {
        val e = Employee_()
        val list = nativeSql
            .from(e)
            .select(
                Expressions.concat(e.employeeName, "a"),
                Expressions.concat("b", e.employeeName),
                Expressions.concat(e.employeeName, e.employeeName)
            )
            .fetch()
        assertEquals(14, list.size)
    }

    @Test
    fun expressions_when() {
        val e = Employee_()
        val list = nativeSql
            .from(e)
            .select(
                KExpressions.case(
                    {
                        eq(e.employeeName, Expressions.literal("SMITH"), Expressions.lower(e.employeeName))
                        eq(e.employeeName, Expressions.literal("KING"), Expressions.lower(e.employeeName))
                    },
                    KExpressions.literal("_")
                )
            )
            .fetch()
        assertEquals(14, list.size)
        assertEquals(1, list.filter { it == "smith" }.count())
        assertEquals(1, list.filter { it == "king" }.count())
    }

    @Test
    fun expressions_when_empty() {
        val e = Employee_()
        val list = nativeSql.from(e).select(KExpressions.case({ }, KExpressions.literal("_"))).fetch()
        assertEquals(14, list.size)
        assertEquals(14, list.stream().filter { it == "_" }.count())
    }

    @Test
    fun expressions_literal_localDate() {
        val e = Employee_()
        val date = nativeSql.from(e).select(KExpressions.literal(LocalDate.of(2020, 5, 23))).fetchOne()
        assertEquals(LocalDate.of(2020, 5, 23), date)
    }

    @Test
    fun expressions_select() {
        val e = Employee_()
        val e2 = Employee_()
        val count = nativeSql.from(e).select(KExpressions.select { from(e2).select(KExpressions.count(e2.employeeId)) }).fetchOne()
        assertEquals(14L, count)
    }
}

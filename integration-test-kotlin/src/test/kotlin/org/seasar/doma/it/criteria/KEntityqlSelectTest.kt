package org.seasar.doma.it.criteria

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.seasar.doma.it.IntegrationTestEnvironment
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.SqlLogType
import org.seasar.doma.jdbc.criteria.option.AssociationOption
import org.seasar.doma.jdbc.criteria.statement.EmptyWhereClauseException
import org.seasar.doma.kotlin.jdbc.criteria.KEntityql
import org.seasar.doma.kotlin.jdbc.criteria.expression.KExpressions
import org.seasar.doma.kotlin.jdbc.criteria.statement.KListable

@ExtendWith(IntegrationTestEnvironment::class)
class KEntityqlSelectTest(config: Config) {

    private val entityql = KEntityql(config)

    @Test
    fun settings() {
        val e = Employee_()
        val list = entityql.from(e) {
            comment = "all employees"
            sqlLogType = SqlLogType.RAW
            queryTimeout = 1000
            allowEmptyWhere = true
            fetchSize = 100
            maxRows = 100
        }.fetch()
        assertEquals(14, list.size)
    }

    @Test
    fun allowEmptyWhere_disabled() {
        val e = Employee_()
        assertThrows(EmptyWhereClauseException::class.java) {
            entityql.from(e) { allowEmptyWhere = false }.fetch()
        }
    }

    @Test
    fun fetch() {
        val e = Employee_()
        val list = entityql.from(e).fetch()
        assertEquals(14, list.size)
    }

    @Test
    fun fetchOne() {
        val e = Employee_()
        val employee = entityql.from(e).where { eq(e.employeeId, 1) }.fetchOne()
        assertNotNull(employee)
    }

    @Test
    fun fetchOne_null() {
        val e = Employee_()
        val stmt = entityql.from(e).where { eq(e.employeeId, 100) }
        assertThrows(NoSuchElementException::class.java) {
            stmt.fetchOne()
        }
    }

    @Test
    fun fetchOneOrNull_notNull() {
        val e = Employee_()
        val employee = entityql.from(e).where { eq(e.employeeId, 1) }.fetchOneOrNull()
        assertNotNull(employee)
    }

    @Test
    fun fetchOneOrNull_null() {
        val e = Employee_()
        val employee = entityql.from(e).where { eq(e.employeeId, 100) }.fetchOneOrNull()
        assertNull(employee)
    }

    @Test
    fun where() {
        val e = Employee_()
        val list = entityql
            .from(e)
            .where {
                eq(e.departmentId, 2)
                isNotNull(e.managerId)
                or {
                    gt(e.salary, Salary("1000"))
                    lt(e.salary, Salary("2000"))
                }
            }
            .fetch()
        assertEquals(10, list.size)
    }

    @Test
    fun where_dynamic() {
        val list = where_dynamic("C%", false)
        assertEquals(3, list.size)
        val list2 = where_dynamic("C%", true)
        assertEquals(1, list2.size)
    }

    @Suppress("SameParameterValue")
    private fun where_dynamic(name: String, enableNameCondition: Boolean): List<Employee> {
        val e = Employee_()
        return entityql
            .from(e)
            .where {
                eq(e.departmentId, 1)
                if (enableNameCondition) {
                    like(e.employeeName, name)
                }
            }
            .fetch()
    }

    @Test
    fun where_in() {
        val e = Employee_()
        val list = entityql
            .from(e)
            .where { `in`(e.employeeId, listOf(2, 3, 4)) }
            .orderBy { asc(e.employeeId) }
            .fetch()
        assertEquals(3, list.size)
    }

    @Test
    fun where_in_subQuery() {
        val e = Employee_()
        val e2 = Employee_()
        val list = entityql
            .from(e)
            .where { `in`(e.employeeId, from(e2).select(e2.managerId)) }
            .orderBy { asc(e.employeeId) }
            .fetch()
        assertEquals(6, list.size)
    }

    @Test
    fun where_exists_subQuery() {
        val e = Employee_()
        val e2 = Employee_()
        val list = entityql
            .from(e)
            .where {
                exists(from(e2).where { eq(e.employeeId, e2.managerId) }.select(e2.managerId))
            }
            .orderBy { asc(e.employeeId) }
            .fetch()
        assertEquals(6, list.size)
    }

    @Test
    fun where_like() {
        val a = Address_()
        val list = entityql.from(a).where { like(a.street, "%1") }.fetch()
        assertEquals(2, list.size)
    }

    @Test
    fun where_like_null() {
        val a = Address_()
        val list = entityql.from(a).where { like(a.street, null) }.fetch()
        assertEquals(15, list.size)
    }

    @Test
    fun innerJoin() {
        val e = Employee_()
        val d = Department_()
        val list = entityql.from(e).innerJoin(d) { eq(e.departmentId, d.departmentId) }.fetch()
        assertEquals(14, list.size)
    }

    @Test
    fun innerJoin_dynamic() {
        val list = innerJoin_dynamic(true)
        assertEquals(13, list.size)
        val list2 = innerJoin_dynamic(false)
        assertEquals(14, list2.size)
    }

    private fun innerJoin_dynamic(join: Boolean): List<Employee> {
        val e = Employee_()
        val e2 = Employee_()
        return entityql
            .from(e)
            .innerJoin(e2) {
                if (join) {
                    eq(e.managerId, e2.employeeId)
                }
            }
            .fetch()
    }

    @Test
    fun leftJoin() {
        val e = Employee_()
        val d = Department_()
        val list = entityql.from(e).leftJoin(d) { eq(e.departmentId, d.departmentId) }.fetch()
        assertEquals(14, list.size)
    }

    @Test
    fun associate() {
        val e = Employee_()
        val d = Department_()
        val list = entityql
            .from(e)
            .innerJoin(d) { eq(e.departmentId, d.departmentId) }
            .where { eq(d.departmentName, "SALES") }
            .associate(e, d) { employee, department ->
                employee.department = department
                department.employeeList += employee
            }
            .fetch()
        assertEquals(6, list.size)
        assertTrue(list.all { it.department?.departmentName == "SALES" })
        assertEquals(list[0].department?.employeeList?.size, 6)
    }

    @Test
    fun associate_dynamic() {
        val list = associate_dynamic(true)
        assertEquals(14, list.size)
        assertTrue(list.stream().allMatch { emp: Employee -> emp.department != null })
        val list2 = associate_dynamic(false)
        assertEquals(14, list2.size)
        assertTrue(list2.stream().allMatch { emp: Employee -> emp.department == null })
    }

    private fun associate_dynamic(join: Boolean): List<Employee> {
        val e = Employee_()
        val d = Department_()
        return entityql
            .from(e)
            .innerJoin(d) {
                if (join) {
                    eq(e.departmentId, d.departmentId)
                }
            }
            .associate(
                e,
                d,
                { employee: Employee, department: Department ->
                    employee.department = department
                    department.employeeList += employee
                },
                AssociationOption.optional()
            )
            .fetch()
    }

    @Test
    fun associate_multi() {
        val e = Employee_()
        val d = Department_()
        val a = Address_()
        val list = entityql
            .from(e)
            .innerJoin(d) { eq(e.departmentId, d.departmentId) }
            .innerJoin(a) { eq(e.addressId, a.addressId) }
            .where { eq(d.departmentName, "SALES") }
            .associate(e, d) { employee, department ->
                employee.department = department
                department.employeeList += employee
            }
            .associate(e, a) { employee, address -> employee.address = address }
            .fetch()
        assertEquals(6, list.size)
        assertTrue(
            list.all { it.department?.departmentName == "SALES" }
        )
        assertEquals(list[0].department?.employeeList?.size, 6)
        assertTrue(list.all { it: Employee -> it.address != null })
    }

    @Test
    fun orderBy() {
        val e = Employee_()
        val list = entityql
            .from(e)
            .orderBy {
                asc(e.departmentId)
                desc(e.salary)
            }
            .fetch()
        assertEquals(14, list.size)
    }

    @Test
    fun asSql() {
        val d = Department_()
        val stmt: KListable<Department> = entityql.from(d).where { eq(d.departmentName, "SALES") }
        val sql = stmt.asSql()
        System.out.printf("Raw SQL      : %s\n", sql.rawSql)
        System.out.printf("Formatted SQL: %s\n", sql.formattedSql)
    }

    @Test
    fun peek() {
        val d = Department_()
        entityql
            .from(d)
            .peek { println(it) }
            .where { eq(d.departmentName, "SALES") }
            .peek { println(it) }
            .orderBy { asc(d.location) }
            .peek { println(it.formattedSql) }
            .fetch()
    }

    @Test
    fun tableName_replacement() {
        val e = Employee_()
        val d = Department_("DEPARTMENT_ARCHIVE")
        val department = Department()
        department.departmentId = 1
        department.departmentNo = 1
        department.departmentName = "aaa"
        department.location = "bbb"
        val result = entityql.insert(d, department).execute()
        assertEquals(1, result.count)
        val list = entityql
            .from(d)
            .innerJoin(e) { eq(d.departmentId, e.departmentId) }
            .associate(d, e) { dept, employee: Employee -> dept.employeeList += employee }
            .fetch()
        assertEquals(1, list.size)
        assertEquals(3, list[0].employeeList.size)
    }

    @Test
    fun select() {
        val e = Employee_()
        val list = entityql.from(e).select(e).fetch()
        assertEquals(14, list.size)
    }

    @Test
    fun select_join() {
        val e = Employee_()
        val d = Department_()
        val list = entityql
            .from(e)
            .innerJoin(d) { eq(e.departmentId, d.departmentId) }
            .select(d)
            .fetch()
        assertEquals(3, list.size)
    }

    @Test
    fun selectTo() {
        val e = Employee_()
        val list = entityql.from(e).selectTo(e, e.employeeName).fetch()
        assertEquals(14, list.size)
        assertTrue(list.map { it.employeeId }.all { it != null })
        assertTrue(list.map { it.employeeName }.all { it != null })
    }

    @Test
    fun selectTo_associate() {
        val e = Employee_()
        val d = Department_()
        val list = entityql
            .from(e)
            .innerJoin(d) { eq(e.departmentId, d.departmentId) }
            .associate(e, d) { employee, department -> employee.department = department }
            .selectTo(e, e.employeeName)
            .fetch()
        assertEquals(14, list.size)
        assertTrue(list.map { it.employeeId }.all { it != null })
        assertTrue(list.map { it.employeeName }.all { it != null })
        assertTrue(list.map { it.department }.all { it != null })
    }

    @Test
    fun expressions_literal_int() {
        val e = Employee_()
        val employee = entityql.from(e).where { eq(e.employeeId, KExpressions.literal(1)) }.fetchOne()
        assertNotNull(employee)
    }
}

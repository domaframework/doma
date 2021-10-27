package org.seasar.doma.it.criteria

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.seasar.doma.it.Dbms
import org.seasar.doma.it.IntegrationTestEnvironment
import org.seasar.doma.it.Run
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.SqlLogType
import org.seasar.doma.jdbc.criteria.declaration.JoinDeclaration
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration
import org.seasar.doma.jdbc.criteria.expression.Expressions
import org.seasar.doma.jdbc.criteria.expression.SelectExpression
import org.seasar.doma.jdbc.criteria.statement.EmptyWhereClauseException
import org.seasar.doma.kotlin.jdbc.criteria.KNativeSql
import org.seasar.doma.kotlin.jdbc.criteria.expression.KExpressions
import java.math.BigDecimal

@ExtendWith(IntegrationTestEnvironment::class)
class KNativeSqlUpdateTest(config: Config) {

    private val nativeSql = KNativeSql(config)

    @Test
    fun settings() {
        val e = Employee_()
        val count = nativeSql
            .update(
                e
            ) {
                comment = "update all"
                queryTimeout = 1000
                sqlLogType = SqlLogType.RAW
                allowEmptyWhere = true
                batchSize = 20
            }
            .set { value(e.employeeName, "aaa") }
            .execute()
        assertEquals(14, count)
    }

    @Test
    fun where() {
        val e = Employee_()
        val count = nativeSql
            .update(e)
            .set { value(e.departmentId, 3) }
            .where {
                isNotNull(e.managerId)
                ge(e.salary, Salary("2000"))
            }
            .execute()
        assertEquals(5, count)
    }

    @Test
    fun where_empty() {
        val e = Employee_()
        val ex = assertThrows(
            EmptyWhereClauseException::class.java
        ) { nativeSql.update(e).set { value(e.departmentId, 3) }.execute() }
        println(ex.message)
    }

    @Test
    fun where_empty_allowEmptyWhere_enabled() {
        val e = Employee_()
        val count = nativeSql
            .update(e) { allowEmptyWhere = true }
            .set { value(e.departmentId, 3) }
            .execute()
        assertEquals(14, count)
    }

    @Test
    fun expression_add() {
        val e = Employee_()
        val count = nativeSql
            .update(e)
            .set { value(e.version, Expressions.add(e.version, 10)) }
            .where { eq(e.employeeId, 1) }
            .execute()
        assertEquals(1, count)
        val employee = nativeSql.from(e).where { eq(e.employeeId, 1) }.fetchOne()
        checkNotNull(employee)
        assertEquals(11, employee.version)
    }

    @Test
    fun expression_concat() {
        val e = Employee_()
        val count = nativeSql
            .update(e)
            .set { value(e.employeeName, KExpressions.concat("[", KExpressions.concat(e.employeeName, "]"))) }
            .where { eq(e.employeeId, 1) }
            .execute()
        assertEquals(1, count)
    }

    @Test
    fun expressions() {
        val e = Employee_()
        val count = nativeSql
            .update(e)
            .set {
                value(e.employeeName, Expressions.concat("[", Expressions.concat(e.employeeName, "]")))
                value(e.version, Expressions.add(e.version, Expressions.literal(1)))
            }
            .where { eq(e.employeeId, 1) }
            .execute()
        assertEquals(1, count)
    }

    @Test
    @Run(unless = [Dbms.MYSQL])
    fun expression_select() {
        val e = Employee_()
        val e2 = Employee_()
        val d = Department_()
        val subSelect = Expressions.select { c: SelectExpression.Declaration ->
            c.from(e2)
                .innerJoin(d) { on: JoinDeclaration -> on.eq(e2.departmentId, d.departmentId) }
                .where { cc: WhereDeclaration -> cc.eq(e.departmentId, d.departmentId) }
                .groupBy(d.departmentId)
                .select(Expressions.max(e2.salary))
        }
        val count = nativeSql
            .update(e)
            .set { value(e.salary, subSelect) }
            .where { eq(e.employeeId, 1) }
            .peek { println(it) }
            .execute()
        assertEquals(1, count)
        val salary = nativeSql.from(e).where { eq(e.employeeId, 1) }.select(e.salary).fetchOne()
        checkNotNull(salary)
        assertEquals(0, BigDecimal("3000").compareTo(salary.value))
    }
}

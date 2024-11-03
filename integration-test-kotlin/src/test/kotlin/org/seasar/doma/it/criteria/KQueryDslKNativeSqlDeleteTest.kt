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
import org.seasar.doma.jdbc.criteria.statement.EmptyWhereClauseException
import org.seasar.doma.kotlin.jdbc.criteria.KQueryDsl

@ExtendWith(IntegrationTestEnvironment::class)
@Run(unless = [Dbms.SQLITE])
class KQueryDslKNativeSqlDeleteTest(config: Config) {

    private val dsl = KQueryDsl(config)

    @Test
    fun settings() {
        val e = Employee_()
        val count = dsl
            .delete(e) {
                comment = "delete all"
                queryTimeout = 1000
                sqlLogType = SqlLogType.RAW
                allowEmptyWhere = true
                batchSize = 20
            }.where {}
            .execute()
        assertEquals(14, count)
    }

    @Test
    fun where() {
        val e = Employee_()
        val count = dsl.delete(e).where { ge(e.salary, Salary("2000")) }.execute()
        assertEquals(6, count)
    }

    @Test
    fun where_empty() {
        val e = Employee_()
        val ex = assertThrows(EmptyWhereClauseException::class.java) { dsl.delete(e).where {}.execute() }
        println(ex.message)
    }

    @Test
    fun where_empty_allowEmptyWhere_enabled() {
        val e = Employee_()
        val count = dsl.delete(e) { allowEmptyWhere = true }.where {}.execute()
        assertEquals(14, count)
    }

    @Test
    fun all() {
        val e = Employee_()
        val count = dsl.delete(e).all().execute()
        assertEquals(14, count)
    }
}

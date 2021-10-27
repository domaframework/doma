package org.seasar.doma.it.criteria

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.seasar.doma.it.IntegrationTestEnvironment
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.SqlLogType
import org.seasar.doma.jdbc.criteria.statement.EmptyWhereClauseException
import org.seasar.doma.kotlin.jdbc.criteria.KNativeSql

@ExtendWith(IntegrationTestEnvironment::class)
class KNativeSqlDeleteTest(config: Config) {

    private val nativeSql = KNativeSql(config)

    @Test
    fun settings() {
        val e = Employee_()
        val count = nativeSql
            .delete(e) {
                comment = "delete all"
                queryTimeout = 1000
                sqlLogType = SqlLogType.RAW
                allowEmptyWhere = true
                batchSize = 20
            }
            .execute()
        assertEquals(14, count)
    }

    @Test
    fun where() {
        val e = Employee_()
        val count = nativeSql.delete(e).where { ge(e.salary, Salary("2000")) }.execute()
        assertEquals(6, count)
    }

    @Test
    fun where_empty() {
        val e = Employee_()
        val ex = assertThrows(EmptyWhereClauseException::class.java) { nativeSql.delete(e).execute() }
        println(ex.message)
    }

    @Test
    fun where_empty_allowEmptyWhere_enabled() {
        val e = Employee_()
        val count = nativeSql.delete(e) { allowEmptyWhere = true }.execute()
        assertEquals(14, count)
    }
}

/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.it.criteria

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.seasar.doma.it.IntegrationTestEnvironment
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.SqlLogType
import org.seasar.doma.jdbc.criteria.statement.EmptyWhereClauseException
import org.seasar.doma.kotlin.jdbc.criteria.KQueryDsl

@ExtendWith(IntegrationTestEnvironment::class)
class KQueryDslSqlDeleteTest(config: Config) {

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

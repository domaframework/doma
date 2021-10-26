package org.seasar.doma.it.criteria

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.seasar.doma.it.IntegrationTestEnvironment
import org.seasar.doma.jdbc.Config
import org.seasar.doma.kotlin.jdbc.criteria.KEntityql

@ExtendWith(IntegrationTestEnvironment::class)
class KEntityqlBatchInsertTest(config: Config) {

    private val entityql = KEntityql(config)

    @Test
    fun test() {
        val d = Department_()
        val department = Department()
        department.departmentId = 99
        department.departmentNo = 99
        department.departmentName = "aaa"
        department.location = "bbb"

        val department2 = Department()
        department2.departmentId = 100
        department2.departmentNo = 100
        department2.departmentName = "ccc"
        department2.location = "ddd"

        val departments = listOf(department, department2)
        val result = entityql.insert(d, departments).execute()
        assertEquals(departments, result.entities)

        val ids = departments.map { it.departmentId }
        val departments2 = entityql
            .from(d)
            .where { `in`(d.departmentId, ids) }
            .orderBy { asc(d.departmentId) }
            .fetch()
        assertEquals(2, departments2.size)
    }

    @Test
    fun empty() {
        val e = Employee_()
        val result = entityql.insert(e, emptyList()).execute()
        assertTrue(result.entities.isEmpty())
    }
}

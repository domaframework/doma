package org.seasar.doma.it.criteria

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.seasar.doma.it.IntegrationTestEnvironment
import org.seasar.doma.jdbc.Config
import org.seasar.doma.kotlin.jdbc.criteria.KQueryDsl

@ExtendWith(IntegrationTestEnvironment::class)
class KQueryDslEntityInsertTest(config: Config) {

    private val dsl = KQueryDsl(config)

    @Test
    fun test() {
        val d = Department_()
        val department = Department()
        department.departmentId = 99
        department.departmentNo = 99
        department.departmentName = "aaa"
        department.location = "bbb"
        val result = dsl.insert(d).single(department).execute()
        assertEquals(department, result.entity)
        val department2 = dsl.from(d).where { eq(d.departmentId, department.departmentId) }.fetchOne()
        checkNotNull(department2)
        assertEquals("aaa", department2.departmentName)
    }
}

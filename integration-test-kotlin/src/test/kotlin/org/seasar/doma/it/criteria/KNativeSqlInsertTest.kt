package org.seasar.doma.it.criteria

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.seasar.doma.it.IntegrationTestEnvironment
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.SqlLogType
import org.seasar.doma.kotlin.jdbc.criteria.KNativeSql

@ExtendWith(IntegrationTestEnvironment::class)
class KNativeSqlInsertTest(config: Config) {

    private val nativeSql = KNativeSql(config)

    @Test
    fun settings() {
        val d = Department_()
        val count = nativeSql
            .insert(
                d
            ) {
                comment = "insert department"
                queryTimeout = 1000
                sqlLogType = SqlLogType.RAW
                batchSize = 20
            }
            .values {
                value(d.departmentId, 99)
                value(d.departmentNo, 99)
                value(d.departmentName, "aaa")
                value(d.location, "bbb")
                value(d.version, 1)
            }
            .execute()
        assertEquals(1, count)
    }

    @Test
    fun insert() {
        val d = Department_()
        val count = nativeSql
            .insert(d)
            .values {
                value(d.departmentId, 99)
                value(d.departmentNo, 99)
                value(d.departmentName, "aaa")
                value(d.location, "bbb")
                value(d.version, 1)
            }
            .execute()
        assertEquals(1, count)
    }

    @Test
    fun insert_select_entity() {
        val da = DepartmentArchive_()
        val d = Department_()
        val count = nativeSql
            .insert(da)
            .select { from(d).where { `in`(d.departmentId, listOf(1, 2)) } }
            .execute()
        assertEquals(2, count)
    }

    @Test
    fun insert_select_properties() {
        val da = DepartmentArchive_()
        val d = Department_()
        val count = nativeSql
            .insert(da)
            .select {
                from(d).where { `in`(d.departmentId, listOf(1, 2)) }.select(
                    d.departmentId,
                    d.departmentNo,
                    d.departmentName,
                    d.location,
                    d.version
                )
            }
            .execute()
        assertEquals(2, count)
    }

    @Test
    fun insert_select_using_same_entityMetamodel() {
        val da = Department_("DEPARTMENT_ARCHIVE")
        val d = Department_()
        val count = nativeSql.insert(da).select { from(d) }.execute()
        assertEquals(4, count)
    }
}

package example

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.seasar.doma.criteria.sql
import org.seasar.doma.internal.util.AssertionUtil.assertEquals
import org.seasar.doma.jdbc.Config

@ExtendWith(Env::class)
class SqlDeclarationTest(private val config: Config) {

    @Test
    fun test() {
        val query = sql {
            from(::_Employee) { e ->
                where {
                    eq(e.employeeId, 1)
                }
                select(e.employeeName, e.employeeNo) {
                    it[e.employeeNo] to it[e.employeeName]
                }
            }
        }
        val list = query.execute(config)
        assertEquals(1, list.size)
        assertEquals(7369 to "SMITH", list[0])
    }

    @Test
    fun count() {
        val query = sql {
            from(::_Employee) { e ->
                select(count(e.employeeId)) {
                    it[count(e.employeeId)]
                }
            }
        }
        val list = query.execute(config)
        assertEquals(1, list.size)
        assertEquals(14, list[0])
    }

    @Test
    fun groupBy() {
        val query = sql {
            from(::_Employee) { e ->
                val d = leftJoin(::_Department) { d ->
                    eq(e.departmentId, d.departmentId)
                }
                groupBy(d.departmentId, d.departmentName)
                orderBy { asc(e.departmentId) }
                select(d.departmentId, d.departmentName, count(e.employeeId)) {
                    Triple(it[d.departmentId], it[d.departmentName], it[count(e.employeeId)])
                }
            }
        }
        val list = query.execute(config)
        assertEquals(3, list.size)
        assertEquals(Triple(1, "ACCOUNTING", 3), list[0])
        assertEquals(Triple(2, "RESEARCH", 5), list[1])
        assertEquals(Triple(3, "SALES", 6), list[2])
    }

    @Test
    fun having() {
        val query = sql {
            from(::_Employee) { e ->
                val d = leftJoin(::_Department) { d ->
                    eq(e.departmentId, d.departmentId)
                }
                groupBy(d.departmentId, d.departmentName)
                having {
                    gt(count(e.employeeId), 3)
                }
                orderBy { asc(e.departmentId) }
                select(d.departmentId, d.departmentName, count(e.employeeId)) {
                    Triple(it[d.departmentId], it[d.departmentName], it[count(e.employeeId)])
                }
            }
        }
        val list = query.execute(config)
        assertEquals(2, list.size)
        assertEquals(Triple(2, "RESEARCH", 5), list[0])
        assertEquals(Triple(3, "SALES", 6), list[1])
    }

    @Test
    fun insert() {
        val query = sql {
            insert.into(::_Department) { d ->
                values {
                    value(d.departmentId, 99)
                    value(d.departmentNo, 99)
                    value(d.departmentName, "MARKETING")
                    value(d.location, "TOKYO")
                }
            }
        }
        val count = query.execute(config)
        assertEquals(1, count)
    }

    @Test
    fun delete() {
        val query = sql {
            delete.from(::_Employee) { e ->
                where {
                    eq(e.departmentId, 1)
                }
            }
        }
        val count = query.execute(config)
        assertEquals(3, count)
    }

    @Test
    fun delete_in_single_subQuery() {
        val query = sql {
            delete.from(::_Employee) { e ->
                where {
                    `in`(e.departmentId) {
                        from(::_Department) { d ->
                            where {
                                eq(d.departmentName, "ACCOUNTING")
                            }
                            select(d.departmentId)
                        }
                    }
                }
            }
        }
        val count = query.execute(config)
        assertEquals(3, count)
    }

    @Test
    fun update() {
        val query = sql {
            update(::_Department) { e ->
                set {
                    value(e.location, "TOKYO")
                }
                where {
                    eq(e.departmentId, 1)
                }
            }
        }
        val count = query.execute(config)
        assertEquals(1, count)
    }
}

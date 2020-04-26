package example

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.seasar.doma.criteria.sql
import org.seasar.doma.internal.util.AssertionUtil.assertEquals
import org.seasar.doma.jdbc.Config

@ExtendWith(Env::class)
class SqlDslTest(private val config: Config) {

    @Test
    fun select_single() {
        val query = sql {
            from(::_Employee) { e ->
                where {
                    e.employeeId eq 1
                }
                select(e.employeeName)
            }
        }
        val list = query.execute(config)
        assertEquals(1, list.size)
        assertEquals("SMITH", list[0])
    }

    @Test
    fun select_pair() {
        val query = sql {
            from(::_Employee) { e ->
                where {
                    e.employeeId eq 1
                }
                select(e.employeeNo, e.employeeName)
            }
        }
        val list = query.execute(config)
        assertEquals(1, list.size)
        assertEquals(7369 to "SMITH", list[0])
    }

    @Test
    fun select_triple() {
        val query = sql {
            from(::_Employee) { e ->
                where {
                    e.employeeId eq 1
                }
                select(e.employeeNo, e.employeeName, e.departmentId)
            }
        }
        val list = query.execute(config)
        assertEquals(1, list.size)
        assertEquals(Triple(7369, "SMITH", 2), list[0])
    }

    @Test
    fun select_with_mapper() {
        val query = sql {
            from(::_Employee) { e ->
                where {
                    e.employeeId eq 1
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
                    e.departmentId eq d.departmentId
                }
                groupBy(d.departmentId, d.departmentName)
                orderBy { e.departmentId.asc() }
                select(d.departmentId, d.departmentName, count(e.employeeId)) {
                    Triple(it[d.departmentId], it[d.departmentName], it[count(e.employeeId)])
                }
            }
        }
        val list = query.execute(config)
        assertEquals(3, list.size)
        assertEquals(Triple(1, "ACCOUNTING", 3L), list[0])
        assertEquals(Triple(2, "RESEARCH", 5L), list[1])
        assertEquals(Triple(3, "SALES", 6L), list[2])
    }

    @Test
    fun having_avg() {
        val query = sql {
            from(::_Employee) { e ->
                val d = leftJoin(::_Department) { d ->
                    e.departmentId eq d.departmentId
                }
                orderBy { d.departmentId.asc() }
                groupBy(d.departmentName)
                having {
                    avg(e.salary) gt Salary("2000")
                }
                select(d.departmentName, avg(e.salary))
            }
        }
        val list = query.execute(config)
        assertEquals(2, list.size)
        assertEquals("ACCOUNTING", list[0].first)
        assertEquals("RESEARCH", list[1].first)
    }

    @Test
    fun having_count_property() {
        val query = sql {
            from(::_Employee) { e ->
                val d = leftJoin(::_Department) { d ->
                    e.departmentId eq d.departmentId
                }
                groupBy(d.departmentId, d.departmentName)
                having {
                    count(e.employeeName) gt 3
                }
                orderBy { e.departmentId.asc() }
                select(d.departmentId, d.departmentName, count(e.employeeName)) {
                    Triple(it[d.departmentId], it[d.departmentName], it[count(e.employeeName)])
                }
            }
        }
        val list = query.execute(config)
        assertEquals(2, list.size)
        assertEquals(Triple(2, "RESEARCH", 5L), list[0])
        assertEquals(Triple(3, "SALES", 6L), list[1])
    }

    @Test
    fun having_count_asterisk() {
        val query = sql {
            from(::_Employee) { e ->
                val d = leftJoin(::_Department) { d ->
                    e.departmentId eq d.departmentId
                }
                groupBy(d.departmentId, d.departmentName)
                having {
                    count(`*`) gt 3
                }
                orderBy { e.departmentId.asc() }
                select(d.departmentId, d.departmentName, count(`*`)) {
                    Triple(it[d.departmentId], it[d.departmentName], it[count(`*`)])
                }
            }
        }
        val list = query.execute(config)
        assertEquals(2, list.size)
        assertEquals(Triple(2, "RESEARCH", 5L), list[0])
        assertEquals(Triple(3, "SALES", 6L), list[1])
    }

    @Test
    fun having_max() {
        val query = sql {
            from(::_Employee) { e ->
                val d = leftJoin(::_Department) { d ->
                    e.departmentId eq d.departmentId
                }
                orderBy { d.departmentId.asc() }
                groupBy(d.departmentName)
                having {
                    max(e.salary) ge Salary("3000")
                }
                select(d.departmentName, max(e.salary))
            }
        }
        val list = query.execute(config)
        assertEquals(2, list.size)
        assertEquals("ACCOUNTING", list[0].first)
        assertEquals("RESEARCH", list[1].first)
    }

    @Test
    fun having_min() {
        val query = sql {
            from(::_Employee) { e ->
                val d = leftJoin(::_Department) { d ->
                    e.departmentId eq d.departmentId
                }
                orderBy { d.departmentId.asc() }
                groupBy(d.departmentName)
                having {
                    min(e.salary) le Salary("1000")
                }
                select(d.departmentName, min(e.salary))
            }
        }
        val list = query.execute(config)
        assertEquals(2, list.size)
        assertEquals("RESEARCH", list[0].first)
        assertEquals("SALES", list[1].first)
    }

    @Test
    fun having_sum() {
        val query = sql {
            from(::_Employee) { e ->
                val d = leftJoin(::_Department) { d ->
                    e.departmentId eq d.departmentId
                }
                orderBy { d.departmentId.asc() }
                groupBy(d.departmentName)
                having {
                    sum(e.salary) gt Salary("9000")
                }
                select(d.departmentName, sum(e.salary))
            }
        }
        val list = query.execute(config)
        assertEquals(2, list.size)
        assertEquals("RESEARCH", list[0].first)
        assertEquals("SALES", list[1].first)
    }

    @Test
    fun union() {
        val query = sql {
            val x = from(::_Department) { d ->
                where {
                    d.departmentId eq 1
                }
                select(d.departmentName) {
                    it[d.departmentName]
                }
            }
            val y = from(::_Employee) { e ->
                where {
                    e.employeeId eq 1
                }
                select(e.employeeName) {
                    it[e.employeeName]
                }
            }
            x union y
        }
        val list = query.execute(config).sortedBy { it }
        assertEquals(2, list.size)
        assertEquals("ACCOUNTING", list[0])
        assertEquals("SMITH", list[1])
    }

    @Test
    fun unionAll() {
        val query = sql {
            val x = from(::_Department) { d ->
                where {
                    d.departmentId eq 1
                }
                select(d.departmentName) {
                    it[d.departmentName]
                }
            }
            val y = from(::_Department) { d ->
                where {
                    d.departmentId eq 1
                }
                select(d.departmentName) {
                    it[d.departmentName]
                }
            }
            x unionAll y
        }
        val list = query.execute(config)
        assertEquals(2, list.size)
        assertEquals("ACCOUNTING", list[0])
        assertEquals("ACCOUNTING", list[1])
    }

    @Test
    fun insert() {
        val query = sql {
            insert.into(::_Department) { d ->
                values {
                    it[d.departmentId] = 99
                    it[d.departmentNo] = 99
                    it[d.departmentName] = "MARKETING"
                    it[d.location] = "TOKYO"
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
                    e.departmentId eq 1
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
                    e.departmentId `in` {
                        from(::_Department) { d ->
                            where {
                                d.departmentName eq "ACCOUNTING"
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
                    it[e.location] = "TOKYO"
                }
                where {
                    e.departmentId eq 1
                }
            }
        }
        val count = query.execute(config)
        assertEquals(1, count)
    }
}

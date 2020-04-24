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
            select.from(::_Employee) { e ->
                where {
                    eq(e.employeeId, 1)
                }
                map(e.employeeName, e.employeeNo) {
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
            select.from(::_Employee) { e ->
                map(count(e.employeeId)) {
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
            select.from(::_Employee) { e ->
                val d = leftJoin(::_Department) { d ->
                    eq(e.departmentId, d.departmentId)
                }
                groupBy(d.departmentId, d.departmentName)
                orderBy { asc(e.departmentId) }
                map(d.departmentId, d.departmentName, count(e.employeeId)) {
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
            select.from(::_Employee) { e ->
                val d = leftJoin(::_Department) { d ->
                    eq(e.departmentId, d.departmentId)
                }
                groupBy(d.departmentId, d.departmentName)
                having {
                    gt(count(e.employeeId), 3)
                }
                orderBy { asc(e.departmentId) }
                map(d.departmentId, d.departmentName, count(e.employeeId)) {
                    Triple(it[d.departmentId], it[d.departmentName], it[count(e.employeeId)])
                }
            }
        }
        val list = query.execute(config)
        assertEquals(2, list.size)
        assertEquals(Triple(2, "RESEARCH", 5), list[0])
        assertEquals(Triple(3, "SALES", 6), list[1])
    }
}

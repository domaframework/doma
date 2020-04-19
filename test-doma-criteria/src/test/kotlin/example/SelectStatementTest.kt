package example

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.seasar.doma.criteria.select
import org.seasar.doma.jdbc.Config

@ExtendWith(Env::class)
class SelectStatementTest(private val config: Config) {

    @Test
    fun test() {
        val query = select(::_Employee) {}
        val list = query.execute(config)
        println(list.size)
    }

    @Test
    fun where() {
        val query = select(::_Employee) { e ->
            where {
                eq(e.employeeName, "SMITH")
            }
        }
        val list = query.execute(config)
        assertEquals(1, list.size)
        assertEquals("SMITH", list[0].employeeName)
    }

    @Test
    fun where_in() {
        val query = select(::_Employee) { e ->
            where {
                `in`(e.employeeId, listOf(3, 4, 5))
            }
            orderBy {
                asc(e.employeeId)
            }
        }
        val list = query.execute(config)
        assertEquals(3, list.size)
        assertEquals(3, list[0].employeeId)
        assertEquals(4, list[1].employeeId)
        assertEquals(5, list[2].employeeId)
    }

    @Test
    fun where_isNull() {
        val query = select(::_Employee) { e ->
            where {
                eq(e.managerId, null)
            }
        }
        val list = query.execute(config)
        assertEquals(1, list.size)
        assertEquals("KING", list[0].employeeName)
    }

    @Test
    fun where_isNotNull() {
        val query = select(::_Employee) { e ->
            where {
                ne(e.managerId, null)
            }
        }
        val list = query.execute(config)
        assertEquals(13, list.size)
        assertTrue(list.none { it.employeeName == "KING" })
    }

    @Test
    fun leftJoin_manyToOne() {
        val query = select(::_Employee) { e ->
            leftJoin(::_Department) { d ->
                eq(e.departmentId, d.departmentId)
            }
        }
        val list = query.execute(config)
        assertEquals(14, list.size)
    }

    @Test
    fun leftJoin_manyToOne_associate() {
        val query = select(::_Employee) { e ->
            val d = leftJoin(::_Department) { d ->
                eq(e.departmentId, d.departmentId)
            }
            where {
                eq(e.employeeName, "SMITH")
            }
            associate(e, d) { employee, department ->
                employee.department = department
            }
        }
        val list = query.execute(config)
        assertEquals(1, list.size)
        assertEquals("RESEARCH", list[0].department.departmentName)
    }

    @Test
    fun leftJoin_oneToMany() {
        val query = select(::_Department) { d ->
            leftJoin(::_Employee) { e ->
                eq(d.departmentId, e.departmentId)
            }
        }
        val list = query.execute(config)
        assertEquals(4, list.size)
    }

    @Test
    fun leftJoin_oneToMany_association() {
        val query = select(::_Department) { d ->
            val e = leftJoin(::_Employee) { e ->
                eq(d.departmentId, e.departmentId)
            }
            associate(d, e) { department, employee ->
                department.employeeList.add(employee)
            }
        }
        val list = query.execute(config)
        assertEquals(4, list.size)
        assertEquals(3, list[0].employeeList.size)
        assertEquals(5, list[1].employeeList.size)
        assertEquals(6, list[2].employeeList.size)
        assertEquals(0, list[3].employeeList.size)
    }

    @Test
    fun selfJoin() {
        val query = select(::_Employee) { e ->
            val m = leftJoin(::_Employee) { m ->
                eq(e.managerId, m.employeeId)
            }
            associate(e, m) { employee, manager ->
                employee.manager = manager
            }
        }
        val list = query.execute(config)
        assertEquals(14, list.size)
        assertTrue(list.filter { it.employeeName == "KING" }.all { it.manager == null })
        assertTrue(list.filterNot { it.employeeName == "KING" }.all { it.manager != null })
    }
}

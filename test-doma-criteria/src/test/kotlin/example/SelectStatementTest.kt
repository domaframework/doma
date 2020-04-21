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
    fun where_in_pair() {
        val query = select(::_Employee) { e ->
            where {
                `in`(e.departmentId to e.version, listOf(1 to 1))
            }
            orderBy {
                asc(e.employeeId)
            }
        }
        val list = query.execute(config)
        assertEquals(3, list.size)
        list.forEach {
            println(it.employeeName)
        }
    }

    @Test
    fun where_in_select() {
        val query = select(::_Employee) { e ->
            where {
                `in`(e.managerId,
                        selectSingle({ it.employeeId }, ::_Employee) {})
            }
        }
        val list = query.execute(config)
        assertEquals(13, list.size)
        assertTrue(list.none { it.employeeName == "KING" })
    }

    @Test
    fun where_in_select_pair() {
        val query = select(::_Employee) { e ->
            where {
                `in`(e.managerId to e.departmentId,
                        selectPair({ it.employeeId to it.departmentId }, ::_Employee) {})
            }
        }
        val list = query.execute(config)
        assertEquals(11, list.size)
    }

    @Test
    fun where_notIn() {
        val query = select(::_Department) { d ->
            where {
                notIn(d.departmentId, listOf(1, 3))
            }
            orderBy {
                asc(d.departmentId)
            }
        }
        val list = query.execute(config)
        assertEquals(2, list.size)
        assertEquals(2, list[0].departmentId)
        assertEquals(4, list[1].departmentId)
    }

    @Test
    fun where_notIn_select() {
        val query = select(::_Department) { d ->
            where {
                notIn(d.departmentId,
                        selectSingle({ it.departmentId }, ::_Employee) {})
            }
        }
        val list = query.execute(config)
        assertEquals(1, list.size)
        assertEquals("OPERATIONS", list[0].departmentName)
    }

    @Test
    fun where_notIn_select_pair() {
        val query = select(::_Department) { d ->
            where {
                notIn(d.departmentId to d.version,
                        selectPair({ it.departmentId to it.version }, ::_Employee) {})
            }
        }
        val list = query.execute(config)
        assertEquals(1, list.size)
        assertEquals("OPERATIONS", list[0].departmentName)
    }

    @Test
    fun where_exists() {
        val query = select(::_Employee) { e ->
            where {
                exists(::_Employee) { e2 ->
                    where {
                        eq(e.managerId, e2.employeeId)
                    }
                }
            }
        }
        val list = query.execute(config)
        assertEquals(13, list.size)
        assertTrue(list.none { it.employeeName == "KING" })
    }

    @Test
    fun where_notExists() {
        val query = select(::_Employee) { e ->
            where {
                notExists(::_Employee) { e2 ->
                    where {
                        eq(e.managerId, e2.employeeId)
                    }
                }
            }
        }
        val list = query.execute(config)
        assertEquals(1, list.size)
        assertTrue(list.all { it.employeeName == "KING" })
    }

    @Test
    fun where_isNull() {
        val query = select(::_Employee) { e ->
            where {
                eq(e.managerId, null as Int?)
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
                ne(e.managerId, null as Int?)
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

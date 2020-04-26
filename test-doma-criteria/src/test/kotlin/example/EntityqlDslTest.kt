package example

import java.util.Optional
import java.util.OptionalInt
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.seasar.doma.criteria.entityql
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.SqlLogType

@ExtendWith(Env::class)
class EntityqlDslTest(private val config: Config) {

    @Test
    fun test() {
        val query = entityql { from(::_Employee) {} }
        val list = query.execute(config)
        println(list.size)
    }

    @Test
    fun where() {
        val query = entityql {
            from(::_Employee) { e ->
                where {
                    e.employeeName eq "SMITH"
                }
            }
        }
        val list = query.execute(config)
        assertEquals(1, list.size)
        assertEquals("SMITH", list[0].employeeName)
    }

    @Test
    fun where_in() {
        val query = entityql {
            from(::_Employee) { e ->
                where {
                    e.employeeId `in` listOf(3, 4, 5)
                }
                orderBy {
                    e.employeeId.asc()
                }
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
        val query = entityql {
            from(::_Employee) { e ->
                where {
                    (e.departmentId to e.version) `in` listOf(1 to 1)
                }
                orderBy {
                    e.employeeId.asc()
                }
            }
        }
        val list = query.execute(config)
        assertEquals(3, list.size)
        list.forEach {
            println(it.employeeName)
        }
    }

    @Test
    fun where_in_single_subQuery() {
        val query = entityql {
            from(::_Employee) { e ->
                where {
                    e.managerId `in` {
                        from(::_Employee) {
                            select(it.employeeId)
                        }
                    }
                }
            }
        }
        val list = query.execute(config)
        assertEquals(13, list.size)
        assertTrue(list.none { it.employeeName == "KING" })
    }

    @Test
    fun where_in_pair_subQuery() {
        val query = entityql {
            from(::_Employee) { e ->
                where {
                    (e.managerId to e.departmentId) `in` {
                        from(::_Employee) {
                            select(it.employeeId, it.departmentId)
                        }
                    }
                }
            }
        }
        val list = query.execute(config)
        assertEquals(11, list.size)
    }

    @Test
    fun where_notIn() {
        val query = entityql {
            from(::_Department) { d ->
                where {
                    d.departmentId notIn listOf(1, 3)
                }
                orderBy {
                    d.departmentId.asc()
                }
            }
        }
        val list = query.execute(config)
        assertEquals(2, list.size)
        assertEquals(2, list[0].departmentId)
        assertEquals(4, list[1].departmentId)
    }

    @Test
    fun where_notIn_single_subQuery() {
        val query = entityql {
            from(::_Department) { d ->
                where {
                    d.departmentId notIn {
                        from(::_Employee) { e ->
                            select(e.departmentId)
                        }
                    }
                }
            }
        }
        val list = query.execute(config)
        assertEquals(1, list.size)
        assertEquals("OPERATIONS", list[0].departmentName)
    }

    @Test
    fun where_notIn_pair_subQuery() {
        val query = entityql {
            from(::_Department) { d ->
                where {
                    (d.departmentId to d.version) notIn {
                        from(::_Employee) {
                            select(it.departmentId, it.version)
                        }
                    }
                }
            }
        }
        val list = query.execute(config)
        assertEquals(1, list.size)
        assertEquals("OPERATIONS", list[0].departmentName)
    }

    @Test
    fun where_exists() {
        val query = entityql {
            from(::_Employee) { e ->
                where {
                    exists {
                        from(::_Employee) { e2 ->
                            where {
                                e.managerId eq e2.employeeId
                            }
                        }
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
        val query = entityql {
            from(::_Employee) { e ->
                where {
                    notExists {
                        from(::_Employee) { e2 ->
                            where {
                                e.managerId eq e2.employeeId
                            }
                        }
                    }
                }
            }
        }
        val list = query.execute(config)
        assertEquals(1, list.size)
        assertTrue(list.all { it.employeeName == "KING" })
    }

    @Test
    fun where_eq_null() {
        val query = entityql {
            from(::_Employee) { e ->
                where {
                    e.managerId eq null as Int?
                }
            }
        }
        val list = query.execute(config)
        assertEquals(1, list.size)
        assertEquals("KING", list[0].employeeName)
    }

    @Test
    fun where_ne_null() {
        val query = entityql {
            from(::_Employee) { e ->
                where {
                    e.managerId ne null as Int?
                }
            }
        }
        val list = query.execute(config)
        assertEquals(13, list.size)
        assertTrue(list.none { it.employeeName == "KING" })
    }

    @Test
    fun where_isNull() {
        val query = entityql {
            from(::_Employee) { e ->
                where {
                    e.managerId.isNull()
                }
            }
        }
        val list = query.execute(config)
        assertEquals(1, list.size)
        assertEquals("KING", list[0].employeeName)
    }

    @Test
    fun where_isNotNull() {
        val query = entityql {
            from(::_Employee) { e ->
                where {
                    e.managerId.isNotNull()
                }
            }
        }
        val list = query.execute(config)
        assertEquals(13, list.size)
        assertTrue(list.none { it.employeeName == "KING" })
    }

    @Test
    fun leftJoin_manyToOne() {
        val query = entityql {
            from(::_Employee) { e ->
                leftJoin(::_Department) { d ->
                    e.departmentId eq d.departmentId
                }
            }
        }
        val list = query.execute(config)
        assertEquals(14, list.size)
    }

    @Test
    fun leftJoin_manyToOne_associate() {
        val query = entityql {
            from(::_Employee) { e ->
                val d = leftJoin(::_Department) { d ->
                    e.departmentId eq d.departmentId
                }
                where {
                    e.employeeName eq "SMITH"
                }
                associate(e, d) { employee, department ->
                    employee.department = department
                }
            }
        }
        val list = query.execute(config)
        assertEquals(1, list.size)
        assertEquals("RESEARCH", list[0].department.departmentName)
    }

    @Test
    fun leftJoin_oneToMany() {
        val query = entityql {
            from(::_Department) { d ->
                leftJoin(::_Employee) { e ->
                    d.departmentId eq e.departmentId
                }
            }
        }
        val list = query.execute(config)
        assertEquals(4, list.size)
    }

    @Test
    fun leftJoin_oneToMany_association() {
        val query = entityql {
            from(::_Department) { d ->
                val e = leftJoin(::_Employee) { e ->
                    d.departmentId eq e.departmentId
                }
                associate(d, e) { department, employee ->
                    department.employeeList.add(employee)
                }
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
        val query = entityql {
            from(::_Employee) { e ->
                val m = leftJoin(::_Employee) { m ->
                    e.managerId eq m.employeeId
                }
                associate(e, m) { employee, manager ->
                    employee.manager = manager
                }
            }
        }
        val list = query.execute(config)
        assertEquals(14, list.size)
        assertTrue(list.filter { it.employeeName == "KING" }.all { it.manager == null })
        assertTrue(list.filterNot { it.employeeName == "KING" }.all { it.manager != null })
    }

    @Test
    fun domain() {
        val query = entityql {
            from(::_Employee) { p ->
                where {
                    p.salary eq Salary("3000")
                }
            }
        }
        val list = query.execute(config)
        assertEquals(2, list.size)
    }

    @Test
    fun optional_domain() {
        val query = entityql {
            from(::_Person) { p ->
                where {
                    p.salary eq Optional.of(Salary("3000"))
                }
            }
        }
        val list = query.execute(config)
        assertEquals(2, list.size)
    }

    @Test
    fun optional_domain_empty() {
        val query = entityql {
            from(::_Person) { p ->
                where {
                    p.salary eq Optional.empty()
                }
            }
        }
        val list = query.execute(config)
        assertEquals(0, list.size)
    }

    @Test
    fun optional_basic() {
        val query = entityql {
            from(::_Person) { p ->
                where {
                    p.managerId eq Optional.of(9)
                }
            }
        }
        val list = query.execute(config)
        assertEquals(3, list.size)
    }

    @Test
    fun optional_basic_empty() {
        val query = entityql {
            from(::_Person) { p ->
                where {
                    p.managerId eq Optional.empty()
                }
            }
        }
        val list = query.execute(config)
        assertEquals(1, list.size)
    }

    @Test
    fun optionalInt() {
        val query = entityql {
            from(::_Person) { p ->
                where {
                    p.employeeNo eq OptionalInt.of(7900)
                }
            }
        }
        val list = query.execute(config)
        assertEquals(1, list.size)
    }

    @Test
    fun offset_limit() {
        val query = entityql {
            from(::_Employee) { e ->
                orderBy {
                    e.employeeId.asc()
                }
                offset(5)
                limit(2)
            }
        }
        val list = query.execute(config)
        assertEquals(2, list.size)
        assertEquals(6, list[0].employeeId)
        assertEquals(7, list[1].employeeId)
    }

    @Test
    fun forUpdate() {
        val query = entityql {
            from(::_Employee) { e ->
                where {
                    e.employeeId eq 1
                }
                forUpdate { }
            }
        }
        val list = query.execute(config)
        assertEquals(1, list.size)
    }

    @Test
    fun forUpdate_nowait() {
        val query = entityql {
            from(::_Employee) { e ->
                where {
                    e.employeeId eq 1
                }
                forUpdate { nowait(true) }
            }
        }
        val list = query.execute(config)
        assertEquals(1, list.size)
    }

    @Test
    fun distinct() {
        val query = entityql {
            from(::_Employee) { e ->
                distinct()
                where {
                    e.employeeId eq 1
                }
            }
        }
        val list = query.execute(config)
        assertEquals(1, list.size)
    }

    @Test
    fun logType() {
        val query = entityql {
            from(::_Employee) { e ->
                where {
                    e.employeeId eq 1
                }
            }
        }
        query.execute(config, logType = SqlLogType.RAW)
    }
}

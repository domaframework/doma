package example.doc

import example.Env
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.seasar.doma.criteria.entityql
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.SqlLogType

@ExtendWith(Env::class)
class EntityqlDslDoc(private val config: Config) {

    private val logType = SqlLogType.RAW

    @Test
    fun where() {
        val query = entityql {
            from(::Emp_) { e ->
                where {
                    e.managerId ge 3
                    e.managerId le 6
                }
            }
        }
        val list = query.execute(config, logType = logType)
        Assertions.assertEquals(6, list.size)
    }

    @Test
    fun where_or() {
        val query = entityql {
            from(::Emp_) { e ->
                where {
                    e.managerId eq 1
                    or {
                        e.managerId eq 6
                    }
                }
            }
        }
        val list = query.execute(config, logType = logType)
        Assertions.assertEquals(4, list.size)
    }

    @Test
    fun where_in() {
        val query = entityql {
            from(::Emp_) { e ->
                where {
                    e.managerId `in` (listOf(1, 6))
                }
            }
        }
        val list = query.execute(config, logType = logType)
        Assertions.assertEquals(4, list.size)
    }

    @Test
    fun where_in_subQuery() {
        val query = entityql {
            from(::Emp_) { e ->
                where {
                    e.managerId `in` {
                        from(::Emp_) { e2 ->
                            select(e2.employeeId)
                        }
                    }
                }
            }
        }
        val list = query.execute(config, logType = logType)
        Assertions.assertEquals(9, list.size)
    }

    @Test
    fun leftJoin() {
        val query = entityql {
            from(::Emp_) { e ->
                val d = leftJoin(::Dept_) { d ->
                    e.departmentId eq d.departmentId
                }
                where {
                    d.departmentName eq "RESEARCH"
                }
            }
        }
        val list = query.execute(config, logType = logType)
        Assertions.assertEquals(3, list.size)
    }

    @Test
    fun leftJoin_association() {
        val query = entityql {
            from(::Emp_) { e ->
                val d = leftJoin(::Dept_) { d ->
                    e.departmentId eq d.departmentId
                }
                where {
                    e.employeeName eq "SMITH"
                }
                associate(e, d) { employee, department ->
                    employee.department = department
                    department.employeeList.add(employee)
                }
            }
        }
        val list = query.execute(config, logType = logType)
        Assertions.assertEquals(1, list.size)
        Assertions.assertEquals("RESEARCH", list[0].department.departmentName)
    }

    @Test
    fun selfJoin_association() {
        val query = entityql {
            from(::Emp_) { e ->
                val m = leftJoin(::Emp_) { m ->
                    e.managerId eq m.employeeId
                }
                associate(e, m) { employee, manager ->
                    employee.manager = manager
                }
            }
        }
        val list = query.execute(config, logType = logType)
        Assertions.assertEquals(10, list.size)
        Assertions.assertTrue(list.filter { it.employeeName == "KING" }.all { it.manager == null })
        Assertions.assertTrue(list.filterNot { it.employeeName == "KING" }.all { it.manager != null })
    }

    @Test
    fun limit_offset() {
        val query = entityql {
            from(::Emp_) { e ->
                orderBy {
                    e.employeeId.asc()
                }
                limit(5)
                offset(3)
            }
        }
        val list = query.execute(config, logType = logType)
        Assertions.assertEquals(5, list.size)
    }
}

package example.doc

import example.Env
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.seasar.doma.criteria.sql
import org.seasar.doma.internal.util.AssertionUtil
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.SqlLogType

@ExtendWith(Env::class)
class SqlDslDoc(private val config: Config) {

    private val logType = SqlLogType.RAW

    @Test
    fun select_single_value() {
        val query = sql {
            from(::Emp_) { e ->
                where {
                    e.employeeId eq 1
                }
                select(e.employeeName)
            }
        }
        val list = query.execute(config, logType = logType)
        AssertionUtil.assertEquals(1, list.size)
        AssertionUtil.assertEquals("SMITH", list[0])
    }

    @Test
    fun select_with_mapper() {
        data class MyDto(val name: String?, val id: Int?)

        val query = sql {
            from(::Emp_) { e ->
                where {
                    e.employeeId eq 1
                }
                select(e.employeeName, e.departmentId) {
                    MyDto(it[e.employeeName], it[e.departmentId])
                }
            }
        }
        val list = query.execute(config, logType = logType)
        AssertionUtil.assertEquals(1, list.size)
        AssertionUtil.assertEquals(MyDto("SMITH", 2), list[0])
    }

    @Test
    fun aggregate_function() {
        val query = sql {
            from(::Emp_) { e ->
                val d = leftJoin(::Dept_) { d ->
                    e.departmentId eq d.departmentId
                }
                groupBy(d.departmentName)
                having {
                    count(`*`) gt 2
                }
                orderBy {
                    count(`*`).desc()
                }
                select(d.departmentName, count(`*`), min(e.employeeName))
            }
        }
        val list = query.execute(config, logType = logType)
        AssertionUtil.assertEquals(2, list.size)
        AssertionUtil.assertEquals("SALES", list[0].first)
        AssertionUtil.assertEquals("RESEARCH", list[1].first)
    }

    @Test
    fun insert() {
        val query = sql {
            insert.into(::Dept_) { d ->
                values {
                    it[d.departmentId] = 99
                    it[d.departmentName] = "MARKETING"
                    it[d.version] = 1
                }
            }
        }
        val count = query.execute(config, logType = logType)
        AssertionUtil.assertEquals(1, count)
    }

    @Test
    fun delete() {
        val query = sql {
            delete.from(::Emp_) { e ->
                where {
                    e.departmentId eq 1
                }
            }
        }
        val count = query.execute(config, logType = logType)
        AssertionUtil.assertEquals(2, count)
    }

    @Test
    fun update() {
        val query = sql {
            update(::Emp_) { e ->
                set {
                    it[e.managerId] = 2
                }
                where {
                    e.departmentId eq 1
                }
            }
        }
        val count = query.execute(config, logType = logType)
        AssertionUtil.assertEquals(2, count)
    }
}

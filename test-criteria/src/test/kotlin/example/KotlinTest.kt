package example

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.criteria.AggregateFunctions.count
import org.seasar.doma.jdbc.criteria.Entityql
import org.seasar.doma.jdbc.criteria.NativeSql
import org.seasar.doma.jdbc.criteria.Tuple2
import org.seasar.doma.jdbc.criteria.Tuple3
import org.seasar.doma.jdbc.criteria.statement.Collectable

@ExtendWith(Env::class)
class KotlinTest(private val config: Config) {

    @Test
    fun tuple2() {
        val (first, second) = Tuple2(1, "a")
        assertEquals(1, first)
        assertEquals("a", second)
    }

    @Test
    fun tuple3() {
        val (first, second, third) = Tuple3(1, "a", true)
        assertEquals(1, first)
        assertEquals("a", second)
        assertEquals(true, third)
    }

    @Test
    fun where() {
        val e = Employee_()
        val stmt = Entityql.from(e)
                .where { c ->
                    c.eq(e.departmentId, 2)
                    c.isNotNull(e.managerId)
                    c.or {
                        c.gt(e.salary, Salary("1000"))
                        c.lt(e.salary, Salary("2000"))
                    }
                }
        val list = stmt.execute(config)
        assertEquals(10, list.size)
    }

    @Test
    fun aggregate() {
        val e = Employee_()
        val stmt: Collectable<Long> = NativeSql.from(e).select<Long>(count()).map { row -> row.get(count()) }
        val list = stmt.execute(config)
        assertEquals(1, list.size)
        assertEquals(14, list[0])
        println(stmt.asSql(config).rawSql)
    }
}

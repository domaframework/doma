package example

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.criteria.Entityql
import org.seasar.doma.jdbc.criteria.NativeSql
import org.seasar.doma.jdbc.criteria.expression.Expressions.count
import org.seasar.doma.jdbc.criteria.tuple.Tuple2
import org.seasar.doma.jdbc.criteria.tuple.Tuple3

@ExtendWith(Env::class)
class KotlinTest(private val config: Config) {

    private val entityql = Entityql(config)
    private val nativeSql = NativeSql(config)

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
        val list = entityql.from(e)
                .where { c ->
                    c.eq(e.departmentId, 2)
                    c.isNotNull(e.managerId)
                    c.or {
                        c.gt(e.salary, Salary("1000"))
                        c.lt(e.salary, Salary("2000"))
                    }
                }.fetch()
        assertEquals(10, list.size)
    }

    @Test
    fun aggregate() {
        val e = Employee_()
        val list = nativeSql.from(e).select(count()).fetch()
        assertEquals(1, list.size)
        assertEquals(14, list[0])
    }
}

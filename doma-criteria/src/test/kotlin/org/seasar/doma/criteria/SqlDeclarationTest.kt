package org.seasar.doma.criteria

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.seasar.doma.criteria.entity._Dept
import org.seasar.doma.criteria.mock.MockConfig

internal class SqlDeclarationTest {

    private val config = MockConfig()

    @Test
    fun test() {
        val query = sql {
            select.from(::_Dept) { d ->
                where {
                    eq(d.id, 1)
                }
                map(d.id, d.name) {
                    it[d.id] to it[d.name]
                }
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID, t0_.NAME from "CATA"."DEPT" t0_ where t0_.ID = 1"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun groupBy() {
        val query = sql {
            select.from(::_Dept) { d ->
                where {
                    eq(d.id, 1)
                }
                groupBy(d.name)
                map(d.name, count(d.id)) {
                    it[d.name] to it[count(d.id)]
                }
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.NAME, count(t0_.ID) from "CATA"."DEPT" t0_ where t0_.ID = 1 group by t0_.NAME"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun having() {
        val query = sql {
            select.from(::_Dept) { d ->
                where {
                    eq(d.id, 1)
                }
                groupBy(d.name)
                having {
                    ge(count(d.id), 1)
                }
                map(d.name, count(d.id)) {
                    it[d.name] to count(d.id)
                }
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.NAME, count(t0_.ID) from "CATA"."DEPT" t0_ where t0_.ID = 1 group by t0_.NAME having count(t0_.ID) >= 1"""
        assertEquals(expected, sql.formattedSql)
    }
}

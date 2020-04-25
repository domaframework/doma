package org.seasar.doma.criteria

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.seasar.doma.criteria.entity.Emp
import org.seasar.doma.criteria.entity._Dept
import org.seasar.doma.criteria.entity._Emp
import org.seasar.doma.criteria.mock.MockConfig

internal class SqlDslTest {

    private val config = MockConfig()

    @Test
    fun select_single() {
        val query = sql {
            from(::_Emp) { e ->
                where {
                    eq(e.id, 1)
                }
                select(e.id)
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID from EMP t0_ where t0_.ID = 1"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun select_pair() {
        val query = sql {
            from(::_Emp) { e ->
                where {
                    eq(e.id, 1)
                }
                select(e.id, e.name)
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID, t0_.NAME from EMP t0_ where t0_.ID = 1"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun select_triple() {
        val query = sql {
            from(::_Emp) { e ->
                where {
                    eq(e.id, 1)
                }
                select(e.id, e.name, e.salary)
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID, t0_.NAME, t0_.SALARY from EMP t0_ where t0_.ID = 1"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun select_with_mapper() {
        val query = sql {
            from(::_Emp) { e ->
                where {
                    eq(e.id, 1)
                }
                select(e.id, e.name, e.salary, e.version) {
                    Emp().apply {
                        id = it[e.id]
                        name = it[e.name]
                        salary = it[e.salary]
                        version = it[e.version]
                    }
                }
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ where t0_.ID = 1"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun groupBy() {
        val query = sql {
            from(::_Dept) { d ->
                where {
                    eq(d.id, 1)
                }
                groupBy(d.name)
                select(d.name, count(d.id)) {
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
            from(::_Dept) { d ->
                where {
                    eq(d.id, 1)
                }
                groupBy(d.name)
                having {
                    ge(count(d.id), 1)
                }
                select(d.name, count(d.id)) {
                    it[d.name] to count(d.id)
                }
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.NAME, count(t0_.ID) from "CATA"."DEPT" t0_ where t0_.ID = 1 group by t0_.NAME having count(t0_.ID) >= 1"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun delete() {
        val query = sql {
            delete.from(::_Dept) { d ->
                where {
                    eq(d.name, "hoge")
                }
            }
        }
        val sql = query.asSql(config)
        val expected = """delete from "CATA"."DEPT" t0_ where t0_.NAME = 'hoge'"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun insert() {
        val query = sql {
            insert.into(::_Dept) { d ->
                values {
                    value(d.id, 1)
                    value(d.name, "hoge")
                }
            }
        }
        val sql = query.asSql(config)
        val expected = """insert into "CATA"."DEPT" (ID, NAME) values (1, 'hoge')"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun update() {
        val query = sql {
            update(::_Dept) { d ->
                set {
                    value(d.name, "hoge")
                }
                where {
                    eq(d.id, 1)
                }
            }
        }
        val sql = query.asSql(config)
        val expected = """update "CATA"."DEPT" t0_ set t0_.NAME = 'hoge' where t0_.ID = 1"""
        assertEquals(expected, sql.formattedSql)
    }
}

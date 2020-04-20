package org.seasar.doma.criteria

import java.math.BigDecimal
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.seasar.doma.criteria.entity._Dept
import org.seasar.doma.criteria.entity._Emp
import org.seasar.doma.criteria.mock.ColumnMetaData
import org.seasar.doma.criteria.mock.MockConfig
import org.seasar.doma.criteria.mock.MockConnection
import org.seasar.doma.criteria.mock.MockPreparedStatement
import org.seasar.doma.criteria.mock.MockResultSet
import org.seasar.doma.criteria.mock.MockResultSetMetaData
import org.seasar.doma.criteria.mock.RowData

class SelectStatementTest {

    private val config = MockConfig()

    @Test
    fun innerJoin() {
        val query = select(::_Emp) { e ->
            innerJoin(::_Dept) { d -> eq(e.id, d.id) }
        }
        val (_, sql) = query.buildContextAndSql(config)
        val expected = """select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ inner join "CATA"."DEPT" t1_ on (t0_.ID = t1_.ID)"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun association() {
        val query = select(::_Emp) { e ->
            val d = innerJoin(::_Dept) { d -> eq(e.id, d.id) }
            associate(e, d) { emp, dept -> }
        }
        val (_, sql) = query.buildContextAndSql(config)
        val expected = """select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION, t1_.ID, t1_.NAME from EMP t0_ inner join "CATA"."DEPT" t1_ on (t0_.ID = t1_.ID)"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun where() {
        val query = select(::_Emp) { e ->
            where {
                eq(e.id, 1)
            }
        }
        val (_, sql) = query.buildContextAndSql(config)
        val expected = "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ where t0_.ID = 1"
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun where_and() {
        val query = select(::_Emp) { e ->
            where {
                eq(e.id, (1))
                and {
                    eq(e.name, "hoge")
                }
            }
        }
        val (_, sql) = query.buildContextAndSql(config)
        val expected = "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ where t0_.ID = 1 and (t0_.NAME = 'hoge')"
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun `in`() {
        val query = select(::_Emp) { e ->
            where {
                `in`(e.id, listOf(1, 2, 3))
            }
        }
        val (_, sql) = query.buildContextAndSql(config)
        val expected = "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ where t0_.ID in (1, 2, 3)"
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun between() {
        val query = select(::_Emp) { e ->
            where {
                between(e.id, 1, 5)
            }
        }
        val (_, sql) = query.buildContextAndSql(config)
        val expected = "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ where t0_.ID between 1 and 5"
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun exists() {
        val query = select(::_Emp) { e ->
            where {
                exists(::_Dept) { d ->
                    where {
                        eq(e.name, d.name)
                    }
                }
            }
        }
        val (_, sql) = query.buildContextAndSql(config)
        val expected = """select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ where exists (select * from "CATA"."DEPT" t1_ where t0_.NAME = t1_.NAME)"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun notExists() {
        val query = select(::_Emp) { e ->
            where {
                notExists(::_Dept) { d ->
                    where {
                        eq(e.name, d.name)
                    }
                }
            }
        }
        val (_, sql) = query.buildContextAndSql(config)
        val expected = """select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ where not exists (select * from "CATA"."DEPT" t1_ where t0_.NAME = t1_.NAME)"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun orderBy() {
        val query = select(::_Emp) { e ->
            orderBy {
                desc(e.id)
            }
        }
        val (_, sql) = query.buildContextAndSql(config)
        val expected = "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ order by t0_.ID desc"
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun innerJoin_where_orderBy() {
        val query = select(::_Emp) { e ->
            val d = innerJoin(::_Dept) { d -> eq(e.id, d.id) }
            where {
                eq(e.id, 1)
                eq(d.id, 1)
            }
            orderBy {
                desc(d.name)
            }
        }
        val (_, sql) = query.buildContextAndSql(config)
        val expected = """select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ inner join "CATA"."DEPT" t1_ on (t0_.ID = t1_.ID) where t0_.ID = 1 and t1_.ID = 1 order by t1_.NAME desc"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun plus() {
        val query1 = select(::_Emp) { e ->
            where {
                eq(e.id, 1)
            }
        }
        val query2 = select(::_Emp) { e ->
            where {
                eq(e.name, "hoge")
            }
        }
        val query3 = query1 + query2
        val (_, sql) = query3.buildContextAndSql(config)
        val expected = """select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ where t0_.ID = 1 and t0_.NAME = 'hoge'"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun execute() {
        val metaData = MockResultSetMetaData()
        metaData.columns.add(ColumnMetaData("id"))
        metaData.columns.add(ColumnMetaData("name"))
        metaData.columns.add(ColumnMetaData("salary"))
        metaData.columns.add(ColumnMetaData("version"))
        val resultSet = MockResultSet(metaData)
        resultSet.rows.add(RowData(1, "hoge", BigDecimal(10000), 100))
        resultSet.rows.add(RowData(2, "foo", BigDecimal(20000), 200))
        resultSet.rows.add(RowData(3, "bar", BigDecimal(30000), 300))
        config.dataSource.connection = MockConnection(MockPreparedStatement(resultSet))

        val query = select(::_Emp) {}
        val list = query.execute(config)
        assertEquals(3, list.size)

        var emp = list[0]
        assertEquals(1, emp.id)
        assertEquals("hoge", emp.name)
        assertEquals(0, BigDecimal(10000).compareTo(emp.salary))
        assertEquals(100, emp.version)

        emp = list[1]
        assertEquals(2, emp.id)
        assertEquals("foo", emp.name)
        assertEquals(0, BigDecimal(20000).compareTo(emp.salary))
        assertEquals(200, emp.version)

        emp = list[2]
        assertEquals(3, emp.id)
        assertEquals("bar", emp.name)
        assertEquals(0, BigDecimal(30000).compareTo(emp.salary))
        assertEquals(300, emp.version)
    }
}

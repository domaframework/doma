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

class EntityqlDslTest {

    private val config = MockConfig()

    @Test
    fun eq() {
        val query = entityql {
            from(::_Dept) { d ->
                where {
                    d.id eq 1
                }
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID, t0_.NAME from "CATA"."DEPT" t0_ where t0_.ID = 1"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun eq_propType() {
        val query = entityql {
            entityql {
                from(::_Dept) { d ->
                    val e = leftJoin(::_Emp) { e ->
                        d.id eq e.id
                    }
                    where {
                        d.id eq e.id
                    }
                }
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID, t0_.NAME from "CATA"."DEPT" t0_ left outer join EMP t1_ on (t0_.ID = t1_.ID) where t0_.ID = t1_.ID"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun ne() {
        val query = entityql {
            from(::_Dept) { d ->
                where {
                    d.id ne 1
                }
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID, t0_.NAME from "CATA"."DEPT" t0_ where t0_.ID <> 1"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun ne_propType() {
        val query = entityql {
            from(::_Dept) { d ->
                val e = leftJoin(::_Emp) { e ->
                    d.id eq e.id
                }
                where {
                    d.id ne e.id
                }
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID, t0_.NAME from "CATA"."DEPT" t0_ left outer join EMP t1_ on (t0_.ID = t1_.ID) where t0_.ID <> t1_.ID"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun ge() {
        val query = entityql {
            from(::_Dept) { d ->
                where {
                    d.id ge 1
                }
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID, t0_.NAME from "CATA"."DEPT" t0_ where t0_.ID >= 1"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun ge_propType() {
        val query = entityql {
            from(::_Dept) { d ->
                val e = leftJoin(::_Emp) { e ->
                    d.id eq e.id
                }
                where {
                    d.id ge e.id
                }
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID, t0_.NAME from "CATA"."DEPT" t0_ left outer join EMP t1_ on (t0_.ID = t1_.ID) where t0_.ID >= t1_.ID"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun gt() {
        val query = entityql {
            from(::_Dept) { d ->
                where {
                    d.id gt 1
                }
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID, t0_.NAME from "CATA"."DEPT" t0_ where t0_.ID > 1"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun gt_propType() {
        val query = entityql {
            from(::_Dept) { d ->
                val e = leftJoin(::_Emp) { e ->
                    d.id eq e.id
                }
                where {
                    d.id gt e.id
                }
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID, t0_.NAME from "CATA"."DEPT" t0_ left outer join EMP t1_ on (t0_.ID = t1_.ID) where t0_.ID > t1_.ID"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun le() {
        val query = entityql {
            from(::_Dept) { d ->
                where {
                    d.id le 1
                }
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID, t0_.NAME from "CATA"."DEPT" t0_ where t0_.ID <= 1"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun le_propType() {
        val query = entityql {
            from(::_Dept) { d ->
                val e = leftJoin(::_Emp) { e ->
                    d.id eq e.id
                }
                where {
                    d.id le e.id
                }
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID, t0_.NAME from "CATA"."DEPT" t0_ left outer join EMP t1_ on (t0_.ID = t1_.ID) where t0_.ID <= t1_.ID"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun lt() {
        val query = entityql {
            from(::_Dept) { d ->
                where {
                    d.id lt 1
                }
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID, t0_.NAME from "CATA"."DEPT" t0_ where t0_.ID < 1"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun lt_propType() {
        val query = entityql {
            from(::_Dept) { d ->
                val e = leftJoin(::_Emp) { e ->
                    d.id eq e.id
                }
                where {
                    d.id lt e.id
                }
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID, t0_.NAME from "CATA"."DEPT" t0_ left outer join EMP t1_ on (t0_.ID = t1_.ID) where t0_.ID < t1_.ID"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun isNull() {
        val query = entityql {
            from(::_Dept) { d ->
                where {
                    d.name.isNull()
                }
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID, t0_.NAME from "CATA"."DEPT" t0_ where t0_.NAME is null"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun isNotNull() {
        val query = entityql {
            from(::_Dept) { d ->
                where {
                    d.name.isNotNull()
                }
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID, t0_.NAME from "CATA"."DEPT" t0_ where t0_.NAME is not null"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun like() {
        val query = entityql {
            from(::_Dept) { d ->
                where {
                    d.name like "%test"
                }
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID, t0_.NAME from "CATA"."DEPT" t0_ where t0_.NAME like '%test'"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun notLike() {
        val query = entityql {
            from(::_Dept) { d ->
                where {
                    d.name notLike "%test"
                }
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID, t0_.NAME from "CATA"."DEPT" t0_ where t0_.NAME not like '%test'"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun innerJoin() {
        val query = entityql {
            from(::_Emp) { e ->
                innerJoin(::_Dept) { d -> e.id eq d.id }
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ inner join "CATA"."DEPT" t1_ on (t0_.ID = t1_.ID)"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun association() {
        val query = entityql {
            from(::_Emp) { e ->
                val d = innerJoin(::_Dept) { d -> e.id eq d.id }
                associate(e, d) { _, _ -> }
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION, t1_.ID, t1_.NAME from EMP t0_ inner join "CATA"."DEPT" t1_ on (t0_.ID = t1_.ID)"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun where() {
        val query = entityql {
            from(::_Emp) { e ->
                where {
                    e.id eq 1
                }
            }
        }
        val sql = query.asSql(config)
        val expected = "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ where t0_.ID = 1"
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun where_and() {
        val query = entityql {
            from(::_Emp) { e ->
                where {
                    e.id eq 1
                    and {
                        e.name eq "hoge"
                    }
                }
            }
        }
        val sql = query.asSql(config)
        val expected = "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ where t0_.ID = 1 and (t0_.NAME = 'hoge')"
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun `in`() {
        val query = entityql {
            from(::_Emp) { e ->
                where {
                    e.id `in` listOf(1, 2, 3)
                }
            }
        }
        val sql = query.asSql(config)
        val expected = "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ where t0_.ID in (1, 2, 3)"
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun in_pair() {
        val query = entityql {
            from(::_Emp) { e ->
                where {
                    (e.id to e.name) `in` listOf(1 to "a", 2 to "b")
                }
            }
        }
        val sql = query.asSql(config)
        val expected = "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ where (t0_.ID, t0_.NAME) in ((1, 'a'), (2, 'b'))"
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun in_single_subQuery() {
        val query = entityql {
            from(::_Emp) { e ->
                where {
                    e.id `in` {
                        from(::_Dept) { d ->
                            select(d.id)
                        }
                    }
                }
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ where t0_.ID in (select t1_.ID from "CATA"."DEPT" t1_)"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun in_pair_subQuery() {
        val query = entityql {
            from(::_Emp) { e ->
                where {
                    (e.id to e.name) `in` {
                        from(::_Dept) { d ->
                            select(d.id, d.name)
                        }
                    }
                }
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ where (t0_.ID, t0_.NAME) in (select t1_.ID, t1_.NAME from "CATA"."DEPT" t1_)"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun notIn() {
        val query = entityql {
            from(::_Emp) { e ->
                where {
                    e.id notIn listOf(1, 2, 3)
                }
            }
        }
        val sql = query.asSql(config)
        val expected = "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ where t0_.ID not in (1, 2, 3)"
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun notIn_pair() {
        val query = entityql {
            from(::_Emp) { e ->
                where {
                    (e.id to e.name) notIn listOf(1 to "a", 2 to "b")
                }
            }
        }
        val sql = query.asSql(config)
        val expected = "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ where (t0_.ID, t0_.NAME) not in ((1, 'a'), (2, 'b'))"
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun notIn_single_subQuery() {
        val query = entityql {
            from(::_Emp) { e ->
                where {
                    e.id notIn {
                        from(::_Dept) { d ->
                            select(d.id)
                        }
                    }
                }
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ where t0_.ID not in (select t1_.ID from "CATA"."DEPT" t1_)"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun notIn_pair_subQuery() {
        val query = entityql {
            from(::_Emp) { e ->
                where {
                    (e.id to e.name) notIn {
                        from(::_Dept) { d ->
                            select(d.id, d.name)
                        }
                    }
                }
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ where (t0_.ID, t0_.NAME) not in (select t1_.ID, t1_.NAME from "CATA"."DEPT" t1_)"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun between() {
        val query = entityql {
            from(::_Emp) { e ->
                where {
                    e.id between 1..5
                }
            }
        }
        val sql = query.asSql(config)
        val expected = "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ where t0_.ID between 1 and 5"
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun exists() {
        val query = entityql {
            from(::_Emp) { e ->
                where {
                    exists {
                        from(::_Dept) { d ->
                            where {
                                e.name eq d.name
                            }
                        }
                    }
                }
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ where exists (select * from "CATA"."DEPT" t1_ where t0_.NAME = t1_.NAME)"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun notExists() {
        val query = entityql {
            from(::_Emp) { e ->
                where {
                    notExists {
                        from(::_Dept) { d ->
                            where {
                                e.name eq d.name
                            }
                        }
                    }
                }
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ where not exists (select * from "CATA"."DEPT" t1_ where t0_.NAME = t1_.NAME)"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun orderBy() {
        val query = entityql {
            from(::_Emp) { e ->
                orderBy {
                    e.id.desc()
                }
            }
        }
        val sql = query.asSql(config)
        val expected = "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ order by t0_.ID desc"
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun innerJoin_where_orderBy() {
        val query = entityql {
            from(::_Emp) { e ->
                val d = innerJoin(::_Dept) { d -> e.id eq d.id }
                where {
                    e.id eq 1
                    d.id eq 1
                }
                orderBy {
                    d.name.desc()
                }
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ inner join "CATA"."DEPT" t1_ on (t0_.ID = t1_.ID) where t0_.ID = 1 and t1_.ID = 1 order by t1_.NAME desc"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun limit() {
        val query = entityql {
            from(::_Emp) {
                limit(10)
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ limit 10"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun offset() {
        val query = entityql {
            from(::_Emp) {
                offset(10)
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ offset 10"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun forUpdate() {
        val query = entityql {
            from(::_Emp) {
                forUpdate {}
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ for update"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun forUpdate_nowait() {
        val query = entityql {
            from(::_Emp) {
                forUpdate { nowait() }
            }
        }
        val sql = query.asSql(config)
        val expected = """select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ for update nowait"""
        assertEquals(expected, sql.formattedSql)
    }

    @Test
    fun distinct() {
        val query = entityql {
            from(::_Emp) {
                distinct()
            }
        }
        val sql = query.asSql(config)
        val expected = """select distinct t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_"""
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

        val query = entityql { from(::_Emp) {} }
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

package org.seasar.doma.jdbc.criteria

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.seasar.doma.DomaException
import org.seasar.doma.jdbc.CommentContext
import org.seasar.doma.jdbc.Commenter
import org.seasar.doma.jdbc.criteria.entity.Dept_
import org.seasar.doma.jdbc.criteria.entity.Emp_
import org.seasar.doma.jdbc.criteria.entity.NoIdEmp_
import org.seasar.doma.jdbc.criteria.expression.UserDefinedExpression
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel
import org.seasar.doma.jdbc.criteria.mock.MockConfig
import org.seasar.doma.jdbc.criteria.option.DistinctOption
import org.seasar.doma.jdbc.criteria.option.ForUpdateOption
import org.seasar.doma.jdbc.criteria.option.LikeOption
import org.seasar.doma.jdbc.criteria.tuple.Tuple2
import org.seasar.doma.jdbc.dialect.Db2Dialect
import org.seasar.doma.jdbc.dialect.Dialect
import org.seasar.doma.jdbc.dialect.Mssql2008Dialect
import org.seasar.doma.jdbc.dialect.MssqlDialect
import org.seasar.doma.jdbc.dialect.OracleDialect
import org.seasar.doma.jdbc.dialect.PostgresDialect
import org.seasar.doma.kotlin.jdbc.criteria.expression.KExpressions.avg
import org.seasar.doma.kotlin.jdbc.criteria.expression.KExpressions.case
import org.seasar.doma.kotlin.jdbc.criteria.expression.KExpressions.concat
import org.seasar.doma.kotlin.jdbc.criteria.expression.KExpressions.count
import org.seasar.doma.kotlin.jdbc.criteria.expression.KExpressions.countDistinct
import org.seasar.doma.kotlin.jdbc.criteria.expression.KExpressions.literal
import org.seasar.doma.kotlin.jdbc.criteria.expression.KExpressions.lower
import org.seasar.doma.kotlin.jdbc.criteria.expression.KExpressions.ltrim
import org.seasar.doma.kotlin.jdbc.criteria.expression.KExpressions.max
import org.seasar.doma.kotlin.jdbc.criteria.expression.KExpressions.min
import org.seasar.doma.kotlin.jdbc.criteria.expression.KExpressions.rtrim
import org.seasar.doma.kotlin.jdbc.criteria.expression.KExpressions.select
import org.seasar.doma.kotlin.jdbc.criteria.expression.KExpressions.sum
import org.seasar.doma.kotlin.jdbc.criteria.expression.KExpressions.trim
import org.seasar.doma.kotlin.jdbc.criteria.expression.KExpressions.upper
import org.seasar.doma.kotlin.jdbc.criteria.expression.KExpressions.userDefined
import org.seasar.doma.kotlin.jdbc.criteria.statement.KSetOperand
import org.seasar.doma.message.Message
import java.math.BigDecimal

internal class KNativeSqlSelectTest {

    private val nativeSql = org.seasar.doma.kotlin.jdbc.criteria.KNativeSql(MockConfig())

    @Test
    fun from() {
        val e = Emp_()
        val stmt = nativeSql.from(e)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_",
            sql.formattedSql,
        )
    }

    @Test
    fun from_with_subQuery() {
        val e = Emp_()
        val subQueryStmt = nativeSql.from(e)
        val stmt = nativeSql.from(e, subQueryStmt)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from (select t0_.ID AS ID, t0_.NAME AS NAME, t0_.SALARY AS SALARY, t0_.VERSION AS VERSION from EMP t0_) t0_",
            sql.formattedSql,
        )
    }

    @Test
    fun where_eq() {
        val e = Emp_()
        val stmt = nativeSql
            .from(e)
            .where {
                eq(e.id, e.id)
                eq(e.id, 1)
                eq(e.id, null as Int?)
            }
            .select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ where t0_.ID = t0_.ID and t0_.ID = 1",
            sql.formattedSql,
        )
    }

    @Test
    fun where_ne() {
        val e = Emp_()
        val stmt = nativeSql
            .from(e)
            .where {
                ne(e.id, e.id)
                ne(e.id, 1)
                ne(e.id, null as Int?)
            }
            .select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ where t0_.ID <> t0_.ID and t0_.ID <> 1",
            sql.formattedSql,
        )
    }

    @Test
    fun where_ge() {
        val e = Emp_()
        val stmt = nativeSql
            .from(e)
            .where {
                ge(e.id, e.id)
                ge(e.id, 1)
                ge(e.id, null as Int?)
            }
            .select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ where t0_.ID >= t0_.ID and t0_.ID >= 1",
            sql.formattedSql,
        )
    }

    @Test
    fun where_gt() {
        val e = Emp_()
        val stmt = nativeSql
            .from(e)
            .where {
                gt(e.id, e.id)
                gt(e.id, 1)
                gt(e.id, null as Int?)
            }
            .select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ where t0_.ID > t0_.ID and t0_.ID > 1",
            sql.formattedSql,
        )
    }

    @Test
    fun where_le() {
        val e = Emp_()
        val stmt = nativeSql
            .from(e)
            .where {
                le(e.id, e.id)
                le(e.id, 1)
                le(e.id, null as Int?)
            }
            .select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ where t0_.ID <= t0_.ID and t0_.ID <= 1",
            sql.formattedSql,
        )
    }

    @Test
    fun where_lt() {
        val e = Emp_()
        val stmt = nativeSql
            .from(e)
            .where {
                lt(e.id, e.id)
                lt(e.id, 1)
                lt(e.id, null as Int?)
            }
            .select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ where t0_.ID < t0_.ID and t0_.ID < 1",
            sql.formattedSql,
        )
    }

    @Test
    fun where_isNull() {
        val e = Emp_()
        val stmt = nativeSql.from(e).where { isNull(e.id) }.select(e.id)
        val sql = stmt.asSql()
        assertEquals("select t0_.ID from EMP t0_ where t0_.ID is null", sql.formattedSql)
    }

    @Test
    fun where_isNotNull() {
        val e = Emp_()
        val stmt = nativeSql.from(e).where { isNotNull(e.id) }.select(e.id)
        val sql = stmt.asSql()
        assertEquals("select t0_.ID from EMP t0_ where t0_.ID is not null", sql.formattedSql)
    }

    @Test
    fun where_eqOrIsNull() {
        val e = Emp_()
        val stmt = nativeSql
            .from(e)
            .where {
                eqOrIsNull(e.id, 1)
                eqOrIsNull(e.id, null)
            }
            .select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ where t0_.ID = 1 and t0_.ID is null",
            sql.formattedSql,
        )
    }

    @Test
    fun where_neOrIsNotNull() {
        val e = Emp_()
        val stmt = nativeSql
            .from(e)
            .where {
                neOrIsNotNull(e.id, 1)
                neOrIsNotNull(e.id, null)
            }
            .select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ where t0_.ID <> 1 and t0_.ID is not null",
            sql.formattedSql,
        )
    }

    @Test
    fun where_like() {
        val e = Emp_()
        val stmt = nativeSql
            .from(e)
            .where {
                like(e.name, "a$")
                like(e.name, null)
            }
            .select(e.id)
        val sql = stmt.asSql()
        assertEquals("select t0_.ID from EMP t0_ where t0_.NAME like 'a$'", sql.formattedSql)
    }

    @Test
    fun where_like_escape() {
        val e = Emp_()
        val stmt = nativeSql.from(e).where { like(e.name, "a$", LikeOption.escape()) }.select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ where t0_.NAME like 'a$$' escape '$'",
            sql.formattedSql,
        )
    }

    @Test
    fun where_like_escape_escapeChar() {
        val e = Emp_()
        val stmt = nativeSql.from(e).where { like(e.name, "a¥", LikeOption.escape('¥')) }.select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ where t0_.NAME like 'a¥¥' escape '¥'",
            sql.formattedSql,
        )
    }

    @Test
    fun where_like_prefix() {
        val e = Emp_()
        val stmt = nativeSql.from(e).where { like(e.name, "a$", LikeOption.prefix()) }.select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ where t0_.NAME like 'a$$%' escape '$'",
            sql.formattedSql,
        )
    }

    @Test
    fun where_like_infix() {
        val e = Emp_()
        val stmt = nativeSql.from(e).where { like(e.name, "a$", LikeOption.infix()) }.select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ where t0_.NAME like '%a$$%' escape '$'",
            sql.formattedSql,
        )
    }

    @Test
    fun where_like_suffix() {
        val e = Emp_()
        val stmt = nativeSql.from(e).where { like(e.name, "a$", LikeOption.suffix()) }.select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ where t0_.NAME like '%a$$' escape '$'",
            sql.formattedSql,
        )
    }

    @Test
    fun where_notLike() {
        val e = Emp_()
        val stmt = nativeSql.from(e).where { notLike(e.name, "a%") }.select(e.id)
        val sql = stmt.asSql()
        assertEquals("select t0_.ID from EMP t0_ where t0_.NAME not like 'a%'", sql.formattedSql)
    }

    @Test
    fun where_between() {
        val e = Emp_()
        val stmt = nativeSql
            .from(e)
            .where {
                between(e.id, 1, 10)
                between(e.id, null, 10)
                between(e.id, 1, null)
                between(e.id, null, null)
            }
            .select(e.id)
        val sql = stmt.asSql()
        assertEquals("select t0_.ID from EMP t0_ where t0_.ID between 1 and 10", sql.formattedSql)
    }

    @Test
    fun where_in() {
        val e = Emp_()
        val stmt = nativeSql
            .from(e)
            .where {
                `in`(e.id, listOf(1, 2))
                `in`(e.id, null as List<Int>?)
            }
            .select(e.id)
        val sql = stmt.asSql()
        assertEquals("select t0_.ID from EMP t0_ where t0_.ID in (1, 2)", sql.formattedSql)
    }

    @Test
    fun where_notIn() {
        val e = Emp_()
        val stmt = nativeSql
            .from(e)
            .where {
                notIn(e.id, listOf(1, 2))
                notIn(e.id, null as List<Int>?)
            }
            .select(e.id)
        val sql = stmt.asSql()
        assertEquals("select t0_.ID from EMP t0_ where t0_.ID not in (1, 2)", sql.formattedSql)
    }

    @Test
    fun where_in_tuple2() {
        val e = Emp_()
        val stmt = nativeSql
            .from(e)
            .where {
                `in`(Tuple2(e.id, e.name), listOf(Tuple2(1, "a"), Tuple2(2, "b")))
                `in`(Tuple2(e.id, e.name), null as List<Tuple2<Int, String>>?)
            }
            .select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ where (t0_.ID, t0_.NAME) in ((1, 'a'), (2, 'b'))",
            sql.formattedSql,
        )
    }

    @Test
    fun where_notIn_tuple2() {
        val e = Emp_()
        val stmt = nativeSql
            .from(e)
            .where {
                notIn(Tuple2(e.id, e.name), listOf(Tuple2(1, "a"), Tuple2(2, "b")))
                notIn(Tuple2(e.id, e.name), null as List<Tuple2<Int, String>>?)
            }
            .select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ where (t0_.ID, t0_.NAME) not in ((1, 'a'), (2, 'b'))",
            sql.formattedSql,
        )
    }

    @Test
    fun where_in_subQuery() {
        val e = Emp_()
        val d = Dept_()
        val stmt = nativeSql.from(e).where { `in`(e.id, from(d).select(d.id)) }.select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ where t0_.ID in (select t1_.ID from CATA.DEPT t1_)",
            sql.formattedSql,
        )
    }

    @Test
    fun where_notIn_subQuery() {
        val e = Emp_()
        val d = Dept_()
        val stmt = nativeSql.from(e).where { notIn(e.id, from(d).select(d.id)) }.select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ where t0_.ID not in (select t1_.ID from CATA.DEPT t1_)",
            sql.formattedSql,
        )
    }

    @Test
    fun where_in_tuple2_subQuery() {
        val e = Emp_()
        val d = Dept_()
        val stmt = nativeSql
            .from(e)
            .where { `in`(Tuple2(e.id, e.name), from(d).select(d.id, d.name)) }
            .select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ where (t0_.ID, t0_.NAME) in (select t1_.ID, t1_.NAME from CATA.DEPT t1_)",
            sql.formattedSql,
        )
    }

    @Test
    fun where_notIn_tuple2_subQuery() {
        val e = Emp_()
        val d = Dept_()
        val stmt = nativeSql
            .from(e)
            .where { notIn(Tuple2(e.id, e.name), from(d).select(d.id, d.name)) }
            .select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ where (t0_.ID, t0_.NAME) not in (select t1_.ID, t1_.NAME from CATA.DEPT t1_)",
            sql.formattedSql,
        )
    }

    @Test
    fun where_exist() {
        val e = Emp_()
        val d = Dept_()
        val stmt = nativeSql.from(e).where { exists(from(d).select(d.id)) }.select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ where exists (select t1_.ID from CATA.DEPT t1_)",
            sql.formattedSql,
        )
    }

    @Test
    fun where_exist_without_select() {
        val e = Emp_()
        val d = Dept_()
        val stmt = nativeSql.from(e).where { exists(from(d)) }.select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ where exists (select t1_.ID, t1_.NAME from CATA.DEPT t1_)",
            sql.formattedSql,
        )
    }

    @Test
    fun where_notExist() {
        val e = Emp_()
        val d = Dept_()
        val stmt = nativeSql.from(e).where { notExists(from(d).select(d.id)) }.select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ where not exists (select t1_.ID from CATA.DEPT t1_)",
            sql.formattedSql,
        )
    }

    @Test
    fun where_and() {
        val e = Emp_()
        val stmt = nativeSql
            .from(e)
            .where {
                eq(e.id, 1)
                and {
                    eq(e.name, "a")
                    eq(e.version, 1)
                    and {
                        eq(e.salary, BigDecimal("20"))
                    }
                }
                eq(e.salary, BigDecimal("10"))
            }
            .select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ where t0_.ID = 1 and (t0_.NAME = 'a' and t0_.VERSION = 1 and (t0_.SALARY = 20)) and t0_.SALARY = 10",
            sql.formattedSql,
        )
    }

    @Test
    fun where_or() {
        val e = Emp_()
        val stmt = nativeSql
            .from(e)
            .where {
                eq(e.id, 1)
                or {
                    eq(e.name, "a")
                    eq(e.version, 1)
                    or {
                        eq(e.salary, BigDecimal("20"))
                    }
                }
                eq(e.salary, BigDecimal("10"))
            }
            .select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ where t0_.ID = 1 or (t0_.NAME = 'a' and t0_.VERSION = 1 or (t0_.SALARY = 20)) and t0_.SALARY = 10",
            sql.formattedSql,
        )
    }

    @Test
    fun where_not() {
        val e = Emp_()
        val stmt = nativeSql
            .from(e)
            .where {
                eq(e.id, 1)
                not {
                    eq(e.name, "a")
                    eq(e.version, 1)
                    not {
                        eq(e.salary, BigDecimal("20"))
                    }
                }
                eq(e.salary, BigDecimal("10"))
            }
            .select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ where t0_.ID = 1 and not (t0_.NAME = 'a' and t0_.VERSION = 1 and not (t0_.SALARY = 20)) and t0_.SALARY = 10",
            sql.formattedSql,
        )
    }

    @Test
    fun where_empty_empty() {
        val e = Emp_()
        val stmt = nativeSql.from(e).where { }.select(e.id)
        val sql = stmt.asSql()
        assertEquals("select t0_.ID from EMP t0_", sql.formattedSql)
    }

    @Test
    fun where_empty_and() {
        val e = Emp_()
        val stmt = nativeSql
            .from(e)
            .where {
                eq(e.id, 1)
                and {}
                eq(e.salary, BigDecimal("10"))
            }
            .select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ where t0_.ID = 1 and t0_.SALARY = 10",
            sql.formattedSql,
        )
    }

    @Test
    fun where_empty_or() {
        val e = Emp_()
        val stmt = nativeSql
            .from(e)
            .where {
                eq(e.id, 1)
                or {}
                eq(e.salary, BigDecimal("10"))
            }
            .select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ where t0_.ID = 1 and t0_.SALARY = 10",
            sql.formattedSql,
        )
    }

    @Test
    fun where_empty_not() {
        val e = Emp_()
        val stmt = nativeSql
            .from(e)
            .where {
                eq(e.id, 1)
                not {}
                eq(e.salary, BigDecimal("10"))
            }
            .select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ where t0_.ID = 1 and t0_.SALARY = 10",
            sql.formattedSql,
        )
    }

    @Test
    fun innerJoin() {
        val e = Emp_()
        val d = Dept_()
        val stmt: KSetOperand<Int> = nativeSql.from(e).innerJoin(d) { eq(e.id, d.id) }.select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ inner join CATA.DEPT t1_ on (t0_.ID = t1_.ID)",
            sql.formattedSql,
        )
    }

    @Test
    fun innerJoin_empty() {
        val e = Emp_()
        val d = Dept_()
        val stmt = nativeSql.from(e).innerJoin(d) { }.select(e.id)
        val sql = stmt.asSql()
        assertEquals("select t0_.ID from EMP t0_", sql.formattedSql)
    }

    @Test
    fun leftJoin() {
        val e = Emp_()
        val d = Dept_()
        val stmt = nativeSql.from(e).leftJoin(d) { eq(e.id, d.id) }.select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ left outer join CATA.DEPT t1_ on (t0_.ID = t1_.ID)",
            sql.formattedSql,
        )
    }

    @Test
    fun leftJoin_empty() {
        val e = Emp_()
        val d = Dept_()
        val stmt = nativeSql.from(e).leftJoin(d) { }.select(e.id)
        val sql = stmt.asSql()
        assertEquals("select t0_.ID from EMP t0_", sql.formattedSql)
    }

    @Test
    fun join_on() {
        val e = Emp_()
        val d = Dept_()
        val stmt = nativeSql
            .from(e)
            .innerJoin(d) {
                eq(e.id, d.id)
                ne(e.id, d.id)
                ge(e.id, d.id)
                gt(e.id, d.id)
                le(e.id, d.id)
                lt(e.id, d.id)
                isNull(e.name)
                isNotNull(e.name)
            }
            .select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ inner join CATA.DEPT t1_ on (t0_.ID = t1_.ID and t0_.ID <> t1_.ID and t0_.ID >= t1_.ID and t0_.ID > t1_.ID and t0_.ID <= t1_.ID and t0_.ID < t1_.ID and t0_.NAME is null and t0_.NAME is not null)",
            sql.formattedSql,
        )
    }

    @Test
    fun orderBy() {
        val e = Emp_()
        val stmt = nativeSql
            .from(e)
            .orderBy {
                asc(e.id)
                desc(e.name)
                asc(e.salary)
                desc(e.version)
            }
            .select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ order by t0_.ID asc, t0_.NAME desc, t0_.SALARY asc, t0_.VERSION desc",
            sql.formattedSql,
        )
    }

    @Test
    fun orderBy_empty() {
        val e = Emp_()
        val stmt = nativeSql.from(e).orderBy { }.select(e.id)
        val sql = stmt.asSql()
        assertEquals("select t0_.ID from EMP t0_", sql.formattedSql)
    }

    @Test
    fun limit() {
        val e = Emp_()
        val stmt = nativeSql.from(e).limit(10).select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ offset 0 rows fetch first 10 rows only",
            sql.formattedSql,
        )
    }

    @Test
    fun limit_null() {
        val e = Emp_()
        val stmt = nativeSql.from(e).limit(null).select(e.id)
        val sql = stmt.asSql()
        assertEquals("select t0_.ID from EMP t0_", sql.formattedSql)
    }

    @Test
    fun offset() {
        val e = Emp_()
        val stmt = nativeSql.from(e).offset(10).select(e.id)
        val sql = stmt.asSql()
        assertEquals("select t0_.ID from EMP t0_ offset 10 rows", sql.formattedSql)
    }

    @Test
    fun offset_null() {
        val e = Emp_()
        val stmt = nativeSql.from(e).offset(null).select(e.id)
        val sql = stmt.asSql()
        assertEquals("select t0_.ID from EMP t0_", sql.formattedSql)
    }

    @Test
    fun forUpdate() {
        val e = Emp_()
        val stmt = nativeSql.from(e).forUpdate().select(e.id)
        val sql = stmt.asSql()
        assertEquals("select t0_.ID from EMP t0_ for update", sql.formattedSql)
    }

    @Test
    fun forUpdate_db2() {
        val nativeSql = org.seasar.doma.kotlin.jdbc.criteria.KNativeSql(
            object : MockConfig() {
                override fun getDialect(): Dialect {
                    return Db2Dialect()
                }
            },
        )
        val e = Emp_()
        val stmt = nativeSql.from(e).where { eq(e.id, 1) }.forUpdate().select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ where t0_.ID = 1 for update with rs",
            sql.formattedSql,
        )
    }

    @Test
    fun forUpdate_mssql() {
        val nativeSql = org.seasar.doma.kotlin.jdbc.criteria.KNativeSql(
            object : MockConfig() {
                override fun getDialect(): Dialect {
                    return MssqlDialect()
                }
            },
        )
        val e = Emp_()
        val stmt = nativeSql.from(e).where { eq(e.id, 1) }.forUpdate().select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ with (updlock, rowlock) where t0_.ID = 1",
            sql.formattedSql,
        )
    }

    @Test
    fun forUpdate_mssql_nowait() {
        val nativeSql = org.seasar.doma.kotlin.jdbc.criteria.KNativeSql(
            object : MockConfig() {
                override fun getDialect(): Dialect {
                    return MssqlDialect()
                }
            },
        )
        val e = Emp_()
        val stmt = nativeSql
            .from(e)
            .where { eq(e.id, 1) }
            .forUpdate(ForUpdateOption.noWait())
            .select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ with (updlock, rowlock, nowait) where t0_.ID = 1",
            sql.formattedSql,
        )
    }

    @Test
    fun forUpdate_oracle_nowait() {
        val nativeSql = org.seasar.doma.kotlin.jdbc.criteria.KNativeSql(
            object : MockConfig() {
                override fun getDialect(): Dialect {
                    return OracleDialect()
                }
            },
        )
        val e = Emp_()
        val stmt = nativeSql.from(e).forUpdate(ForUpdateOption.noWait()).select(e.id)
        val sql = stmt.asSql()
        assertEquals("select t0_.ID from EMP t0_ for update nowait", sql.formattedSql)
    }

    @Test
    fun forUpdate_oracle_nowait_withColumn() {
        val nativeSql = org.seasar.doma.kotlin.jdbc.criteria.KNativeSql(
            object : MockConfig() {
                override fun getDialect(): Dialect {
                    return OracleDialect()
                }
            },
        )
        val e = Emp_()
        val d = Dept_()
        val stmt = nativeSql
            .from(e)
            .innerJoin(d) { eq(e.id, d.id) }
            .forUpdate(ForUpdateOption.noWait(e.id, d.id))
            .select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ inner join CATA.DEPT t1_ on (t0_.ID = t1_.ID) for update of t0_.ID, t1_.ID nowait",
            sql.formattedSql,
        )
    }

    @Test
    fun forUpdate_oracle_wait_withColumn() {
        val nativeSql = org.seasar.doma.kotlin.jdbc.criteria.KNativeSql(
            object : MockConfig() {
                override fun getDialect(): Dialect {
                    return OracleDialect()
                }
            },
        )
        val e = Emp_()
        val d = Dept_()
        val stmt = nativeSql
            .from(e)
            .innerJoin(d) { eq(e.id, d.id) }
            .forUpdate(ForUpdateOption.wait(5, e.id, d.id))
            .select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ inner join CATA.DEPT t1_ on (t0_.ID = t1_.ID) for update of t0_.ID, t1_.ID wait 5",
            sql.formattedSql,
        )
    }

    @Test
    fun forUpdate_postgres_nowait_withTable() {
        val nativeSql = org.seasar.doma.kotlin.jdbc.criteria.KNativeSql(
            object : MockConfig() {
                override fun getDialect(): Dialect {
                    return PostgresDialect()
                }
            },
        )
        val e = Emp_()
        val d = Dept_()
        val stmt = nativeSql
            .from(e)
            .innerJoin(d) { eq(e.id, d.id) }
            .forUpdate(ForUpdateOption.noWait(e.id, d.id))
            .select(e.id)
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID from EMP t0_ inner join CATA.DEPT t1_ on (t0_.ID = t1_.ID) for update of t0_, t1_ nowait",
            sql.formattedSql,
        )
    }

    @Test
    fun forUpdate_none() {
        val e = Emp_()
        val stmt = nativeSql.from(e).forUpdate(ForUpdateOption.none()).select(e.id)
        val sql = stmt.asSql()
        assertEquals("select t0_.ID from EMP t0_", sql.formattedSql)
    }

    @Test
    fun groupBy() {
        val e = Emp_()
        val stmt = nativeSql.from(e).groupBy(e.id, e.name).select(e.id)
        val sql = stmt.asSql()
        assertEquals("select t0_.ID from EMP t0_ group by t0_.ID, t0_.NAME", sql.formattedSql)
    }

    @Test
    fun groupBy_empty() {
        val e = Emp_()
        val stmt = nativeSql.from(e).groupBy().select(e.id)
        val sql = stmt.asSql()
        assertEquals("select t0_.ID from EMP t0_", sql.formattedSql)
    }

    @Test
    fun groupBy_auto_generation() {
        val e = Emp_()
        val stmt = nativeSql.from(e).select(e.id, e.salary, count(e.name), max(e.id))
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID, t0_.SALARY, count(t0_.NAME), max(t0_.ID) from EMP t0_ group by t0_.ID, t0_.SALARY",
            sql.formattedSql,
        )
    }

    @Test
    fun having() {
        val e = Emp_()
        val stmt = nativeSql.from(e).having { eq(e.id, 1) }.select(e.id)
        val sql = stmt.asSql()
        assertEquals("select t0_.ID from EMP t0_ having t0_.ID = 1", sql.formattedSql)
    }

    @Test
    fun having_empty() {
        val e = Emp_()
        val stmt = nativeSql.from(e).having {}.select(e.id)
        val sql = stmt.asSql()
        assertEquals("select t0_.ID from EMP t0_", sql.formattedSql)
    }

    @Test
    fun test_select() {
        val e = Emp_()
        val stmt = nativeSql.from(e).select(e.id, e.name)
        val sql = stmt.asSql()
        assertEquals("select t0_.ID, t0_.NAME from EMP t0_", sql.formattedSql)
    }

    @Test
    fun selectTo() {
        val e = Emp_()
        val stmt = nativeSql.from(e).selectTo(e, e.name)
        val sql = stmt.asSql()
        assertEquals("select t0_.ID, t0_.NAME from EMP t0_", sql.formattedSql)
    }

    @Test
    fun selectTo_no_propertyMetamodels() {
        val e = Emp_()
        val stmt = nativeSql.from(e).selectTo(e)
        val sql = stmt.asSql()
        assertEquals("select t0_.ID from EMP t0_", sql.formattedSql)
    }

    @Test
    fun selectTo_illegal_entityMetamodel() {
        val e = Emp_()
        val d = Dept_()
        val ex = assertThrows(DomaException::class.java) { nativeSql.from(e).selectTo(d) }
        assertEquals(Message.DOMA6007, ex.messageResource)
        println(ex.message)
    }

    @Test
    fun selectTo_illegal_propertyMetamodel() {
        val e = Emp_()
        val d = Dept_()
        val ex = assertThrows(DomaException::class.java) { nativeSql.from(e).selectTo(e, d.name) }
        assertEquals(Message.DOMA6008, ex.messageResource)
        println(ex.message)
    }

    @Test
    fun aggregateFunctions() {
        val e = Emp_()
        val stmt = nativeSql.from(e).select(avg(e.id), count(e.id), count(), max(e.id), min(e.id), sum(e.id))
        val sql = stmt.asSql()
        assertEquals(
            "select avg(t0_.ID), count(t0_.ID), count(*), max(t0_.ID), min(t0_.ID), sum(t0_.ID) from EMP t0_",
            sql.formattedSql,
        )
    }

    @Test
    fun aggregateFunction_countDistinct() {
        val e = Emp_()
        val stmt = nativeSql.from(e).select(countDistinct(e.id))
        val sql = stmt.asSql()
        assertEquals("select count(distinct t0_.ID) from EMP t0_", sql.formattedSql)
    }

    @Test
    fun union() {
        val e = Emp_()
        val d = Dept_()
        val stmt1 = nativeSql.from(e).select(e.name)
        val stmt2 = nativeSql.from(d).select(d.name)
        val stmt3 = stmt1.union(stmt2)
        val sql1 = stmt1.asSql()
        assertEquals("select t0_.NAME from EMP t0_", sql1.formattedSql)
        val sql2 = stmt2.asSql()
        assertEquals("select t0_.NAME from CATA.DEPT t0_", sql2.formattedSql)
        val sql3 = stmt3.asSql()
        assertEquals(
            "select t0_.NAME from EMP t0_ union select t0_.NAME from CATA.DEPT t0_",
            sql3.formattedSql,
        )
    }

    @Test
    fun union_orderBy() {
        val e = Emp_()
        val d = Dept_()
        val stmt1 = nativeSql.from(e).select(e.name)
        val stmt2 = nativeSql.from(d).select(d.name)
        val stmt3 = stmt1.union(stmt2).orderBy { asc(1) }
        val sql1 = stmt1.asSql()
        assertEquals("select t0_.NAME from EMP t0_", sql1.formattedSql)
        val sql2 = stmt2.asSql()
        assertEquals("select t0_.NAME from CATA.DEPT t0_", sql2.formattedSql)
        val sql3 = stmt3.asSql()
        assertEquals(
            "(select t0_.NAME from EMP t0_) union (select t0_.NAME from CATA.DEPT t0_) order by 1 asc",
            sql3.formattedSql,
        )
    }

    @Test
    fun multi_union() {
        val e = Emp_()
        val d = Dept_()
        val n = NoIdEmp_()
        val stmt1 = nativeSql.from(e).select(e.name)
        val stmt2 = nativeSql.from(d).select(d.name)
        val stmt3 = nativeSql.from(n).select(n.name)
        val stmt4 = stmt1.union(stmt2).union(stmt3)
        val sql = stmt4.asSql()
        assertEquals(
            "select t0_.NAME from EMP t0_ union select t0_.NAME from CATA.DEPT t0_ union select t0_.NAME from NO_ID_EMP t0_",
            sql.formattedSql,
        )
    }

    @Test
    fun multi_union_orderBy() {
        val e = Emp_()
        val d = Dept_()
        val n = NoIdEmp_()
        val stmt1 = nativeSql.from(e).select(e.name)
        val stmt2 = nativeSql.from(d).select(d.name)
        val stmt3 = nativeSql.from(n).select(n.name)
        val stmt4 = stmt1.union(stmt2).union(stmt3).orderBy { desc(1) }
        val sql = stmt4.asSql()
        assertEquals(
            "(select t0_.NAME from EMP t0_ union select t0_.NAME from CATA.DEPT t0_) union (select t0_.NAME from NO_ID_EMP t0_) order by 1 desc",
            sql.formattedSql,
        )
    }

    @Test
    fun unionAll() {
        val e = Emp_()
        val d = Dept_()
        val stmt1 = nativeSql.from(e).select(e.name)
        val stmt2 = nativeSql.from(d).select(d.name)
        val stmt3 = stmt1.unionAll(stmt2)
        val sql = stmt3.asSql()
        assertEquals(
            "select t0_.NAME from EMP t0_ union all select t0_.NAME from CATA.DEPT t0_",
            sql.formattedSql,
        )
    }

    @Test
    fun options_comment() {
        val config = object : MockConfig() {
            override fun getCommenter(): Commenter {
                return object : Commenter {
                    override fun comment(sql: String, context: CommentContext): String {
                        if (context.message.isPresent) {
                            val message: String = context.message.get()
                            return String.format("// %s\n%s", message, sql)
                        }
                        return sql
                    }
                }
            }
        }
        val nativeSql = org.seasar.doma.kotlin.jdbc.criteria.KNativeSql(config)
        val e = Emp_()
        val stmt = nativeSql.from(e) { comment = "hello" }
        val sql = stmt.asSql()
        assertEquals(
            "// hello\nselect t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_",
            sql.formattedSql,
        )
    }

    @Test
    fun peek() {
        val e = Emp_()
        val d = Dept_()

        @Suppress("UNUSED_VARIABLE")
        val stmt = nativeSql
            .from(e)
            .select(e.name)
            .peek { println(it) }
            .union(nativeSql.from(d).select(d.name).peek { println(it) })
            .peek { println(it) }
    }

    @Test
    fun distinct() {
        val e = Emp_()
        val stmt = nativeSql.from(e).distinct().where { eq(e.name, "a") }
        val sql = stmt.asSql()
        assertEquals(
            "select distinct t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ where t0_.NAME = 'a'",
            sql.formattedSql,
        )
    }

    @Test
    fun distinct_normal() {
        val e = Emp_()
        val stmt = nativeSql.from(e).distinct(DistinctOption.basic()).where { eq(e.name, "a") }
        val sql = stmt.asSql()
        assertEquals(
            "select distinct t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ where t0_.NAME = 'a'",
            sql.formattedSql,
        )
    }

    @Test
    fun distinct_none() {
        val e = Emp_()
        val stmt = nativeSql.from(e).distinct(DistinctOption.none()).where { eq(e.name, "a") }
        val sql = stmt.asSql()
        assertEquals(
            "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ where t0_.NAME = 'a'",
            sql.formattedSql,
        )
    }

    @Test
    fun expression_concat() {
        val e = Emp_()
        val stmt = nativeSql.from(e).select(concat(e.name, "a"))
        val sql = stmt.asSql()
        assertEquals("select concat(t0_.NAME, 'a') from EMP t0_", sql.formattedSql)
    }

    @Test
    fun expression_concat_mssql2008() {
        val nativeSql = org.seasar.doma.kotlin.jdbc.criteria.KNativeSql(
            object : MockConfig() {
                override fun getDialect(): Dialect {
                    return Mssql2008Dialect()
                }
            },
        )
        val e = Emp_()
        val stmt = nativeSql.from(e).select(concat(e.name, "a"))
        val sql = stmt.asSql()
        assertEquals("select (t0_.NAME + 'a') from EMP t0_", sql.formattedSql)
    }

    @Test
    fun expression_lower() {
        val e = Emp_()
        val stmt = nativeSql.from(e).select(lower(e.name))
        val sql = stmt.asSql()
        assertEquals("select lower(t0_.NAME) from EMP t0_", sql.formattedSql)
    }

    @Test
    fun expression_ltrim() {
        val e = Emp_()
        val stmt = nativeSql.from(e).select(ltrim(e.name))
        val sql = stmt.asSql()
        assertEquals("select ltrim(t0_.NAME) from EMP t0_", sql.formattedSql)
    }

    @Test
    fun expression_rtrim() {
        val e = Emp_()
        val stmt = nativeSql.from(e).select(rtrim(e.name))
        val sql = stmt.asSql()
        assertEquals("select rtrim(t0_.NAME) from EMP t0_", sql.formattedSql)
    }

    @Test
    fun expression_trim() {
        val e = Emp_()
        val stmt = nativeSql.from(e).select(trim(e.name))
        val sql = stmt.asSql()
        assertEquals("select trim(t0_.NAME) from EMP t0_", sql.formattedSql)
    }

    @Test
    fun expression_upper() {
        val e = Emp_()
        val stmt = nativeSql.from(e).select(upper(e.name))
        val sql = stmt.asSql()
        assertEquals("select upper(t0_.NAME) from EMP t0_", sql.formattedSql)
    }

    @Test
    fun expression_stringLiteral() {
        val e = Emp_()
        val stmt = nativeSql.from(e).select(literal("a"))
        val sql = stmt.asSql()
        assertEquals("select 'a' from EMP t0_", sql.rawSql)
    }

    @Test
    fun expression_intLiteral() {
        val e = Emp_()
        val stmt = nativeSql.from(e).select(literal(123))
        val sql = stmt.asSql()
        assertEquals("select 123 from EMP t0_", sql.rawSql)
    }

    @Test
    fun expression_when_returns_same_type_with_comparison_type() {
        val e = Emp_()
        val stmt = nativeSql.from(e).select(case({ eq(e.name, literal("a"), literal("b")) }, literal("z")))
        val sql = stmt.asSql()
        assertEquals(
            "select case when t0_.NAME = 'a' then 'b' else 'z' end from EMP t0_",
            sql.rawSql,
        )
    }

    @Test
    fun expression_when_returns_different_type_with_comparison_type() {
        val e = Emp_()
        val stmt = nativeSql.from(e).select(case({ eq(e.name, literal("a"), literal(1)) }, literal(0)))
        val sql = stmt.asSql()
        assertEquals(
            "select case " + "when t0_.NAME = 'a' then 1 else 0 end from EMP t0_",
            sql.rawSql,
        )
    }

    @Test
    fun expression_when_operators() {
        val e = Emp_()
        val stmt = nativeSql
            .from(e)
            .select(
                case(
                    {
                        eq(e.name, literal("a"), literal("b"))
                        ne(e.name, literal("c"), literal("d"))
                        ge(e.name, literal("e"), literal("f"))
                        gt(e.name, literal("g"), literal("h"))
                        le(e.name, literal("i"), literal("j"))
                        lt(e.name, literal("k"), literal("l"))
                        isNull(e.name, literal("m"))
                        isNotNull(e.name, literal("n"))
                    },
                    literal("z"),
                ),
            )
        val sql = stmt.asSql()
        assertEquals(
            (
                "select case " +
                    "when t0_.NAME = 'a' then 'b' " +
                    "when t0_.NAME <> 'c' then 'd' " +
                    "when t0_.NAME >= 'e' then 'f' " +
                    "when t0_.NAME > 'g' then 'h' " +
                    "when t0_.NAME <= 'i' then 'j' " +
                    "when t0_.NAME < 'k' then 'l' " +
                    "when t0_.NAME is null then 'm' " +
                    "when t0_.NAME is not null then 'n' " +
                    "else 'z' end from EMP t0_"
                ),
            sql.rawSql,
        )
    }

    @Test
    fun expression_when_empty() {
        val e = Emp_()
        val stmt = nativeSql.from(e).select(case({ }, literal("c")))
        val sql = stmt.asSql()
        assertEquals("select 'c' from EMP t0_", sql.rawSql)
    }

    @Test
    fun expression_select() {
        val e = Emp_()
        val e2 = Emp_()
        val expression = select { from(e2).select(e2.id) }
        val stmt = nativeSql.from(e).select(expression)
        val sql = stmt.asSql()
        assertEquals("select (select t1_.ID from EMP t1_) from EMP t0_", sql.rawSql)
    }

    @Test
    fun expression_userDefined1() {
        val e = Emp_()
        val exp = countDistinctMultipleWithUserDefined1(e.id, e.name)
        val stmt = nativeSql.from(e).select(exp)
        val sql = stmt.asSql()
        assertEquals("select count(distinct (t0_.ID, t0_.NAME)) from EMP t0_", sql.rawSql)
    }

    @Test
    fun expression_userDefined2() {
        val e = Emp_()
        val count = count(e.id)
        val exp = countDistinctMultipleWithUserDefined2(count, e.id, e.name)
        val stmt = nativeSql.from(e).select(exp)
        val sql = stmt.asSql()
        assertEquals("select count(distinct (t0_.ID, t0_.NAME)) from EMP t0_", sql.rawSql)
    }

    private fun countDistinctMultipleWithUserDefined1(
        vararg propertyMetamodels: PropertyMetamodel<*>,
    ): UserDefinedExpression<Long> {
        return userDefined {
            appendSql("count(distinct (")
            for (propertyMetamodel in propertyMetamodels) {
                appendExpression(propertyMetamodel)
                appendSql(", ")
            }
            cutBackSql(2)
            appendSql("))")
        }
    }

    private fun countDistinctMultipleWithUserDefined2(
        propertyMetamodel: PropertyMetamodel<Long>,
        vararg propertyMetamodels: PropertyMetamodel<*>,
    ): UserDefinedExpression<Long> {
        return userDefined(propertyMetamodel) {
            appendSql("count(distinct (")
            for (propertyMetamodel in propertyMetamodels) {
                appendExpression(propertyMetamodel)
                appendSql(", ")
            }
            cutBackSql(2)
            appendSql("))")
        }
    }
}

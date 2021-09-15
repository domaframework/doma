package org.seasar.doma.jdbc.criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.avg;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.avgAsDouble;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.concat;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.count;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.countDistinct;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.literal;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.lower;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.ltrim;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.max;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.min;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.rtrim;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.select;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.sum;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.trim;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.upper;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.seasar.doma.DomaException;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.CommentContext;
import org.seasar.doma.jdbc.Commenter;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.criteria.entity.Dept_;
import org.seasar.doma.jdbc.criteria.entity.Emp;
import org.seasar.doma.jdbc.criteria.entity.Emp_;
import org.seasar.doma.jdbc.criteria.entity.NoIdEmp_;
import org.seasar.doma.jdbc.criteria.expression.SelectExpression;
import org.seasar.doma.jdbc.criteria.option.DistinctOption;
import org.seasar.doma.jdbc.criteria.option.ForUpdateOption;
import org.seasar.doma.jdbc.criteria.option.LikeOption;
import org.seasar.doma.jdbc.criteria.statement.Buildable;
import org.seasar.doma.jdbc.criteria.statement.SetOperand;
import org.seasar.doma.jdbc.criteria.statement.Statement;
import org.seasar.doma.jdbc.criteria.tuple.Tuple2;
import org.seasar.doma.jdbc.dialect.Db2Dialect;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.Mssql2008Dialect;
import org.seasar.doma.jdbc.dialect.MssqlDialect;
import org.seasar.doma.jdbc.dialect.OracleDialect;
import org.seasar.doma.jdbc.dialect.PostgresDialect;
import org.seasar.doma.message.Message;

class NativeSqlSelectTest {

  private final NativeSql nativeSql = new NativeSql(new MockConfig());

  @Test
  void from() {
    Emp_ e = new Emp_();
    Statement<List<Emp>> stmt = nativeSql.from(e);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void where_eq() {
    Emp_ e = new Emp_();
    Buildable<?> stmt =
        nativeSql
            .from(e)
            .where(
                c -> {
                  c.eq(e.id, e.id);
                  c.eq(e.id, 1);
                  c.eq(e.id, (Integer) null);
                })
            .select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where t0_.ID = t0_.ID and t0_.ID = 1", sql.getFormattedSql());
  }

  @Test
  void where_ne() {
    Emp_ e = new Emp_();
    Buildable<?> stmt =
        nativeSql
            .from(e)
            .where(
                c -> {
                  c.ne(e.id, e.id);
                  c.ne(e.id, 1);
                  c.ne(e.id, (Integer) null);
                })
            .select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where t0_.ID <> t0_.ID and t0_.ID <> 1", sql.getFormattedSql());
  }

  @Test
  void where_ge() {
    Emp_ e = new Emp_();
    Buildable<?> stmt =
        nativeSql
            .from(e)
            .where(
                c -> {
                  c.ge(e.id, e.id);
                  c.ge(e.id, 1);
                  c.ge(e.id, (Integer) null);
                })
            .select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where t0_.ID >= t0_.ID and t0_.ID >= 1", sql.getFormattedSql());
  }

  @Test
  void where_gt() {
    Emp_ e = new Emp_();
    Buildable<?> stmt =
        nativeSql
            .from(e)
            .where(
                c -> {
                  c.gt(e.id, e.id);
                  c.gt(e.id, 1);
                  c.gt(e.id, (Integer) null);
                })
            .select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where t0_.ID > t0_.ID and t0_.ID > 1", sql.getFormattedSql());
  }

  @Test
  void where_le() {
    Emp_ e = new Emp_();
    Buildable<?> stmt =
        nativeSql
            .from(e)
            .where(
                c -> {
                  c.le(e.id, e.id);
                  c.le(e.id, 1);
                  c.le(e.id, (Integer) null);
                })
            .select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where t0_.ID <= t0_.ID and t0_.ID <= 1", sql.getFormattedSql());
  }

  @Test
  void where_lt() {
    Emp_ e = new Emp_();
    Buildable<?> stmt =
        nativeSql
            .from(e)
            .where(
                c -> {
                  c.lt(e.id, e.id);
                  c.lt(e.id, 1);
                  c.lt(e.id, (Integer) null);
                })
            .select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where t0_.ID < t0_.ID and t0_.ID < 1", sql.getFormattedSql());
  }

  @Test
  void where_isNull() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).where(c -> c.isNull(e.id)).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_ where t0_.ID is null", sql.getFormattedSql());
  }

  @Test
  void where_isNotNull() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).where(c -> c.isNotNull(e.id)).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_ where t0_.ID is not null", sql.getFormattedSql());
  }

  @Test
  void where_eqOrIsNull() {
    Emp_ e = new Emp_();
    Buildable<?> stmt =
        nativeSql
            .from(e)
            .where(
                c -> {
                  c.eqOrIsNull(e.id, 1);
                  c.eqOrIsNull(e.id, null);
                })
            .select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where t0_.ID = 1 and t0_.ID is null", sql.getFormattedSql());
  }

  @Test
  void where_neOrIsNotNull() {
    Emp_ e = new Emp_();
    Buildable<?> stmt =
        nativeSql
            .from(e)
            .where(
                c -> {
                  c.neOrIsNotNull(e.id, 1);
                  c.neOrIsNotNull(e.id, null);
                })
            .select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where t0_.ID <> 1 and t0_.ID is not null",
        sql.getFormattedSql());
  }

  @Test
  void where_like() {
    Emp_ e = new Emp_();
    Buildable<?> stmt =
        nativeSql
            .from(e)
            .where(
                c -> {
                  c.like(e.name, "a$");
                  c.like(e.name, null);
                })
            .select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_ where t0_.NAME like 'a$'", sql.getFormattedSql());
  }

  @Test
  void where_like_escape() {
    Emp_ e = new Emp_();
    Buildable<?> stmt =
        nativeSql.from(e).where(c -> c.like(e.name, "a$", LikeOption.escape())).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where t0_.NAME like 'a$$' escape '$'", sql.getFormattedSql());
  }

  @Test
  void where_like_escape_escapeChar() {
    Emp_ e = new Emp_();
    Buildable<?> stmt =
        nativeSql.from(e).where(c -> c.like(e.name, "a¥", LikeOption.escape('¥'))).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where t0_.NAME like 'a¥¥' escape '¥'", sql.getFormattedSql());
  }

  @Test
  void where_like_prefix() {
    Emp_ e = new Emp_();
    Buildable<?> stmt =
        nativeSql.from(e).where(c -> c.like(e.name, "a$", LikeOption.prefix())).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where t0_.NAME like 'a$$%' escape '$'", sql.getFormattedSql());
  }

  @Test
  void where_like_infix() {
    Emp_ e = new Emp_();
    Buildable<?> stmt =
        nativeSql.from(e).where(c -> c.like(e.name, "a$", LikeOption.infix())).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where t0_.NAME like '%a$$%' escape '$'", sql.getFormattedSql());
  }

  @Test
  void where_like_suffix() {
    Emp_ e = new Emp_();
    Buildable<?> stmt =
        nativeSql.from(e).where(c -> c.like(e.name, "a$", LikeOption.suffix())).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where t0_.NAME like '%a$$' escape '$'", sql.getFormattedSql());
  }

  @Test
  void where_notLike() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).where(c -> c.notLike(e.name, "a%")).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_ where t0_.NAME not like 'a%'", sql.getFormattedSql());
  }

  @Test
  void where_between() {
    Emp_ e = new Emp_();
    Buildable<?> stmt =
        nativeSql
            .from(e)
            .where(
                c -> {
                  c.between(e.id, 1, 10);
                  c.between(e.id, null, 10);
                  c.between(e.id, 1, null);
                  c.between(e.id, null, null);
                })
            .select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_ where t0_.ID between 1 and 10", sql.getFormattedSql());
  }

  @Test
  void where_in() {
    Emp_ e = new Emp_();
    Buildable<?> stmt =
        nativeSql
            .from(e)
            .where(
                c -> {
                  c.in(e.id, Arrays.asList(1, 2));
                  c.in(e.id, (List<Integer>) null);
                })
            .select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_ where t0_.ID in (1, 2)", sql.getFormattedSql());
  }

  @Test
  void where_notIn() {
    Emp_ e = new Emp_();
    Buildable<?> stmt =
        nativeSql
            .from(e)
            .where(
                c -> {
                  c.notIn(e.id, Arrays.asList(1, 2));
                  c.notIn(e.id, (List<Integer>) null);
                })
            .select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_ where t0_.ID not in (1, 2)", sql.getFormattedSql());
  }

  @Test
  void where_in_tuple2() {
    Emp_ e = new Emp_();
    Buildable<?> stmt =
        nativeSql
            .from(e)
            .where(
                c -> {
                  c.in(
                      new Tuple2<>(e.id, e.name),
                      Arrays.asList(new Tuple2<>(1, "a"), new Tuple2<>(2, "b")));
                  c.in(new Tuple2<>(e.id, e.name), (List<Tuple2<Integer, String>>) null);
                })
            .select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where (t0_.ID, t0_.NAME) in ((1, 'a'), (2, 'b'))",
        sql.getFormattedSql());
  }

  @Test
  void where_notIn_tuple2() {
    Emp_ e = new Emp_();
    Buildable<?> stmt =
        nativeSql
            .from(e)
            .where(
                c -> {
                  c.notIn(
                      new Tuple2<>(e.id, e.name),
                      Arrays.asList(new Tuple2<>(1, "a"), new Tuple2<>(2, "b")));
                  c.notIn(new Tuple2<>(e.id, e.name), (List<Tuple2<Integer, String>>) null);
                })
            .select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where (t0_.ID, t0_.NAME) not in ((1, 'a'), (2, 'b'))",
        sql.getFormattedSql());
  }

  @Test
  void where_in_subQuery() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    Buildable<?> stmt =
        nativeSql.from(e).where(c -> c.in(e.id, c.from(d).select(d.id))).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where t0_.ID in (select t1_.ID from CATA.DEPT t1_)",
        sql.getFormattedSql());
  }

  @Test
  void where_notIn_subQuery() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    Buildable<?> stmt =
        nativeSql.from(e).where(c -> c.notIn(e.id, c.from(d).select(d.id))).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where t0_.ID not in (select t1_.ID from CATA.DEPT t1_)",
        sql.getFormattedSql());
  }

  @Test
  void where_in_tuple2_subQuery() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    Buildable<?> stmt =
        nativeSql
            .from(e)
            .where(c -> c.in(new Tuple2<>(e.id, e.name), c.from(d).select(d.id, d.name)))
            .select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where (t0_.ID, t0_.NAME) in (select t1_.ID, t1_.NAME from CATA.DEPT t1_)",
        sql.getFormattedSql());
  }

  @Test
  void where_notIn_tuple2_subQuery() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    Buildable<?> stmt =
        nativeSql
            .from(e)
            .where(c -> c.notIn(new Tuple2<>(e.id, e.name), c.from(d).select(d.id, d.name)))
            .select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where (t0_.ID, t0_.NAME) not in (select t1_.ID, t1_.NAME from CATA.DEPT t1_)",
        sql.getFormattedSql());
  }

  @Test
  void where_exist() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    Buildable<?> stmt = nativeSql.from(e).where(c -> c.exists(c.from(d).select(d.id))).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where exists (select t1_.ID from CATA.DEPT t1_)",
        sql.getFormattedSql());
  }

  @Test
  void where_exist_without_select() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    Buildable<?> stmt = nativeSql.from(e).where(c -> c.exists(c.from(d))).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where exists (select t1_.ID, t1_.NAME from CATA.DEPT t1_)",
        sql.getFormattedSql());
  }

  @Test
  void where_notExist() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    Buildable<?> stmt =
        nativeSql.from(e).where(c -> c.notExists(c.from(d).select(d.id))).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where not exists (select t1_.ID from CATA.DEPT t1_)",
        sql.getFormattedSql());
  }

  @Test
  void where_and() {
    Emp_ e = new Emp_();
    Buildable<?> stmt =
        nativeSql
            .from(e)
            .where(
                c -> {
                  c.eq(e.id, 1);
                  c.and(
                      () -> {
                        c.eq(e.name, "a");
                        c.eq(e.version, 1);
                        c.and(() -> c.eq(e.salary, new BigDecimal("20")));
                      });
                  c.eq(e.salary, new BigDecimal("10"));
                })
            .select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where t0_.ID = 1 and (t0_.NAME = 'a' and t0_.VERSION = 1 and (t0_.SALARY = 20)) and t0_.SALARY = 10",
        sql.getFormattedSql());
  }

  @Test
  void where_or() {
    Emp_ e = new Emp_();
    Buildable<?> stmt =
        nativeSql
            .from(e)
            .where(
                c -> {
                  c.eq(e.id, 1);
                  c.or(
                      () -> {
                        c.eq(e.name, "a");
                        c.eq(e.version, 1);
                        c.or(() -> c.eq(e.salary, new BigDecimal("20")));
                      });
                  c.eq(e.salary, new BigDecimal("10"));
                })
            .select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where t0_.ID = 1 or (t0_.NAME = 'a' and t0_.VERSION = 1 or (t0_.SALARY = 20)) and t0_.SALARY = 10",
        sql.getFormattedSql());
  }

  @Test
  void where_not() {
    Emp_ e = new Emp_();
    Buildable<?> stmt =
        nativeSql
            .from(e)
            .where(
                c -> {
                  c.eq(e.id, 1);
                  c.not(
                      () -> {
                        c.eq(e.name, "a");
                        c.eq(e.version, 1);
                        c.not(() -> c.eq(e.salary, new BigDecimal("20")));
                      });
                  c.eq(e.salary, new BigDecimal("10"));
                })
            .select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where t0_.ID = 1 and not (t0_.NAME = 'a' and t0_.VERSION = 1 and not (t0_.SALARY = 20)) and t0_.SALARY = 10",
        sql.getFormattedSql());
  }

  @Test
  void where_empty_empty() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).where(c -> {}).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void where_empty_and() {
    Emp_ e = new Emp_();
    Buildable<?> stmt =
        nativeSql
            .from(e)
            .where(
                c -> {
                  c.eq(e.id, 1);
                  c.and(() -> {});
                  c.eq(e.salary, new BigDecimal("10"));
                })
            .select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where t0_.ID = 1 and t0_.SALARY = 10", sql.getFormattedSql());
  }

  @Test
  void where_empty_or() {
    Emp_ e = new Emp_();
    Buildable<?> stmt =
        nativeSql
            .from(e)
            .where(
                c -> {
                  c.eq(e.id, 1);
                  c.or(() -> {});
                  c.eq(e.salary, new BigDecimal("10"));
                })
            .select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where t0_.ID = 1 and t0_.SALARY = 10", sql.getFormattedSql());
  }

  @Test
  void where_empty_not() {
    Emp_ e = new Emp_();
    Buildable<?> stmt =
        nativeSql
            .from(e)
            .where(
                c -> {
                  c.eq(e.id, 1);
                  c.not(() -> {});
                  c.eq(e.salary, new BigDecimal("10"));
                })
            .select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where t0_.ID = 1 and t0_.SALARY = 10", sql.getFormattedSql());
  }

  @Test
  void innerJoin() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    Buildable<?> stmt = nativeSql.from(e).innerJoin(d, on -> on.eq(e.id, d.id)).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ inner join CATA.DEPT t1_ on (t0_.ID = t1_.ID)",
        sql.getFormattedSql());
  }

  @Test
  void innerJoin_empty() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    Buildable<?> stmt = nativeSql.from(e).innerJoin(d, on -> {}).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void leftJoin() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    Buildable<?> stmt = nativeSql.from(e).leftJoin(d, on -> on.eq(e.id, d.id)).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ left outer join CATA.DEPT t1_ on (t0_.ID = t1_.ID)",
        sql.getFormattedSql());
  }

  @Test
  void leftJoin_empty() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    Buildable<?> stmt = nativeSql.from(e).leftJoin(d, on -> {}).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void join_on() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    Buildable<?> stmt =
        nativeSql
            .from(e)
            .innerJoin(
                d,
                on -> {
                  on.eq(e.id, d.id);
                  on.ne(e.id, d.id);
                  on.ge(e.id, d.id);
                  on.gt(e.id, d.id);
                  on.le(e.id, d.id);
                  on.lt(e.id, d.id);
                  on.isNull(e.name);
                  on.isNotNull(e.name);
                })
            .select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ inner join CATA.DEPT t1_ on (t0_.ID = t1_.ID and t0_.ID <> t1_.ID and t0_.ID >= t1_.ID and t0_.ID > t1_.ID and t0_.ID <= t1_.ID and t0_.ID < t1_.ID and t0_.NAME is null and t0_.NAME is not null)",
        sql.getFormattedSql());
  }

  @Test
  void orderBy() {
    Emp_ e = new Emp_();
    Buildable<?> stmt =
        nativeSql
            .from(e)
            .orderBy(
                c -> {
                  c.asc(e.id);
                  c.desc(e.name);
                  c.asc(e.salary);
                  c.desc(e.version);
                })
            .select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ order by t0_.ID asc, t0_.NAME desc, t0_.SALARY asc, t0_.VERSION desc",
        sql.getFormattedSql());
  }

  @Test
  void orderBy_empty() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).orderBy(c -> {}).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void limit() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).limit(10).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ offset 0 rows fetch first 10 rows only", sql.getFormattedSql());
  }

  @Test
  void limit_null() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).limit(null).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void offset() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).offset(10).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_ offset 10 rows", sql.getFormattedSql());
  }

  @Test
  void offset_null() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).offset(null).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void forUpdate() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).forUpdate().select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_ for update", sql.getFormattedSql());
  }

  @Test
  void forUpdate_db2() {
    NativeSql nativeSql =
        new NativeSql(
            new MockConfig() {
              @Override
              public Dialect getDialect() {
                return new Db2Dialect();
              }
            });

    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).where(c -> c.eq(e.id, 1)).forUpdate().select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where t0_.ID = 1 for update with rs", sql.getFormattedSql());
  }

  @Test
  void forUpdate_mssql() {
    NativeSql nativeSql =
        new NativeSql(
            new MockConfig() {
              @Override
              public Dialect getDialect() {
                return new MssqlDialect();
              }
            });

    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).where(c -> c.eq(e.id, 1)).forUpdate().select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ with (updlock, rowlock) where t0_.ID = 1",
        sql.getFormattedSql());
  }

  @Test
  void forUpdate_mssql_nowait() {
    NativeSql nativeSql =
        new NativeSql(
            new MockConfig() {
              @Override
              public Dialect getDialect() {
                return new MssqlDialect();
              }
            });

    Emp_ e = new Emp_();
    Buildable<?> stmt =
        nativeSql
            .from(e)
            .where(c -> c.eq(e.id, 1))
            .forUpdate(ForUpdateOption.noWait())
            .select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ with (updlock, rowlock, nowait) where t0_.ID = 1",
        sql.getFormattedSql());
  }

  @Test
  void forUpdate_oracle_nowait() {
    NativeSql nativeSql =
        new NativeSql(
            new MockConfig() {
              @Override
              public Dialect getDialect() {
                return new OracleDialect();
              }
            });

    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).forUpdate(ForUpdateOption.noWait()).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_ for update nowait", sql.getFormattedSql());
  }

  @Test
  void forUpdate_oracle_nowait_withColumn() {
    NativeSql nativeSql =
        new NativeSql(
            new MockConfig() {
              @Override
              public Dialect getDialect() {
                return new OracleDialect();
              }
            });

    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    Buildable<?> stmt =
        nativeSql
            .from(e)
            .innerJoin(d, on -> on.eq(e.id, d.id))
            .forUpdate(ForUpdateOption.noWait(e.id, d.id))
            .select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ inner join CATA.DEPT t1_ on (t0_.ID = t1_.ID) for update of t0_.ID, t1_.ID nowait",
        sql.getFormattedSql());
  }

  @Test
  void forUpdate_oracle_wait_withColumn() {
    NativeSql nativeSql =
        new NativeSql(
            new MockConfig() {
              @Override
              public Dialect getDialect() {
                return new OracleDialect();
              }
            });

    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    Buildable<?> stmt =
        nativeSql
            .from(e)
            .innerJoin(d, on -> on.eq(e.id, d.id))
            .forUpdate(ForUpdateOption.wait(5, e.id, d.id))
            .select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ inner join CATA.DEPT t1_ on (t0_.ID = t1_.ID) for update of t0_.ID, t1_.ID wait 5",
        sql.getFormattedSql());
  }

  @Test
  void forUpdate_postgres_nowait_withTable() {
    NativeSql nativeSql =
        new NativeSql(
            new MockConfig() {
              @Override
              public Dialect getDialect() {
                return new PostgresDialect();
              }
            });

    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    Buildable<?> stmt =
        nativeSql
            .from(e)
            .innerJoin(d, on -> on.eq(e.id, d.id))
            .forUpdate(ForUpdateOption.noWait(e.id, d.id))
            .select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ inner join CATA.DEPT t1_ on (t0_.ID = t1_.ID) for update of t0_, t1_ nowait",
        sql.getFormattedSql());
  }

  @Test
  void forUpdate_none() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).forUpdate(ForUpdateOption.none()).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void groupBy() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).groupBy(e.id, e.name).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_ group by t0_.ID, t0_.NAME", sql.getFormattedSql());
  }

  @Test
  void groupBy_empty() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).groupBy().select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void groupBy_auto_generation() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).select(e.id, e.salary, count(e.name), max(e.id));

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID, t0_.SALARY, count(t0_.NAME), max(t0_.ID) from EMP t0_ group by t0_.ID, t0_.SALARY",
        sql.getFormattedSql());
  }

  @Test
  void having() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).having(c -> c.eq(e.id, 1)).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_ having t0_.ID = 1", sql.getFormattedSql());
  }

  @Test
  void having_empty() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).having(c -> {}).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void test_select() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).select(e.id, e.name);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID, t0_.NAME from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void selectTo() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).selectTo(e, e.name);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID, t0_.NAME from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void selectTo_no_propertyMetamodels() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).selectTo(e);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void selectTo_illegal_entityMetamodel() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    DomaException ex = assertThrows(DomaException.class, () -> nativeSql.from(e).selectTo(d));
    assertEquals(Message.DOMA6007, ex.getMessageResource());
    System.out.println(ex.getMessage());
  }

  @Test
  void selectTo_illegal_propertyMetamodel() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    DomaException ex =
        assertThrows(DomaException.class, () -> nativeSql.from(e).selectTo(e, d.name));
    assertEquals(Message.DOMA6008, ex.getMessageResource());
    System.out.println(ex.getMessage());
  }

  @Test
  void aggregateFunctions() {
    Emp_ e = new Emp_();
    Buildable<?> stmt =
        nativeSql.from(e).select(avg(e.id), count(e.id), count(), max(e.id), min(e.id), sum(e.id));

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select avg(t0_.ID), count(t0_.ID), count(*), max(t0_.ID), min(t0_.ID), sum(t0_.ID) from EMP t0_",
        sql.getFormattedSql());
  }

  @Test
  void aggregateFunction_countDistinct() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).select(countDistinct(e.id));

    Sql<?> sql = stmt.asSql();
    assertEquals("select count(distinct t0_.ID) from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void union() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    SetOperand<String> stmt1 = nativeSql.from(e).select(e.name);
    SetOperand<String> stmt2 = nativeSql.from(d).select(d.name);
    SetOperand<String> stmt3 = stmt1.union(stmt2);

    Sql<?> sql1 = stmt1.asSql();
    assertEquals("select t0_.NAME from EMP t0_", sql1.getFormattedSql());

    Sql<?> sql2 = stmt2.asSql();
    assertEquals("select t0_.NAME from CATA.DEPT t0_", sql2.getFormattedSql());

    Sql<?> sql3 = stmt3.asSql();
    assertEquals(
        "select t0_.NAME from EMP t0_ union select t0_.NAME from CATA.DEPT t0_",
        sql3.getFormattedSql());
  }

  @Test
  void union_orderBy() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    SetOperand<String> stmt1 = nativeSql.from(e).select(e.name);
    SetOperand<String> stmt2 = nativeSql.from(d).select(d.name);
    SetOperand<String> stmt3 = stmt1.union(stmt2).orderBy(c -> c.asc(1));

    Sql<?> sql1 = stmt1.asSql();
    assertEquals("select t0_.NAME from EMP t0_", sql1.getFormattedSql());

    Sql<?> sql2 = stmt2.asSql();
    assertEquals("select t0_.NAME from CATA.DEPT t0_", sql2.getFormattedSql());

    Sql<?> sql3 = stmt3.asSql();
    assertEquals(
        "(select t0_.NAME from EMP t0_) union (select t0_.NAME from CATA.DEPT t0_) order by 1 asc",
        sql3.getFormattedSql());
  }

  @Test
  void multi_union() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    NoIdEmp_ n = new NoIdEmp_();
    SetOperand<String> stmt1 = nativeSql.from(e).select(e.name);
    SetOperand<String> stmt2 = nativeSql.from(d).select(d.name);
    SetOperand<String> stmt3 = nativeSql.from(n).select(n.name);
    SetOperand<String> stmt4 = stmt1.union(stmt2).union(stmt3);

    Sql<?> sql = stmt4.asSql();
    assertEquals(
        "select t0_.NAME from EMP t0_ union select t0_.NAME from CATA.DEPT t0_ union select t0_.NAME from NO_ID_EMP t0_",
        sql.getFormattedSql());
  }

  @Test
  void multi_union_orderBy() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    NoIdEmp_ n = new NoIdEmp_();
    SetOperand<String> stmt1 = nativeSql.from(e).select(e.name);
    SetOperand<String> stmt2 = nativeSql.from(d).select(d.name);
    SetOperand<String> stmt3 = nativeSql.from(n).select(n.name);
    SetOperand<String> stmt4 = stmt1.union(stmt2).union(stmt3).orderBy(c -> c.desc(1));

    Sql<?> sql = stmt4.asSql();
    assertEquals(
        "(select t0_.NAME from EMP t0_ union select t0_.NAME from CATA.DEPT t0_) union (select t0_.NAME from NO_ID_EMP t0_) order by 1 desc",
        sql.getFormattedSql());
  }

  @Test
  void unionAll() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    SetOperand<String> stmt1 = nativeSql.from(e).select(e.name);
    SetOperand<String> stmt2 = nativeSql.from(d).select(d.name);
    SetOperand<String> stmt3 = stmt1.unionAll(stmt2);
    Sql<?> sql = stmt3.asSql();

    assertEquals(
        "select t0_.NAME from EMP t0_ union all select t0_.NAME from CATA.DEPT t0_",
        sql.getFormattedSql());
  }

  @Test
  void options_comment() {
    Config config =
        new MockConfig() {
          @Override
          public Commenter getCommenter() {
            return new Commenter() {
              @Override
              public String comment(String sql, CommentContext context) {
                if (context.getMessage().isPresent()) {
                  String message = context.getMessage().get();
                  return String.format("// %s\n%s", message, sql);
                }
                return sql;
              }
            };
          }
        };

    NativeSql nativeSql = new NativeSql(config);

    Emp_ e = new Emp_();
    Statement<List<Emp>> stmt = nativeSql.from(e, options -> options.setComment("hello"));

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "// hello\nselect t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_",
        sql.getFormattedSql());
  }

  @Test
  void peek() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    SetOperand<String> stmt =
        nativeSql
            .from(e)
            .select(e.name)
            .peek(System.out::println)
            .union(nativeSql.from(d).select(d.name).peek(System.out::println))
            .peek(System.out::println);
  }

  @Test
  void distinct() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).distinct().where(c -> c.eq(e.name, "a"));
    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select distinct t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ where t0_.NAME = 'a'",
        sql.getFormattedSql());
  }

  @Test
  void distinct_normal() {
    Emp_ e = new Emp_();
    Buildable<?> stmt =
        nativeSql.from(e).distinct(DistinctOption.basic()).where(c -> c.eq(e.name, "a"));
    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select distinct t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ where t0_.NAME = 'a'",
        sql.getFormattedSql());
  }

  @Test
  void distinct_none() {
    Emp_ e = new Emp_();
    Buildable<?> stmt =
        nativeSql.from(e).distinct(DistinctOption.none()).where(c -> c.eq(e.name, "a"));
    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ where t0_.NAME = 'a'",
        sql.getFormattedSql());
  }

  @Test
  void expression_concat() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).select(concat(e.name, "a"));
    Sql<?> sql = stmt.asSql();
    assertEquals("select concat(t0_.NAME, 'a') from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void expression_concat_mssql2008() {
    NativeSql nativeSql =
        new NativeSql(
            new MockConfig() {
              @Override
              public Dialect getDialect() {
                return new Mssql2008Dialect();
              }
            });

    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).select(concat(e.name, "a"));
    Sql<?> sql = stmt.asSql();
    assertEquals("select (t0_.NAME + 'a') from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void expression_lower() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).select(lower(e.name));
    Sql<?> sql = stmt.asSql();
    assertEquals("select lower(t0_.NAME) from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void expression_ltrim() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).select(ltrim(e.name));
    Sql<?> sql = stmt.asSql();
    assertEquals("select ltrim(t0_.NAME) from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void expression_rtrim() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).select(rtrim(e.name));
    Sql<?> sql = stmt.asSql();
    assertEquals("select rtrim(t0_.NAME) from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void expression_trim() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).select(trim(e.name));
    Sql<?> sql = stmt.asSql();
    assertEquals("select trim(t0_.NAME) from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void expression_upper() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).select(upper(e.name));
    Sql<?> sql = stmt.asSql();
    assertEquals("select upper(t0_.NAME) from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void expression_stringLiteral() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).select(literal("a"));
    Sql<?> sql = stmt.asSql();
    assertEquals("select 'a' from EMP t0_", sql.getRawSql());
  }

  @Test
  void expression_intLiteral() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).select(literal(123));
    Sql<?> sql = stmt.asSql();
    assertEquals("select 123 from EMP t0_", sql.getRawSql());
  }

  @Test
  void expression_when_returns_same_type_with_comparison_type() {
    Emp_ e = new Emp_();
    Buildable<?> stmt =
        nativeSql.from(e).select(when(c -> c.eq(e.name, literal("a"), literal("b")), literal("z")));
    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select case when t0_.NAME = 'a' then 'b' else 'z' end from EMP t0_", sql.getRawSql());
  }

  @Test
  void expression_when_returns_different_type_with_comparison_type() {
    Emp_ e = new Emp_();
    Buildable<?> stmt =
        nativeSql.from(e).select(when(c -> c.eq(e.name, literal("a"), literal(1)), literal(0)));
    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select case " + "when t0_.NAME = 'a' then 1 else 0 end from EMP t0_", sql.getRawSql());
  }

  @Test
  void expression_when_operators() {
    Emp_ e = new Emp_();
    Buildable<?> stmt =
        nativeSql
            .from(e)
            .select(
                when(
                    c -> {
                      c.eq(e.name, literal("a"), literal("b"));
                      c.ne(e.name, literal("c"), literal("d"));
                      c.ge(e.name, literal("e"), literal("f"));
                      c.gt(e.name, literal("g"), literal("h"));
                      c.le(e.name, literal("i"), literal("j"));
                      c.lt(e.name, literal("k"), literal("l"));
                      c.isNull(e.name, literal("m"));
                      c.isNotNull(e.name, literal("n"));
                    },
                    literal("z")));
    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select case "
            + "when t0_.NAME = 'a' then 'b' "
            + "when t0_.NAME <> 'c' then 'd' "
            + "when t0_.NAME >= 'e' then 'f' "
            + "when t0_.NAME > 'g' then 'h' "
            + "when t0_.NAME <= 'i' then 'j' "
            + "when t0_.NAME < 'k' then 'l' "
            + "when t0_.NAME is null then 'm' "
            + "when t0_.NAME is not null then 'n' "
            + "else 'z' end from EMP t0_",
        sql.getRawSql());
  }

  @Test
  void expression_when_empty() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).select(when(c -> {}, literal("c")));
    Sql<?> sql = stmt.asSql();
    assertEquals("select 'c' from EMP t0_", sql.getRawSql());
  }

  @Test
  void expression_select() {
    Emp_ e = new Emp_();
    Emp_ e2 = new Emp_();
    SelectExpression<Integer> expression = select(c -> c.from(e2).select(e2.id));
    Buildable<?> stmt = nativeSql.from(e).select(expression);
    Sql<?> sql = stmt.asSql();
    assertEquals("select (select t1_.ID from EMP t1_) from EMP t0_", sql.getRawSql());
  }

  @Test
  void expression_avg() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).select(avg(e.salary));
    Sql<?> sql = stmt.asSql();
    assertEquals("select avg(t0_.SALARY) from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void expression_avgAsDouble() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = nativeSql.from(e).select(avgAsDouble(e.salary));
    Sql<?> sql = stmt.asSql();
    assertEquals("select avg(t0_.SALARY) from EMP t0_", sql.getFormattedSql());
  }
}

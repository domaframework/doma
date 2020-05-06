package org.seasar.doma.jdbc.criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.seasar.doma.jdbc.criteria.AggregateFunctions.avg;
import static org.seasar.doma.jdbc.criteria.AggregateFunctions.count;
import static org.seasar.doma.jdbc.criteria.AggregateFunctions.max;
import static org.seasar.doma.jdbc.criteria.AggregateFunctions.min;
import static org.seasar.doma.jdbc.criteria.AggregateFunctions.sum;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.criteria.entity.Dept_;
import org.seasar.doma.jdbc.criteria.entity.Emp;
import org.seasar.doma.jdbc.criteria.entity.Emp_;
import org.seasar.doma.jdbc.criteria.entity.NoIdEmp_;
import org.seasar.doma.jdbc.criteria.statement.Collectable;
import org.seasar.doma.jdbc.criteria.statement.Mappable;
import org.seasar.doma.jdbc.criteria.statement.Statement;

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
    Mappable<Emp> stmt =
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
        "select t0_.ID from EMP t0_ where t0_.ID = t0_.ID and t0_.ID = 1 and t0_.ID is null",
        sql.getFormattedSql());
  }

  @Test
  void where_ne() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt =
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
        "select t0_.ID from EMP t0_ where t0_.ID <> t0_.ID and t0_.ID <> 1 and t0_.ID is not null",
        sql.getFormattedSql());
  }

  @Test
  void where_ge() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt =
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
        "select t0_.ID from EMP t0_ where t0_.ID >= t0_.ID and t0_.ID >= 1 and t0_.ID >= null",
        sql.getFormattedSql());
  }

  @Test
  void where_gt() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt =
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
        "select t0_.ID from EMP t0_ where t0_.ID > t0_.ID and t0_.ID > 1 and t0_.ID > null",
        sql.getFormattedSql());
  }

  @Test
  void where_le() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt =
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
        "select t0_.ID from EMP t0_ where t0_.ID <= t0_.ID and t0_.ID <= 1 and t0_.ID <= null",
        sql.getFormattedSql());
  }

  @Test
  void where_lt() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt =
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
        "select t0_.ID from EMP t0_ where t0_.ID < t0_.ID and t0_.ID < 1 and t0_.ID < null",
        sql.getFormattedSql());
  }

  @Test
  void where_isNull() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt = nativeSql.from(e).where(c -> c.isNull(e.id)).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_ where t0_.ID is null", sql.getFormattedSql());
  }

  @Test
  void where_isNotNull() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt = nativeSql.from(e).where(c -> c.isNotNull(e.id)).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_ where t0_.ID is not null", sql.getFormattedSql());
  }

  @Test
  void where_like() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt = nativeSql.from(e).where(c -> c.like(e.name, "a$")).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_ where t0_.NAME like 'a$'", sql.getFormattedSql());
  }

  @Test
  void where_like_escape() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt =
        nativeSql.from(e).where(c -> c.like(e.name, "a$", LikeOption.ESCAPE)).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where t0_.NAME like 'a$$' escape '$'", sql.getFormattedSql());
  }

  @Test
  void where_like_prefix() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt =
        nativeSql.from(e).where(c -> c.like(e.name, "a$", LikeOption.PREFIX)).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where t0_.NAME like 'a$$%' escape '$'", sql.getFormattedSql());
  }

  @Test
  void where_like_infix() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt =
        nativeSql.from(e).where(c -> c.like(e.name, "a$", LikeOption.INFIX)).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where t0_.NAME like '%a$$%' escape '$'", sql.getFormattedSql());
  }

  @Test
  void where_like_suffix() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt =
        nativeSql.from(e).where(c -> c.like(e.name, "a$", LikeOption.SUFFIX)).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where t0_.NAME like '%a$$' escape '$'", sql.getFormattedSql());
  }

  @Test
  void where_notLike() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt = nativeSql.from(e).where(c -> c.notLike(e.name, "a%")).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_ where t0_.NAME not like 'a%'", sql.getFormattedSql());
  }

  @Test
  void where_between() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt = nativeSql.from(e).where(c -> c.between(e.id, 1, 10)).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_ where t0_.ID between 1 and 10", sql.getFormattedSql());
  }

  @Test
  void where_in() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt = nativeSql.from(e).where(c -> c.in(e.id, Arrays.asList(1, 2))).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_ where t0_.ID in (1, 2)", sql.getFormattedSql());
  }

  @Test
  void where_notIn() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt =
        nativeSql.from(e).where(c -> c.notIn(e.id, Arrays.asList(1, 2))).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_ where t0_.ID not in (1, 2)", sql.getFormattedSql());
  }

  @Test
  void where_in_tuple2() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt =
        nativeSql
            .from(e)
            .where(
                c ->
                    c.in(
                        new Tuple2<>(e.id, e.name),
                        Arrays.asList(new Tuple2<>(1, "a"), new Tuple2<>(2, "b"))))
            .select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where (t0_.ID, t0_.NAME) in ((1, 'a'), (2, 'b'))",
        sql.getFormattedSql());
  }

  @Test
  void where_notIn_tuple2() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt =
        nativeSql
            .from(e)
            .where(
                c ->
                    c.notIn(
                        new Tuple2<>(e.id, e.name),
                        Arrays.asList(new Tuple2<>(1, "a"), new Tuple2<>(2, "b"))))
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
    Mappable<Emp> stmt =
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
    Mappable<Emp> stmt =
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
    Mappable<Emp> stmt =
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
    Mappable<Emp> stmt =
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
    Mappable<Emp> stmt =
        nativeSql.from(e).where(c -> c.exists(c.from(d).select(d.id))).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where exists (select t1_.ID from CATA.DEPT t1_)",
        sql.getFormattedSql());
  }

  @Test
  void where_notExist() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    Mappable<Emp> stmt =
        nativeSql.from(e).where(c -> c.notExists(c.from(d).select(d.id))).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ where not exists (select t1_.ID from CATA.DEPT t1_)",
        sql.getFormattedSql());
  }

  @Test
  void where_and() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt =
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
    Mappable<Emp> stmt =
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
    Mappable<Emp> stmt =
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
    Mappable<Emp> stmt = nativeSql.from(e).where(c -> {}).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void where_empty_and() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt =
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
    Mappable<Emp> stmt =
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
    Mappable<Emp> stmt =
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
    Mappable<Emp> stmt = nativeSql.from(e).innerJoin(d, on -> on.eq(e.id, d.id)).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ inner join CATA.DEPT t1_ on (t0_.ID = t1_.ID)",
        sql.getFormattedSql());
  }

  @Test
  void innerJoin_empty() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    Mappable<Emp> stmt = nativeSql.from(e).innerJoin(d, on -> {}).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void leftJoin() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    Mappable<Emp> stmt = nativeSql.from(e).leftJoin(d, on -> on.eq(e.id, d.id)).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ left outer join CATA.DEPT t1_ on (t0_.ID = t1_.ID)",
        sql.getFormattedSql());
  }

  @Test
  void leftJoin_empty() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    Mappable<Emp> stmt = nativeSql.from(e).leftJoin(d, on -> {}).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void join_on() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    Mappable<Emp> stmt =
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
                })
            .select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID from EMP t0_ inner join CATA.DEPT t1_ on (t0_.ID = t1_.ID and t0_.ID <> t1_.ID and t0_.ID >= t1_.ID and t0_.ID > t1_.ID and t0_.ID <= t1_.ID and t0_.ID < t1_.ID)",
        sql.getFormattedSql());
  }

  @Test
  void orderBy() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt =
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
    Mappable<Emp> stmt = nativeSql.from(e).orderBy(c -> {}).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void limit() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt = nativeSql.from(e).limit(10).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_ limit 10", sql.getFormattedSql());
  }

  @Test
  void limit_null() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt = nativeSql.from(e).limit(null).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void offset() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt = nativeSql.from(e).offset(10).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_ offset 10", sql.getFormattedSql());
  }

  @Test
  void offset_null() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt = nativeSql.from(e).offset(null).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void forUpdate() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt = nativeSql.from(e).forUpdate().select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_ for update", sql.getFormattedSql());
  }

  @Test
  void forUpdate_nowait() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt = nativeSql.from(e).forUpdate(ForUpdateOption.NOWAIT).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_ for update nowait", sql.getFormattedSql());
  }

  @Test
  void forUpdate_disabled() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt = nativeSql.from(e).forUpdate(ForUpdateOption.DISABLED).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void groupBy() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt = nativeSql.from(e).groupBy(e.id, e.name).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_ group by t0_.ID, t0_.NAME", sql.getFormattedSql());
  }

  @Test
  void groupBy_empty() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt = nativeSql.from(e).groupBy().select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void having() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt = nativeSql.from(e).having(c -> c.eq(e.id, 1)).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_ having t0_.ID = 1", sql.getFormattedSql());
  }

  @Test
  void having_empty() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt = nativeSql.from(e).having(c -> {}).select(e.id);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void select() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt = nativeSql.from(e).select(e.id, e.name);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID, t0_.NAME from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void select_empty() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt = nativeSql.from(e).select();

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void aggregateFunctions() {
    Emp_ e = new Emp_();
    Mappable<Emp> stmt =
        nativeSql.from(e).select(avg(e.id), count(e.id), count(), max(e.id), min(e.id), sum(e.id));

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select avg(t0_.ID), count(t0_.ID), count(*), max(t0_.ID), min(t0_.ID), sum(t0_.ID) from EMP t0_",
        sql.getFormattedSql());
  }

  @Test
  void map() {
    Emp_ e = new Emp_();
    Collectable<String> stmt = nativeSql.from(e).<String>select(e.name).map(row -> row.get(e.name));

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.NAME from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void union() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    Mappable<String> stmt1 = nativeSql.from(e).select(e.name);
    Mappable<String> stmt2 = nativeSql.from(d).select(d.name);
    Collectable<String> stmt3 = stmt1.union(stmt2).map(row -> row.get(e.name));

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
  void multi_union() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    NoIdEmp_ n = new NoIdEmp_();
    Mappable<String> stmt1 = nativeSql.from(e).select(e.name);
    Mappable<String> stmt2 = nativeSql.from(d).select(d.name);
    Mappable<String> stmt3 = nativeSql.from(n).select(n.name);
    Collectable<String> stmt4 = stmt1.union(stmt2).union(stmt3).map(row -> row.get(e.name));

    Sql<?> sql = stmt4.asSql();
    assertEquals(
        "select t0_.NAME from EMP t0_ union select t0_.NAME from CATA.DEPT t0_ union select t0_.NAME from NO_ID_EMP t0_",
        sql.getFormattedSql());
  }

  @Test
  void unionAll() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    Mappable<String> stmt1 = nativeSql.from(e).select(e.name);
    Mappable<String> stmt2 = nativeSql.from(d).select(d.name);
    Collectable<String> stmt3 = stmt1.unionAll(stmt2).map(row -> row.get(e.name));
    Sql<?> sql = stmt3.asSql();

    assertEquals(
        "select t0_.NAME from EMP t0_ union all select t0_.NAME from CATA.DEPT t0_",
        sql.getFormattedSql());
  }
}

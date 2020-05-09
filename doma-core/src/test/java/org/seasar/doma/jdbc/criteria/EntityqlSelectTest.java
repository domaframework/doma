package org.seasar.doma.jdbc.criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.seasar.doma.DomaException;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.criteria.entity.Dept_;
import org.seasar.doma.jdbc.criteria.entity.Emp;
import org.seasar.doma.jdbc.criteria.entity.Emp_;
import org.seasar.doma.jdbc.criteria.statement.Statement;
import org.seasar.doma.message.Message;

class EntityqlSelectTest {

  private final Entityql entityql = new Entityql(new MockConfig());

  @Test
  void from() {
    Emp_ e = new Emp_();
    Statement<List<Emp>> stmt = entityql.from(e);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void innerJoin() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();

    Statement<List<Emp>> stmt = entityql.from(e).innerJoin(d, on -> on.eq(e.id, d.id));

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ inner join CATA.DEPT t1_ on (t0_.ID = t1_.ID)",
        sql.getFormattedSql());
  }

  @Test
  void innerJoin_empty() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();

    Statement<List<Emp>> stmt = entityql.from(e).innerJoin(d, on -> {});

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void leftJoin() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();

    Statement<List<Emp>> stmt = entityql.from(e).leftJoin(d, on -> on.eq(e.id, d.id));

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ left outer join CATA.DEPT t1_ on (t0_.ID = t1_.ID)",
        sql.getFormattedSql());
  }

  @Test
  void leftJoin_empty() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();

    Statement<List<Emp>> stmt = entityql.from(e).leftJoin(d, on -> {});

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void associate() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();

    Statement<List<Emp>> stmt =
        entityql.from(e).innerJoin(d, on -> on.eq(e.id, d.id)).associate(e, d, (emp, dept) -> {});

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION, t1_.ID, t1_.NAME from EMP t0_ inner join CATA.DEPT t1_ on (t0_.ID = t1_.ID)",
        sql.getFormattedSql());
  }

  @Test
  void associate_mandatory() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();

    try {
      entityql.from(e).innerJoin(d, on -> {}).associate(e, d, (emp, dept) -> {});
      fail();
    } catch (DomaException ex) {
      assertEquals(Message.DOMA6001, ex.getMessageResource());
      System.out.println(ex.getMessage());
    }
  }

  @Test
  void associate_optional() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();

    Statement<List<Emp>> stmt =
        entityql
            .from(e)
            .innerJoin(d, on -> {})
            .associate(e, d, (emp, dept) -> {}, AssociationKind.OPTIONAL);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void associate_more() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    Emp_ e2 = new Emp_();
    Dept_ d2 = new Dept_();

    Statement<List<Emp>> stmt =
        entityql
            .from(e)
            .innerJoin(d, on -> on.eq(e.id, d.id))
            .leftJoin(e2, on -> on.eq(e.id, e2.id))
            .leftJoin(d2, on -> on.eq(d.id, d2.id))
            .associate(e, d, (emp, dept) -> {})
            .associate(e, e2, (emp, emp2) -> {})
            .associate(d, d2, (dept, dept2) -> {});

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select "
            + "t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION, "
            + "t1_.ID, t1_.NAME, "
            + "t2_.ID, t2_.NAME, t2_.SALARY, t2_.VERSION, "
            + "t3_.ID, t3_.NAME from EMP t0_ "
            + "inner join CATA.DEPT t1_ on (t0_.ID = t1_.ID) "
            + "left outer join EMP t2_ on (t0_.ID = t2_.ID) "
            + "left outer join CATA.DEPT t3_ on (t1_.ID = t3_.ID)",
        sql.getFormattedSql());
  }

  @Test
  void limit() {
    Emp_ e = new Emp_();
    Statement<List<Emp>> stmt = entityql.from(e).limit(5);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ limit 5",
        sql.getFormattedSql());
  }

  @Test
  void limit_null() {
    Emp_ e = new Emp_();
    Statement<List<Emp>> stmt = entityql.from(e).limit(null);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void offset() {
    Emp_ e = new Emp_();
    Statement<List<Emp>> stmt = entityql.from(e).offset(5);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ offset 5",
        sql.getFormattedSql());
  }

  @Test
  void offset_null() {
    Emp_ e = new Emp_();
    Statement<List<Emp>> stmt = entityql.from(e).offset(null);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void distinct() {
    Emp_ e = new Emp_();
    Statement<List<Emp>> stmt = entityql.from(e).distinct();

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select distinct t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_",
        sql.getFormattedSql());
  }

  @Test
  void distinct_enabled() {
    Emp_ e = new Emp_();
    Statement<List<Emp>> stmt = entityql.from(e).distinct(DistinctOption.ENABLED);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select distinct t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_",
        sql.getFormattedSql());
  }

  @Test
  void distinct_disabled() {
    Emp_ e = new Emp_();
    Statement<List<Emp>> stmt = entityql.from(e).distinct(DistinctOption.DISABLED);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_", sql.getFormattedSql());
  }
}

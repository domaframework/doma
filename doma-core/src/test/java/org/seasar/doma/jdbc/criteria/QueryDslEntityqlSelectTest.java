package org.seasar.doma.jdbc.criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.seasar.doma.DomaException;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.criteria.entity.Dept;
import org.seasar.doma.jdbc.criteria.entity.Dept_;
import org.seasar.doma.jdbc.criteria.entity.Emp;
import org.seasar.doma.jdbc.criteria.entity.Emp_;
import org.seasar.doma.jdbc.criteria.option.AssociationOption;
import org.seasar.doma.jdbc.criteria.option.DistinctOption;
import org.seasar.doma.jdbc.criteria.statement.Statement;
import org.seasar.doma.message.Message;

class QueryDslEntityqlSelectTest {

  private final QueryDsl dsl = new QueryDsl(new MockConfig());

  @Test
  void from() {
    Emp_ e = new Emp_();
    Statement<List<Emp>> stmt = dsl.from(e);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void with1() {
    Emp_ e = new Emp_();
    Statement<List<Emp>> stmt = dsl.with(e, dsl.from(e).select()).from(e);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "with EMP(ID, NAME, SALARY, VERSION) as (select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_) select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_",
        sql.getFormattedSql());
  }

  @Test
  void with2() {
    Emp_ e = new Emp_();
    Emp_ e2 = new Emp_("EMP2");
    Statement<List<Emp>> stmt =
        dsl.with(
                e, dsl.from(e).select(),
                e2, dsl.from(e).select())
            .from(e);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "with EMP(ID, NAME, SALARY, VERSION) as (select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_), EMP2(ID, NAME, SALARY, VERSION) as (select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_) select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_",
        sql.getFormattedSql());
  }

  @Test
  void innerJoin() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();

    Statement<List<Emp>> stmt = dsl.from(e).innerJoin(d, on -> on.eq(e.id, d.id));

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ inner join CATA.DEPT t1_ on (t0_.ID = t1_.ID)",
        sql.getFormattedSql());
  }

  @Test
  void innerJoin_empty() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();

    Statement<List<Emp>> stmt = dsl.from(e).innerJoin(d, on -> {});

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void leftJoin() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();

    Statement<List<Emp>> stmt = dsl.from(e).leftJoin(d, on -> on.eq(e.id, d.id));

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ left outer join CATA.DEPT t1_ on (t0_.ID = t1_.ID)",
        sql.getFormattedSql());
  }

  @Test
  void leftJoin_empty() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();

    Statement<List<Emp>> stmt = dsl.from(e).leftJoin(d, on -> {});

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void associate() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();

    Statement<List<Emp>> stmt =
        dsl.from(e).innerJoin(d, on -> on.eq(e.id, d.id)).associate(e, d, (emp, dept) -> {});

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
      dsl.from(e).innerJoin(d, on -> {}).associate(e, d, (emp, dept) -> {});
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
        dsl.from(e)
            .innerJoin(d, on -> {})
            .associate(e, d, (emp, dept) -> {}, AssociationOption.optional());

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
        dsl.from(e)
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
    Statement<List<Emp>> stmt = dsl.from(e).limit(5);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ offset 0 rows fetch first 5 rows only",
        sql.getFormattedSql());
  }

  @Test
  void limit_null() {
    Emp_ e = new Emp_();
    Statement<List<Emp>> stmt = dsl.from(e).limit(null);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void offset() {
    Emp_ e = new Emp_();
    Statement<List<Emp>> stmt = dsl.from(e).offset(5);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_ offset 5 rows",
        sql.getFormattedSql());
  }

  @Test
  void offset_null() {
    Emp_ e = new Emp_();
    Statement<List<Emp>> stmt = dsl.from(e).offset(null);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void distinct() {
    Emp_ e = new Emp_();
    Statement<List<Emp>> stmt = dsl.from(e).distinct();

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select distinct t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_",
        sql.getFormattedSql());
  }

  @Test
  void distinct_basic() {
    Emp_ e = new Emp_();
    Statement<List<Emp>> stmt = dsl.from(e).distinct(DistinctOption.basic());

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select distinct t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_",
        sql.getFormattedSql());
  }

  @Test
  void distinct_none() {
    Emp_ e = new Emp_();
    Statement<List<Emp>> stmt = dsl.from(e).distinct(DistinctOption.none());

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void select() {
    Emp_ e = new Emp_();
    Statement<List<Emp>> stmt = dsl.from(e).select(e);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void select_join() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    Statement<List<Dept>> stmt = dsl.from(e).innerJoin(d, on -> on.eq(e.id, d.id)).select(d);

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t1_.ID, t1_.NAME from EMP t0_ inner join CATA.DEPT t1_ on (t0_.ID = t1_.ID)",
        sql.getFormattedSql());
  }

  @Test
  void column_alias_is_not_found() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    DomaException ex = assertThrows(DomaException.class, () -> dsl.from(e).select(d).execute());
    assertEquals(Message.DOMA6004, ex.getMessageResource());
    System.out.println(ex.getMessage());
  }

  @Test
  void selectTo() {
    Emp_ e = new Emp_();
    Statement<List<Emp>> stmt = dsl.from(e).projectTo(e, e.name);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID, t0_.NAME from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void selectTo_no_propertyMetamodels() {
    Emp_ e = new Emp_();
    Statement<List<Emp>> stmt = dsl.from(e).projectTo(e);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.ID from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void selectTo_illegal_entityMetamodel() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();

    DomaException ex = assertThrows(DomaException.class, () -> dsl.from(e).projectTo(d, d.id));
    assertEquals(Message.DOMA6007, ex.getMessageResource());
    System.out.println(ex.getMessage());
  }

  @Test
  void selectTo_illegal_propertyMetamodel() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();

    DomaException ex = assertThrows(DomaException.class, () -> dsl.from(e).projectTo(e, d.id));
    assertEquals(Message.DOMA6008, ex.getMessageResource());
    System.out.println(ex.getMessage());
  }
}

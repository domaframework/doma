package org.seasar.doma.jdbc.criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.criteria.entity.Dept_;
import org.seasar.doma.jdbc.criteria.entity.Emp;
import org.seasar.doma.jdbc.criteria.entity.Emp_;
import org.seasar.doma.jdbc.criteria.statement.SelectStatement;

class EntityqlSelectTest {

  private final Config config = new MockConfig();

  @Test
  void from() {
    Emp_ e = new Emp_();
    SelectStatement<Emp> stmt = Entityql.from(e);

    Sql<?> sql = stmt.asSql(config);
    assertEquals(
        "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void associate() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();

    SelectStatement<Emp> stmt =
        Entityql.from(e).innerJoin(d, on -> on.eq(e.id, d.id)).associate(e, d, (emp, dept) -> {});

    Sql<?> sql = stmt.asSql(config);
    assertEquals(
        "select t0_.ID, t0_.NAME, t0_.SALARY, t0_.VERSION, t1_.ID, t1_.NAME from EMP t0_ inner join CATA.DEPT t1_ on (t0_.ID = t1_.ID)",
        sql.getFormattedSql());
  }

  @Test
  void associate_more() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    Emp_ e2 = new Emp_();
    Dept_ d2 = new Dept_();

    SelectStatement<Emp> stmt =
        Entityql.from(e)
            .innerJoin(d, on -> on.eq(e.id, d.id))
            .leftJoin(e2, on -> on.eq(e.id, e2.id))
            .leftJoin(d2, on -> on.eq(d.id, d2.id))
            .associate(e, d, (emp, dept) -> {})
            .associate(e, e2, (emp, emp2) -> {})
            .associate(d, d2, (dept, dept2) -> {});

    Sql<?> sql = stmt.asSql(config);
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
}

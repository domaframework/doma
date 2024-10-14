package org.seasar.doma.jdbc.criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.criteria.entity.Dept_;
import org.seasar.doma.jdbc.criteria.entity.Emp_;
import org.seasar.doma.jdbc.criteria.statement.Statement;
import org.seasar.doma.jdbc.dialect.MysqlDialect;

class QueryDslNativeSqlDeleteTest {

  private final QueryDsl dsl = new QueryDsl(new MockConfig());

  @Test
  void deleteFrom() {
    Emp_ e = new Emp_();
    Statement<Integer> stmt = dsl.delete(e).all();

    Sql<?> sql = stmt.asSql();
    assertEquals("delete from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void aliasInDeleteClause() {
    MockConfig config = new MockConfig();
    config.dialect = new MysqlDialect();
    QueryDsl mysqlDsl = new QueryDsl(config);

    Emp_ e = new Emp_();
    Statement<Integer> stmt = mysqlDsl.delete(e).all();

    Sql<?> sql = stmt.asSql();
    assertEquals("delete t0_ from EMP t0_", sql.getFormattedSql());
  }

  @Test
  void where() {
    Emp_ e = new Emp_();
    Statement<Integer> stmt =
        dsl.delete(e)
            .where(
                c -> {
                  c.eq(e.name, "a");
                  c.eq(e.version, 1);
                });

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "delete from EMP t0_ where t0_.NAME = 'a' and t0_.VERSION = 1", sql.getFormattedSql());
  }

  @Test
  void where_in() {
    Emp_ e = new Emp_();
    Dept_ d = new Dept_();
    Statement<Integer> stmt = dsl.delete(e).where(c -> c.in(e.id, c.from(d).select(d.id)));

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "delete from EMP t0_ where t0_.ID in (select t1_.ID from CATA.DEPT t1_)",
        sql.getFormattedSql());
  }
}

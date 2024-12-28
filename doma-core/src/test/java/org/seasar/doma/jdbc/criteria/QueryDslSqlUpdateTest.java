package org.seasar.doma.jdbc.criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.criteria.entity.Emp_;
import org.seasar.doma.jdbc.criteria.statement.Statement;
import org.seasar.doma.jdbc.dialect.MssqlDialect;

class QueryDslSqlUpdateTest {

  private final QueryDsl dsl = new QueryDsl(new MockConfig());

  @Test
  void set() {
    Emp_ e = new Emp_();
    Statement<Integer> stmt =
        dsl.update(e)
            .set(
                c -> {
                  c.value(e.name, "bbb");
                  c.value(e.salary, (BigDecimal) null);
                });

    Sql<?> sql = stmt.asSql();
    assertEquals("update EMP t0_ set NAME = 'bbb', SALARY = null", sql.getFormattedSql());
  }

  @Test
  void where() {
    Emp_ e = new Emp_();
    Statement<Integer> stmt =
        dsl.update(e)
            .set(
                c -> {
                  c.value(e.name, "bbb");
                  c.value(e.salary, new BigDecimal("1000"));
                })
            .where(c -> c.eq(e.id, 1));

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "update EMP t0_ set NAME = 'bbb', SALARY = 1000 where t0_.ID = 1", sql.getFormattedSql());
  }

  @Test
  void aliasInUpdateClause() {
    MockConfig config = new MockConfig();
    config.dialect = new MssqlDialect();
    QueryDsl mssqlDsl = new QueryDsl(config);

    Emp_ e = new Emp_();
    Statement<Integer> stmt =
        mssqlDsl
            .update(e)
            .set(
                c -> {
                  c.value(e.name, "bbb");
                  c.value(e.salary, new BigDecimal("1000"));
                })
            .where(c -> c.eq(e.id, 1));

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "update t0_ set NAME = 'bbb', SALARY = 1000 from EMP t0_ where t0_.ID = 1",
        sql.getFormattedSql());
  }
}

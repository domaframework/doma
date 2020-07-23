package org.seasar.doma.jdbc.criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.criteria.entity.Emp;
import org.seasar.doma.jdbc.criteria.entity.Emp_;
import org.seasar.doma.jdbc.criteria.statement.Buildable;

class EntityqlBatchDeleteTest {

  private final Entityql entityql = new Entityql(new MockConfig());

  @Test
  void test() {
    Emp emp = new Emp();
    emp.setId(1);
    emp.setName("aaa");
    emp.setSalary(new BigDecimal("1000"));
    emp.setVersion(1);

    Emp_ e = new Emp_();
    Buildable<?> stmt = entityql.delete(e, Collections.singletonList(emp));

    Sql<?> sql = stmt.asSql();
    assertEquals("delete from EMP where ID = 1 and VERSION = 1", sql.getFormattedSql());
  }

  @Test
  void ignoreVersion() {
    Emp emp = new Emp();
    emp.setId(1);
    emp.setName("aaa");
    emp.setSalary(new BigDecimal("1000"));
    emp.setVersion(1);

    Emp_ e = new Emp_();
    Buildable<?> stmt =
        entityql.delete(
            e, Collections.singletonList(emp), settings -> settings.setIgnoreVersion(true));

    Sql<?> sql = stmt.asSql();
    assertEquals("delete from EMP where ID = 1", sql.getFormattedSql());
  }

  @Test
  void empty() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = entityql.delete(e, Collections.emptyList());

    Sql<?> sql = stmt.asSql();
    assertEquals("This SQL is empty because target entities are empty.", sql.getFormattedSql());
  }
}

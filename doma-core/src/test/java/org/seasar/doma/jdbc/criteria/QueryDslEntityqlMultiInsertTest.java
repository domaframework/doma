package org.seasar.doma.jdbc.criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.criteria.entity.Emp;
import org.seasar.doma.jdbc.criteria.entity.Emp_;
import org.seasar.doma.jdbc.criteria.statement.Buildable;

class QueryDslEntityqlMultiInsertTest {

  private final QueryDsl dsl = new QueryDsl(new MockConfig());

  @Test
  void insertInto() {
    Emp emp = new Emp();
    emp.setId(1);
    emp.setName("aaa");
    emp.setSalary(new BigDecimal("1000"));
    emp.setVersion(1);

    Emp emp2 = new Emp();
    emp2.setId(2);
    emp2.setName("bbb");
    emp2.setSalary(new BigDecimal("2000"));
    emp2.setVersion(1);

    Emp_ e = new Emp_();
    Buildable<?> stmt = dsl.insert(e).multi(Arrays.asList(emp, emp2));

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "insert into EMP (ID, NAME, SALARY, VERSION) values (1, 'aaa', 1000, 1), (2, 'bbb', 2000, 1)",
        sql.getFormattedSql());
  }

  @Test
  void include() {
    Emp emp = new Emp();
    emp.setId(1);
    emp.setName("aaa");
    emp.setSalary(new BigDecimal("1000"));
    emp.setVersion(1);

    Emp emp2 = new Emp();
    emp2.setId(2);
    emp2.setName("bbb");
    emp2.setSalary(new BigDecimal("2000"));
    emp2.setVersion(1);

    Emp_ e = new Emp_();
    Buildable<?> stmt =
        dsl.insert(e, settings -> settings.include(e.salary)).multi(Arrays.asList(emp, emp2));

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "insert into EMP (ID, SALARY, VERSION) values (1, 1000, 1), (2, 2000, 1)",
        sql.getFormattedSql());
  }

  @Test
  void exclude() {
    Emp emp = new Emp();
    emp.setId(1);
    emp.setName("aaa");
    emp.setSalary(new BigDecimal("1000"));
    emp.setVersion(1);

    Emp emp2 = new Emp();
    emp2.setId(2);
    emp2.setName("bbb");
    emp2.setSalary(new BigDecimal("2000"));
    emp2.setVersion(1);

    Emp_ e = new Emp_();
    Buildable<?> stmt =
        dsl.insert(e, settings -> settings.exclude(e.salary)).multi(Arrays.asList(emp, emp2));

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "insert into EMP (ID, NAME, VERSION) values (1, 'aaa', 1), (2, 'bbb', 1)",
        sql.getFormattedSql());
  }

  @Test
  void empty() {
    Emp_ e = new Emp_();
    Buildable<?> stmt = dsl.insert(e).multi(Collections.emptyList());

    Sql<?> sql = stmt.asSql();
    assertEquals("This SQL is empty because target entities are empty.", sql.getFormattedSql());
  }
}

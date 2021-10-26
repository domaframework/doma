package org.seasar.doma.it.criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.add;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.concat;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.literal;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.max;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.select;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.it.Dbms;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.Run;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.criteria.NativeSql;
import org.seasar.doma.jdbc.criteria.expression.SelectExpression;
import org.seasar.doma.jdbc.criteria.statement.EmptyWhereClauseException;

@ExtendWith(IntegrationTestEnvironment.class)
public class NativeSqlUpdateTest {

  private final NativeSql nativeSql;

  public NativeSqlUpdateTest(Config config) {
    this.nativeSql = new NativeSql(config);
  }

  @Test
  void settings() {
    Employee_ e = new Employee_();

    int count =
        nativeSql
            .update(
                e,
                settings -> {
                  settings.setComment("update all");
                  settings.setQueryTimeout(1000);
                  settings.setSqlLogType(SqlLogType.RAW);
                  settings.setAllowEmptyWhere(true);
                  settings.setBatchSize(20);
                })
            .set(
                c -> {
                  c.value(e.employeeName, "aaa");
                })
            .execute();

    assertEquals(14, count);
  }

  @Test
  void where() {
    Employee_ e = new Employee_();

    int count =
        nativeSql
            .update(e)
            .set(c -> c.value(e.departmentId, 3))
            .where(
                c -> {
                  c.isNotNull(e.managerId);
                  c.ge(e.salary, new Salary("2000"));
                })
            .execute();

    assertEquals(5, count);
  }

  @Test
  void where_empty() {
    Employee_ e = new Employee_();

    EmptyWhereClauseException ex =
        assertThrows(
            EmptyWhereClauseException.class,
            () -> nativeSql.update(e).set(c -> c.value(e.departmentId, 3)).execute());
    System.out.println(ex.getMessage());
  }

  @Test
  void where_empty_allowEmptyWhere_enabled() {
    Employee_ e = new Employee_();

    int count =
        nativeSql
            .update(e, settings -> settings.setAllowEmptyWhere(true))
            .set(c -> c.value(e.departmentId, 3))
            .execute();

    assertEquals(14, count);
  }

  @Test
  void expression_add() {
    Employee_ e = new Employee_();

    int count =
        nativeSql
            .update(e)
            .set(c -> c.value(e.version, add(e.version, 10)))
            .where(c -> c.eq(e.employeeId, 1))
            .execute();

    assertEquals(1, count);

    Employee employee = nativeSql.from(e).where(c -> c.eq(e.employeeId, 1)).fetchOne();
    assertNotNull(employee);
    assertEquals(11, employee.getVersion());
  }

  @Test
  void expression_concat() {
    Employee_ e = new Employee_();

    int count =
        nativeSql
            .update(e)
            .set(c -> c.value(e.employeeName, concat("[", concat(e.employeeName, "]"))))
            .where(c -> c.eq(e.employeeId, 1))
            .execute();

    assertEquals(1, count);
  }

  @Test
  void expressions() {
    Employee_ e = new Employee_();

    int count =
        nativeSql
            .update(e)
            .set(
                c -> {
                  c.value(e.employeeName, concat("[", concat(e.employeeName, "]")));
                  c.value(e.version, add(e.version, literal(1)));
                })
            .where(c -> c.eq(e.employeeId, 1))
            .execute();

    assertEquals(1, count);
  }

  @Test
  @Run(unless = Dbms.MYSQL)
  void expression_select() {
    Employee_ e = new Employee_();
    Employee_ e2 = new Employee_();
    Department_ d = new Department_();

    SelectExpression<Salary> subSelect =
        select(
            c ->
                c.from(e2)
                    .innerJoin(d, on -> on.eq(e2.departmentId, d.departmentId))
                    .where(cc -> cc.eq(e.departmentId, d.departmentId))
                    .groupBy(d.departmentId)
                    .select(max(e2.salary)));

    int count =
        nativeSql
            .update(e)
            .set(c -> c.value(e.salary, subSelect))
            .where(c -> c.eq(e.employeeId, 1))
            .peek(System.out::println)
            .execute();

    assertEquals(1, count);

    Salary salary = nativeSql.from(e).where(c -> c.eq(e.employeeId, 1)).select(e.salary).fetchOne();
    assertEquals(0, new BigDecimal("3000").compareTo(salary.getValue()));
  }
}

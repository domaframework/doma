package org.seasar.doma.it.criteria;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.seasar.doma.it.criteria.CustomExpressions.addSalaryUserDefined;
import static org.seasar.doma.it.criteria.CustomExpressions.concatWithUserDefined;
import static org.seasar.doma.it.criteria.CustomExpressions.tpStringWithUserDefined;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.add;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.concat;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.count;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.countDistinct;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.div;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.literal;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.lower;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.min;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.mod;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.mul;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.select;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.sub;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.sum;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.DomaException;
import org.seasar.doma.it.Dbms;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.Run;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.criteria.NativeSql;
import org.seasar.doma.jdbc.criteria.expression.AliasExpression;
import org.seasar.doma.jdbc.criteria.expression.Expressions;
import org.seasar.doma.jdbc.criteria.expression.UserDefinedExpression;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.criteria.statement.EmptyWhereClauseException;
import org.seasar.doma.jdbc.criteria.statement.NativeSqlSelectStarting;
import org.seasar.doma.jdbc.criteria.statement.SetOperand;
import org.seasar.doma.jdbc.criteria.tuple.Row;
import org.seasar.doma.jdbc.criteria.tuple.Tuple2;
import org.seasar.doma.jdbc.criteria.tuple.Tuple3;
import org.seasar.doma.jdbc.criteria.tuple.Tuple9;
import org.seasar.doma.message.Message;

@ExtendWith(IntegrationTestEnvironment.class)
public class NativeSqlSelectTest {

  private final NativeSql nativeSql;

  public NativeSqlSelectTest(Config config) {
    this.nativeSql = new NativeSql(config);
  }

  @Test
  void settings() {
    Employee_ e = new Employee_();

    List<Employee> list =
        nativeSql
            .from(
                e,
                settings -> {
                  settings.setComment("all employees");
                  settings.setSqlLogType(SqlLogType.RAW);
                  settings.setQueryTimeout(1000);
                  settings.setAllowEmptyWhere(true);
                  settings.setFetchSize(100);
                  settings.setMaxRows(100);
                })
            .fetch();
    assertEquals(14, list.size());
  }

  @Test
  void fetch_allowEmptyWhere_disabled() {
    Employee_ e = new Employee_();

    assertThrows(
        EmptyWhereClauseException.class,
        () -> nativeSql.from(e, settings -> settings.setAllowEmptyWhere(false)).fetch());
  }

  @Test
  void fetchOne_null() {
    Employee_ e = new Employee_();

    Integer result =
        nativeSql.from(e).where(c -> c.eq(e.employeeId, 99)).select(sum(e.employeeNo)).fetchOne();

    assertNull(result);
  }

  @Test
  void fetchOptional_null() {
    Employee_ e = new Employee_();

    Optional<Integer> result =
        nativeSql
            .from(e)
            .where(c -> c.eq(e.employeeId, 99))
            .select(sum(e.employeeNo))
            .fetchOptional();

    assertFalse(result.isPresent());
  }

  @Test
  void from_subquery() {
    Department_ d = new Department_();
    Employee_ e = new Employee_();
    NameAndAmount_ t = new NameAndAmount_();

    SetOperand<Tuple2<String, Salary>> subquery =
        nativeSql
            .from(e)
            .innerJoin(d, c -> c.eq(e.departmentId, d.departmentId))
            .groupBy(d.departmentName)
            .select(d.departmentName, sum(e.salary));

    NativeSqlSelectStarting<NameAndAmount> query =
        nativeSql.from(t, subquery).orderBy(c -> c.asc(t.name));
    List<NameAndAmount> list = query.fetch();
    List<NameAndAmount> expected =
        Arrays.asList(
            new NameAndAmount("ACCOUNTING", new BigDecimal("8750.00")),
            new NameAndAmount("RESEARCH", new BigDecimal("10875.00")),
            new NameAndAmount("SALES", new BigDecimal("9400.00")));
    assertIterableEquals(expected, list);
  }

  @Test
  void from_subquery_alias() {
    Department_ d = new Department_();
    Employee_ e = new Employee_();
    NameAndAmount_ t = new NameAndAmount_();

    SetOperand<?> subquery =
        nativeSql
            .from(e)
            .innerJoin(d, c -> c.eq(e.departmentId, d.departmentId))
            .groupBy(d.departmentName)
            .select(
                Expressions.alias(Expressions.sum(e.salary), t.amount.getName()),
                Expressions.alias(d.departmentName, t.name.getName()));

    NativeSqlSelectStarting<NameAndAmount> query =
        nativeSql.from(t, subquery).orderBy(c -> c.asc(t.name));
    List<NameAndAmount> list = query.fetch();
    List<NameAndAmount> expected =
        Arrays.asList(
            new NameAndAmount("ACCOUNTING", new BigDecimal("8750.00")),
            new NameAndAmount("RESEARCH", new BigDecimal("10875.00")),
            new NameAndAmount("SALES", new BigDecimal("9400.00")));
    assertIterableEquals(expected, list);
  }

  @Test
  void from_subquery_doesnot_match_alias() {
    Department_ d = new Department_();
    Employee_ e = new Employee_();
    NameAndAmount_ t = new NameAndAmount_();

    SetOperand<?> subquery =
        nativeSql
            .from(e)
            .innerJoin(d, c -> c.eq(e.departmentId, d.departmentId))
            .groupBy(d.departmentName)
            .select(Expressions.alias(d.departmentName, t.name.getName()));

    NativeSqlSelectStarting<NameAndAmount> query =
        nativeSql.from(t, subquery).orderBy(c -> c.asc(t.name));
    DomaException ex = assertThrows(DomaException.class, () -> query.fetch());
    assertEquals(Message.DOMA6011, ex.getMessageResource());
    System.out.println(ex.getMessage());
  }

  @Test
  void from_subquery_union() {
    Department_ d = new Department_();
    NameAndAmount_ t = new NameAndAmount_();

    SetOperand<?> subquery =
        nativeSql
            .from(d)
            .where(c -> c.eq(d.departmentName, "ACCOUNTING"))
            .select(
                Expressions.alias(Expressions.literal(1200), t.amount.getName()),
                Expressions.alias(d.departmentName, t.name.getName()))
            .union(
                nativeSql
                    .from(d)
                    .where(c -> c.eq(d.departmentName, "OPERATIONS"))
                    .select(
                        Expressions.alias(Expressions.literal(900), t.amount.getName()),
                        Expressions.alias(d.departmentName, t.name.getName())));

    NativeSqlSelectStarting<NameAndAmount> query =
        nativeSql.from(t, subquery).orderBy(c -> c.asc(t.name));
    List<NameAndAmount> list = query.fetch();
    List<NameAndAmount> expected =
        Arrays.asList(
            new NameAndAmount("ACCOUNTING", new BigDecimal("1200.00")),
            new NameAndAmount("OPERATIONS", new BigDecimal("900")));
    assertIterableEquals(expected, list);
  }

  @Test
  void from_subquery_union_all() {
    Department_ d = new Department_();
    NameAndAmount_ t = new NameAndAmount_();

    SetOperand<?> subquery =
        nativeSql
            .from(d)
            .where(c -> c.eq(d.departmentName, "ACCOUNTING"))
            .select(
                Expressions.alias(Expressions.literal(1200), t.amount.getName()),
                Expressions.alias(d.departmentName, t.name.getName()))
            .unionAll(
                nativeSql
                    .from(d)
                    .where(c -> c.eq(d.departmentName, "OPERATIONS"))
                    .select(
                        Expressions.alias(Expressions.literal(900), t.amount.getName()),
                        Expressions.alias(d.departmentName, t.name.getName())));

    NativeSqlSelectStarting<NameAndAmount> query =
        nativeSql.from(t, subquery).orderBy(c -> c.asc(t.name));
    List<NameAndAmount> list = query.fetch();
    List<NameAndAmount> expected =
        Arrays.asList(
            new NameAndAmount("ACCOUNTING", new BigDecimal("1200.00")),
            new NameAndAmount("OPERATIONS", new BigDecimal("900")));
    assertIterableEquals(expected, list);
  }

  @Test
  void from_subquery_subquery() {
    Department_ d = new Department_();
    Employee_ e = new Employee_();
    NameAndAmount_ t = new NameAndAmount_();

    SetOperand<
            Tuple9<Integer, Integer, String, Integer, LocalDate, Salary, Integer, Integer, Integer>>
        subsubquery =
            nativeSql
                .from(e)
                .select(
                    e.employeeId,
                    e.employeeNo,
                    e.employeeName,
                    e.managerId,
                    e.hiredate,
                    e.salary,
                    e.departmentId,
                    e.addressId,
                    e.version);

    SetOperand<Tuple2<String, Salary>> subquery =
        nativeSql
            .from(e, subsubquery)
            .innerJoin(d, c -> c.eq(e.departmentId, d.departmentId))
            .groupBy(d.departmentName)
            .select(d.departmentName, sum(e.salary));

    NativeSqlSelectStarting<NameAndAmount> query =
        nativeSql.from(t, subquery).orderBy(c -> c.asc(t.name));
    List<NameAndAmount> list = query.fetch();
    List<NameAndAmount> expected =
        Arrays.asList(
            new NameAndAmount("ACCOUNTING", new BigDecimal("8750.00")),
            new NameAndAmount("RESEARCH", new BigDecimal("10875.00")),
            new NameAndAmount("SALES", new BigDecimal("9400.00")));
    assertIterableEquals(expected, list);
  }

  @Test
  void from() {
    Employee_ e = new Employee_();

    List<Employee> list = nativeSql.from(e).fetch();

    assertEquals(14, list.size());
  }

  @Test
  void mapStream() {
    Employee_ e = new Employee_();

    Map<Integer, List<Employee>> map =
        nativeSql
            .from(e)
            .mapStream(stream -> stream.collect(groupingBy(Employee::getDepartmentId)));

    assertEquals(3, map.size());
  }

  @Test
  void collect() {
    Employee_ e = new Employee_();

    Map<Integer, List<Employee>> map =
        nativeSql.from(e).collect(groupingBy(Employee::getDepartmentId));

    assertEquals(3, map.size());
  }

  @Test
  void test_select() {
    Employee_ e = new Employee_();

    List<Employee> list = nativeSql.from(e).select().fetch();

    assertEquals(14, list.size());
    Employee employee = list.get(0);
    assertEquals("SMITH", employee.getEmployeeName());
  }

  @Test
  void select_entity() {
    Employee_ e = new Employee_();

    List<Employee> list = nativeSql.from(e).select(e).fetch();

    assertEquals(14, list.size());
    Employee employee = list.get(0);
    assertEquals("SMITH", employee.getEmployeeName());
  }

  @Test
  void select_joined_entity() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();

    List<Department> list =
        nativeSql
            .from(e)
            .innerJoin(d, on -> on.eq(e.departmentId, d.departmentId))
            .select(d)
            .fetch();

    assertEquals(14, list.size());
  }

  @Test
  void select_entities_tuple2() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();

    List<Tuple2<Employee, Department>> list =
        nativeSql
            .from(e)
            .innerJoin(d, on -> on.eq(e.departmentId, d.departmentId))
            .orderBy(c -> c.asc(e.employeeId))
            .select(e, d)
            .fetch();

    assertEquals(14, list.size());
    Tuple2<Employee, Department> tuple2 = list.get(0);
    Employee employee = tuple2.getItem1();
    Department department = tuple2.getItem2();
    assertEquals("SMITH", employee.getEmployeeName());
    assertEquals("RESEARCH", department.getDepartmentName());
  }

  @Test
  void select_entities_tuple2_emptyEntity() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();

    List<Tuple2<Department, Employee>> list =
        nativeSql
            .from(d)
            .leftJoin(e, on -> on.eq(d.departmentId, e.departmentId))
            .where(c -> c.eq(d.departmentId, 4))
            .select(d, e)
            .fetch();

    assertEquals(1, list.size());
    Tuple2<Department, Employee> tuple2 = list.get(0);
    Department department = tuple2.getItem1();
    assertEquals("OPERATIONS", department.getDepartmentName());
    Employee employee = tuple2.getItem2();
    assertNull(employee);
  }

  @Test
  void select_property() {
    Employee_ e = new Employee_();

    List<String> list = nativeSql.from(e).select(e.employeeName).fetch();

    assertEquals(14, list.size());
    assertEquals("SMITH", list.get(0));
  }

  @Test
  void select_properties_tuple2() {
    Employee_ e = new Employee_();

    List<Tuple2<String, Integer>> list =
        nativeSql.from(e).select(e.employeeName, e.employeeNo).fetch();

    assertEquals(14, list.size());
    assertEquals("SMITH", list.get(0).getItem1());
  }

  @Test
  void selectTo() {
    Employee_ e = new Employee_();

    List<Employee> list = nativeSql.from(e).selectTo(e, e.employeeNo).fetch();

    assertEquals(14, list.size());
    list.stream().map(Employee::getEmployeeId).forEach(System.out::println);
    assertTrue(list.stream().map(Employee::getEmployeeId).allMatch(Objects::nonNull));
    assertTrue(list.stream().map(Employee::getEmployeeNo).allMatch(Objects::nonNull));
    assertTrue(list.stream().map(Employee::getEmployeeName).allMatch(Objects::isNull));
  }

  @Test
  void select_mapStream() {
    Employee_ e = new Employee_();

    long count = nativeSql.from(e).select(e.employeeName).mapStream(Stream::count);

    assertEquals(14, count);
  }

  @Test
  void select_collect() {
    Employee_ e = new Employee_();

    long count = nativeSql.from(e).select(e.employeeName).collect(counting());

    assertEquals(14, count);
  }

  @Test
  void select_row() {
    Employee_ e = new Employee_();

    List<Row> list =
        nativeSql
            .from(e)
            .orderBy(c -> c.asc(e.employeeId))
            .select(e.employeeId, new PropertyMetamodel<?>[] {e.employeeName})
            .fetch();

    assertEquals(14, list.size());
    Row row = list.get(0);
    assertEquals(2, row.size());
    assertTrue(row.containsKey(e.employeeId));
    assertEquals(1, row.get(e.employeeId));
    assertTrue(row.containsKey(e.employeeName));
    assertEquals("SMITH", row.get(e.employeeName));
    assertFalse(row.containsKey(e.hiredate));
    assertNotNull(row.keySet());
    assertNotNull(row.values());
  }

  @Test
  void selectAsRow() {
    Employee_ e = new Employee_();

    List<Row> list =
        nativeSql
            .from(e)
            .orderBy(c -> c.asc(e.employeeId))
            .selectAsRow(e.employeeId, e.employeeName)
            .fetch();

    assertEquals(14, list.size());
    Row row = list.get(0);
    assertEquals(2, row.size());
    assertTrue(row.containsKey(e.employeeId));
    assertEquals(1, row.get(e.employeeId));
    assertTrue(row.containsKey(e.employeeName));
    assertEquals("SMITH", row.get(e.employeeName));
    assertFalse(row.containsKey(e.hiredate));
    assertNotNull(row.keySet());
    assertNotNull(row.values());
  }

  @Test
  void where() {
    Employee_ e = new Employee_();

    List<Employee> list = nativeSql.from(e).where(c -> c.eq(e.departmentId, 2)).fetch();

    assertEquals(5, list.size());
  }

  @Test
  void aggregate() {
    Employee_ e = new Employee_();

    Salary salary = nativeSql.from(e).select(sum(e.salary)).fetchOne();

    assertEquals(0, salary.getValue().compareTo(new BigDecimal("29025")));
  }

  @Test
  void aggregate_countDistinct() {
    Employee_ e = new Employee_();

    Long count = nativeSql.from(e).select(countDistinct(e.departmentId)).fetchOne();
    assertEquals(3, count);
  }

  @Test
  void groupBy() {
    Employee_ e = new Employee_();

    List<Tuple2<Integer, Long>> list =
        nativeSql.from(e).groupBy(e.departmentId).select(e.departmentId, count()).fetch();

    assertEquals(3, list.size());
  }

  @Test
  void groupBy_auto_generation() {
    Employee_ e = new Employee_();

    List<Tuple2<Integer, Long>> list = nativeSql.from(e).select(e.departmentId, count()).fetch();

    assertEquals(3, list.size());
  }

  @Test
  void having() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();

    List<Tuple2<Long, String>> list =
        nativeSql
            .from(e)
            .innerJoin(d, on -> on.eq(e.departmentId, d.departmentId))
            .having(c -> c.gt(count(), 3L))
            .orderBy(c -> c.asc(count()))
            .select(count(), d.departmentName)
            .fetch();

    assertEquals(2, list.size());
    assertEquals(new Tuple2<>(5L, "RESEARCH"), list.get(0));
    assertEquals(new Tuple2<>(6L, "SALES"), list.get(1));
  }

  @Test
  void having_multi_conditions() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();

    List<Tuple2<Long, String>> list =
        nativeSql
            .from(e)
            .innerJoin(d, on -> on.eq(e.departmentId, d.departmentId))
            .groupBy(d.departmentName)
            .having(
                c -> {
                  c.gt(count(), 3L);
                  c.or(() -> c.le(min(e.salary), new Salary("2000")));
                })
            .orderBy(c -> c.asc(count()))
            .select(count(), d.departmentName)
            .fetch();

    assertEquals(3, list.size());
    assertEquals(new Tuple2<>(3L, "ACCOUNTING"), list.get(0));
    assertEquals(new Tuple2<>(5L, "RESEARCH"), list.get(1));
    assertEquals(new Tuple2<>(6L, "SALES"), list.get(2));
  }

  @Test
  void limit_offset() {
    Employee_ e = new Employee_();

    List<Employee> list =
        nativeSql.from(e).limit(5).offset(3).orderBy(c -> c.asc(e.employeeNo)).fetch();

    assertEquals(5, list.size());
  }

  @Test
  void forUpdate() {
    Employee_ e = new Employee_();

    List<Employee> list = nativeSql.from(e).where(c -> c.eq(e.employeeId, 1)).forUpdate().fetch();

    assertEquals(1, list.size());
  }

  @Test
  void union() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();

    List<Tuple2<Integer, String>> list =
        nativeSql
            .from(e)
            .select(e.employeeId, e.employeeName)
            .union(nativeSql.from(d).select(d.departmentId, d.departmentName))
            .fetch();

    assertEquals(18, list.size());
  }

  @Test
  void union_mapStream() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();

    long count =
        nativeSql
            .from(e)
            .select(e.employeeId, e.employeeName)
            .union(nativeSql.from(d).select(d.departmentId, d.departmentName))
            .mapStream(Stream::count);

    assertEquals(18, count);
  }

  @Test
  void unionAll_entity() {
    Department_ d = new Department_();

    List<Department> list = nativeSql.from(d).unionAll(nativeSql.from(d)).fetch();

    assertEquals(8, list.size());
  }

  @Test
  void union_orderBy() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();

    List<Tuple2<Integer, String>> list =
        nativeSql
            .from(e)
            .select(e.employeeId, e.employeeName)
            .union(nativeSql.from(d).select(d.departmentId, d.departmentName))
            .orderBy(c -> c.asc(2))
            .fetch();

    assertEquals(18, list.size());
  }

  @Test
  @Run(unless = Dbms.MYSQL)
  void union_multi_orderBy() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();

    List<Tuple2<Integer, String>> list =
        nativeSql
            .from(e)
            .select(e.employeeId, e.employeeName)
            .union(nativeSql.from(d).select(d.departmentId, d.departmentName))
            .unionAll(nativeSql.from(e).select(e.employeeId, e.employeeName))
            .orderBy(c -> c.asc(2))
            .fetch();

    assertEquals(32, list.size());
  }

  @Test
  void distinct() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();

    List<Department> list =
        nativeSql
            .from(d)
            .distinct()
            .leftJoin(e, on -> on.eq(d.departmentId, e.departmentId))
            .fetch();

    assertEquals(4, list.size());
  }

  @Test
  void alias_orderBy_asc() {
    Department_ d = new Department_();
    Employee_ e = new Employee_();

    AliasExpression<Salary> salarySum = Expressions.alias(sum(e.salary), "SALARY_SUM");

    SetOperand<Tuple2<Integer, Salary>> query =
        nativeSql
            .from(e)
            .innerJoin(d, c -> c.eq(e.departmentId, d.departmentId))
            .groupBy(d.departmentId)
            .orderBy(c -> c.asc(salarySum))
            .select(d.departmentId, salarySum);

    List<Tuple2<Integer, Salary>> list = query.fetch();
    // asc order
    assertEquals(0, list.get(0).component2().getValue().compareTo(new BigDecimal(8750)));
    assertEquals(0, list.get(1).component2().getValue().compareTo(new BigDecimal(9400)));
    assertEquals(0, list.get(2).component2().getValue().compareTo(new BigDecimal(10875)));
  }

  @Test
  void alias_orderBy_desc() {
    Department_ d = new Department_();
    Employee_ e = new Employee_();

    AliasExpression<Salary> salarySum = Expressions.alias(sum(e.salary), "SALARY_SUM");

    SetOperand<Tuple2<Integer, Salary>> query =
        nativeSql
            .from(e)
            .innerJoin(d, c -> c.eq(e.departmentId, d.departmentId))
            .groupBy(d.departmentId)
            .orderBy(c -> c.desc(salarySum))
            .select(d.departmentId, salarySum);

    List<Tuple2<Integer, Salary>> list = query.fetch();
    // desc order
    assertEquals(0, list.get(0).component2().getValue().compareTo(new BigDecimal(10875)));
    assertEquals(0, list.get(1).component2().getValue().compareTo(new BigDecimal(9400)));
    assertEquals(0, list.get(2).component2().getValue().compareTo(new BigDecimal(8750)));
  }

  @SuppressWarnings("unused")
  @Test
  void peek() {
    Department_ d = new Department_();

    List<String> locations =
        nativeSql
            .from(d)
            .peek(System.out::println)
            .where(c -> c.eq(d.departmentName, "SALES"))
            .peek(System.out::println)
            .orderBy(c -> c.asc(d.location))
            .peek(sql -> System.out.println(sql.getFormattedSql()))
            .select(d.location)
            .peek(sql -> System.out.println(sql.getFormattedSql()))
            .fetch();
  }

  @Test
  void expressions_add() {
    Employee_ e = new Employee_();

    List<Tuple3<Integer, Integer, Integer>> list =
        nativeSql
            .from(e)
            .select(add(e.version, 1), add(1, e.version), add(e.departmentId, e.version))
            .fetch();

    assertEquals(14, list.size());
  }

  @Test
  void expressions_sub() {
    Employee_ e = new Employee_();

    List<Tuple3<Integer, Integer, Integer>> list =
        nativeSql
            .from(e)
            .select(sub(e.version, 1), sub(1, e.version), sub(e.departmentId, e.version))
            .fetch();

    assertEquals(14, list.size());
  }

  @Test
  void expressions_mul() {
    Employee_ e = new Employee_();

    List<Tuple3<Integer, Integer, Integer>> list =
        nativeSql
            .from(e)
            .select(mul(e.version, 1), mul(1, e.version), mul(e.departmentId, e.version))
            .fetch();

    assertEquals(14, list.size());
  }

  @Test
  void expressions_div() {
    Employee_ e = new Employee_();

    List<Tuple3<Integer, Integer, Integer>> list =
        nativeSql
            .from(e)
            .select(div(e.version, 1), div(1, e.version), div(e.departmentId, e.version))
            .fetch();

    assertEquals(14, list.size());
  }

  @Test
  void expressions_mod() {
    Employee_ e = new Employee_();

    List<Tuple3<Integer, Integer, Integer>> list =
        nativeSql
            .from(e)
            .select(mod(e.version, 1), mod(1, e.version), mod(e.departmentId, e.version))
            .fetch();

    assertEquals(14, list.size());
  }

  @Test
  void expressions_concat() {
    Employee_ e = new Employee_();

    List<Tuple3<String, String, String>> list =
        nativeSql
            .from(e)
            .select(
                concat(e.employeeName, "a"),
                concat("b", e.employeeName),
                concat(e.employeeName, e.employeeName))
            .fetch();

    assertEquals(14, list.size());
  }

  @Test
  void expressions_when() {
    Employee_ e = new Employee_();

    List<String> list =
        nativeSql
            .from(e)
            .select(
                when(
                    c -> {
                      c.eq(e.employeeName, literal("SMITH"), lower(e.employeeName));
                      c.eq(e.employeeName, literal("KING"), lower(e.employeeName));
                    },
                    literal("_")))
            .fetch();

    assertEquals(14, list.size());
    assertEquals(1, list.stream().filter(it -> it.equals("smith")).count());
    assertEquals(1, list.stream().filter(it -> it.equals("king")).count());
  }

  @Test
  void expressions_when_empty() {
    Employee_ e = new Employee_();

    List<String> list = nativeSql.from(e).select(when(c -> {}, literal("_"))).fetch();

    assertEquals(14, list.size());
    assertEquals(14, list.stream().filter(it -> it.equals("_")).count());
  }

  @Test
  void expressions_when_binding_string() {
    Employee_ e = new Employee_();

    List<String> list =
        nativeSql
            .from(e)
            .select(
                when(
                    c -> {
                      c.eq(e.employeeName, "SMITH", lower(e.employeeName));
                      c.eq(e.employeeName, "KING", lower(e.employeeName));
                    },
                    literal("_")))
            .fetch();

    assertEquals(14, list.size());
    assertEquals(1, list.stream().filter(it -> it.equals("smith")).count());
    assertEquals(1, list.stream().filter(it -> it.equals("king")).count());
  }

  @Test
  void expressions_when_binding_domainType() {
    Employee_ e = new Employee_();

    List<String> list =
        nativeSql
            .from(e)
            .select(
                when(c -> c.eq(e.salary, new Salary("3000"), lower(e.employeeName)), literal("*")))
            .fetch();

    assertEquals(14, list.size());
    assertEquals(1, list.stream().filter(it -> it.equals("scott")).count());
    assertEquals(1, list.stream().filter(it -> it.equals("ford")).count());
    assertEquals(12, list.stream().filter(it -> it.equals("*")).count());
  }

  @Test
  void expressions_when_eq_null() {
    Employee_ e = new Employee_();

    List<String> list =
        nativeSql
            .from(e)
            .select(
                when(c -> c.eq(e.employeeName, (String) null, lower(e.employeeName)), literal("*")))
            .fetch();

    assertEquals(14, list.size());
    assertEquals(14, list.stream().filter(it -> it.equals("*")).count());
  }

  @Test
  void expressions_literal_localDate() {
    Employee_ e = new Employee_();

    LocalDate date = nativeSql.from(e).select(literal(LocalDate.of(2020, 5, 23))).fetchOne();

    assertEquals(LocalDate.of(2020, 5, 23), date);
  }

  @Test
  void expressions_select() {
    Employee_ e = new Employee_();
    Employee_ e2 = new Employee_();

    Long count =
        nativeSql.from(e).select(select(c -> c.from(e2).select(count(e2.employeeId)))).fetchOne();

    assertEquals(14L, count);
  }

  @Test
  @Run(unless = {Dbms.SQLSERVER, Dbms.ORACLE})
  void select_userDefinedExpression_Domain_where_groupBy_orderBy_select() {
    Employee_ e = new Employee_();

    UserDefinedExpression<Salary> addSalary = addSalaryUserDefined(e);

    List<Row> list =
        nativeSql
            .from(e)
            .where(c -> c.ge(addSalary, new Salary(new BigDecimal(3_050))))
            .groupBy(e.employeeId, addSalary)
            .orderBy(
                c -> {
                  c.asc(e.employeeId);
                  c.asc(addSalary);
                })
            .selectAsRow(e.employeeId, e.salary, addSalary)
            .fetch();

    assertEquals(4, list.size());
    for (Row row : list) {
      Salary salary = row.get(e.salary);
      assertTrue(
          salary.getValue().add(BigDecimal.valueOf(100)).compareTo(new BigDecimal(3_050)) >= 0);
      assertEquals(salary.getValue().add(BigDecimal.valueOf(100)), row.get(addSalary).getValue());
    }
  }

  @Test
  void select_userDefinedExpression_String_where_groupBy_orderBy_select() {
    Department_ d = new Department_();

    UserDefinedExpression<String> concatDepartmentIdAndEmployeeId =
        concatWithUserDefined(tpStringWithUserDefined(d.departmentId), d.departmentName);

    List<Row> list =
        nativeSql
            .from(d)
            .where(c -> c.eq(concatDepartmentIdAndEmployeeId, "2-RESEARCH"))
            .groupBy(d.departmentId, concatDepartmentIdAndEmployeeId)
            .orderBy(
                c -> {
                  c.asc(d.departmentId);
                  c.asc(concatDepartmentIdAndEmployeeId);
                })
            .selectAsRow(d.departmentId, concatDepartmentIdAndEmployeeId)
            .fetch();

    assertEquals(1, list.size());
    assertEquals("2-RESEARCH", list.get(0).get(concatDepartmentIdAndEmployeeId));
  }

  @Test
  void select_optional_property() {
    Person_ p = new Person_();
    Optional<Integer> result =
        nativeSql.from(p).where(c -> c.eq(p.employeeId, 2)).select(p.managerId).fetchOptional();
    assertTrue(result.isPresent());
    assertEquals(6, result.get());
  }

  @Test
  void select_optional_property_with_aggregate_function() {
    Person_ p = new Person_();
    Optional<Integer> result =
        nativeSql.from(p).select(Expressions.max(p.managerId)).fetchOptional();
    assertTrue(result.isPresent());
    assertEquals(13, result.get());
  }
}

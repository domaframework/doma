package org.seasar.doma.it.criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.seasar.doma.jdbc.criteria.expression.Expressions.literal;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.DomaException;
import org.seasar.doma.it.Dbms;
import org.seasar.doma.it.IntegrationTestEnvironment;
import org.seasar.doma.it.Run;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Result;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.seasar.doma.jdbc.criteria.NativeSql;
import org.seasar.doma.jdbc.criteria.expression.Expressions;
import org.seasar.doma.jdbc.criteria.option.AssociationOption;
import org.seasar.doma.jdbc.criteria.statement.EmptyWhereClauseException;
import org.seasar.doma.jdbc.criteria.statement.EntityqlSelectStarting;
import org.seasar.doma.jdbc.criteria.statement.Listable;
import org.seasar.doma.jdbc.criteria.statement.SetOperand;
import org.seasar.doma.jdbc.criteria.tuple.Tuple2;
import org.seasar.doma.jdbc.criteria.tuple.Tuple3;
import org.seasar.doma.jdbc.criteria.tuple.Tuple9;
import org.seasar.doma.message.Message;

@ExtendWith(IntegrationTestEnvironment.class)
public class EntityqlSelectTest {

  private final Entityql entityql;
  private final NativeSql nativeSql;

  public EntityqlSelectTest(Config config) {
    this.entityql = new Entityql(config);
    this.nativeSql = new NativeSql(config);
  }

  @Test
  void settings() {
    Employee_ e = new Employee_();

    List<Employee> list =
        entityql
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
  void allowEmptyWhere_disabled() {
    Employee_ e = new Employee_();

    assertThrows(
        EmptyWhereClauseException.class,
        () -> entityql.from(e, settings -> settings.setAllowEmptyWhere(false)).fetch());
  }

  @Test
  void fetch() {
    Employee_ e = new Employee_();

    List<Employee> list = entityql.from(e).fetch();
    assertEquals(14, list.size());
  }

  @Test
  void fetchOptional() {
    Employee_ e = new Employee_();

    Optional<Employee> employee =
        entityql.from(e).where(c -> c.eq(e.employeeId, 1)).fetchOptional();
    assertTrue(employee.isPresent());
  }

  @Test
  void fetchOptional_notPresent() {
    Employee_ e = new Employee_();

    Optional<Employee> employee =
        entityql.from(e).where(c -> c.eq(e.employeeId, 100)).fetchOptional();
    assertFalse(employee.isPresent());
  }

  @Test
  void fetchOne() {
    Employee_ e = new Employee_();

    Employee employee = entityql.from(e).where(c -> c.eq(e.employeeId, 1)).fetchOne();
    assertNotNull(employee);
  }

  @Test
  void fetchOne_null() {
    Employee_ e = new Employee_();

    Employee employee = entityql.from(e).where(c -> c.eq(e.employeeId, 100)).fetchOne();
    assertNull(employee);
  }

  @Test
  void from_subquery() {
    Department_ d = new Department_();
    Employee_ e = new Employee_();
    NameAndAmount_ t = new NameAndAmount_();

    SetOperand<?> subquery =
        nativeSql
            .from(e)
            .innerJoin(d, c -> c.eq(e.departmentId, d.departmentId))
            .groupBy(d.departmentName)
            .select(d.departmentName, Expressions.sum(e.salary));

    EntityqlSelectStarting<NameAndAmount> query =
        entityql.from(t, subquery).orderBy(c -> c.asc(t.name));
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

    EntityqlSelectStarting<NameAndAmount> query =
        entityql.from(t, subquery).orderBy(c -> c.asc(t.name));
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
    EntityqlSelectStarting<NameAndAmount> query =
        entityql.from(t, subquery).orderBy(c -> c.asc(t.name));
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

    EntityqlSelectStarting<NameAndAmount> query =
        entityql.from(t, subquery).orderBy(c -> c.asc(t.name));
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

    EntityqlSelectStarting<NameAndAmount> query =
        entityql.from(t, subquery).orderBy(c -> c.asc(t.name));
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
            .select(d.departmentName, Expressions.sum(e.salary));

    EntityqlSelectStarting<NameAndAmount> query =
        entityql.from(t, subquery).orderBy(c -> c.asc(t.name));
    List<NameAndAmount> list = query.fetch();
    List<NameAndAmount> expected =
        Arrays.asList(
            new NameAndAmount("ACCOUNTING", new BigDecimal("8750.00")),
            new NameAndAmount("RESEARCH", new BigDecimal("10875.00")),
            new NameAndAmount("SALES", new BigDecimal("9400.00")));
    assertIterableEquals(expected, list);
  }

  @Test
  void where() {
    Employee_ e = new Employee_();

    List<Employee> list =
        entityql
            .from(e)
            .where(
                c -> {
                  c.eq(e.departmentId, 2);
                  c.isNotNull(e.managerId);
                  c.or(
                      () -> {
                        c.gt(e.salary, new Salary("1000"));
                        c.lt(e.salary, new Salary("2000"));
                      });
                })
            .fetch();

    assertEquals(10, list.size());
  }

  @Test
  void where_dynamic() {
    List<Employee> list = where_dynamic("C%", false);
    assertEquals(3, list.size());

    List<Employee> list2 = where_dynamic("C%", true);
    assertEquals(1, list2.size());
  }

  @SuppressWarnings("UnnecessaryLocalVariable")
  private List<Employee> where_dynamic(String name, boolean enableNameCondition) {
    Employee_ e = new Employee_();
    List<Employee> list =
        entityql
            .from(e)
            .where(
                c -> {
                  c.eq(e.departmentId, 1);
                  if (enableNameCondition) {
                    c.like(e.employeeName, name);
                  }
                })
            .fetch();
    return list;
  }

  @Test
  void where_in() {
    Employee_ e = new Employee_();

    List<Employee> list =
        entityql
            .from(e)
            .where(c -> c.in(e.employeeId, Arrays.asList(2, 3, 4)))
            .orderBy(c -> c.asc(e.employeeId))
            .fetch();

    assertEquals(3, list.size());
  }

  @Test
  void where_in_subQuery() {
    Employee_ e = new Employee_();
    Employee_ e2 = new Employee_();

    List<Employee> list =
        entityql
            .from(e)
            .where(c -> c.in(e.employeeId, c.from(e2).select(e2.managerId)))
            .orderBy(c -> c.asc(e.employeeId))
            .fetch();

    assertEquals(6, list.size());
  }

  @Test
  @Run(onlyIf = {Dbms.H2, Dbms.MYSQL, Dbms.POSTGRESQL})
  void where_in3() {
    Employee_ e = new Employee_();

    List<Employee> list =
        entityql
            .from(e)
            .where(
                c ->
                    c.in(
                        new Tuple3<>(e.employeeId, e.employeeNo, e.employeeName),
                        Arrays.asList(
                            new Tuple3<>(2, 7499, "ALLEN"),
                            new Tuple3<>(3, 7521, "WARD"),
                            new Tuple3<>(4, 7566, "JONES"))))
            .orderBy(c -> c.asc(e.employeeId))
            .fetch();

    assertEquals(3, list.size());
  }

  @Test
  @Run(onlyIf = {Dbms.H2, Dbms.MYSQL, Dbms.POSTGRESQL})
  void where_in3_subQuery() {
    Employee_ e = new Employee_();
    Employee_ e2 = new Employee_();

    List<Employee> list =
        entityql
            .from(e)
            .where(
                c ->
                    c.in(
                        new Tuple3<>(e.employeeId, e.employeeNo, e.employeeName),
                        c.from(e2)
                            .where(c2 -> c2.in(e2.employeeId, Arrays.asList(2, 3, 4)))
                            .select(e2.employeeId, e2.employeeNo, e2.employeeName)))
            .orderBy(c -> c.asc(e.employeeId))
            .fetch();

    assertEquals(3, list.size());
  }

  @Test
  void where_exists_subQuery() {
    Employee_ e = new Employee_();
    Employee_ e2 = new Employee_();

    List<Employee> list =
        entityql
            .from(e)
            .where(
                c ->
                    c.exists(
                        c.from(e2)
                            .where(c2 -> c2.eq(e.employeeId, e2.managerId))
                            .select(e2.managerId)))
            .orderBy(c -> c.asc(e.employeeId))
            .fetch();

    assertEquals(6, list.size());
  }

  @Test
  void where_like() {
    Address_ a = new Address_();

    List<Address> list = entityql.from(a).where(c -> c.like(a.street, "%1")).fetch();
    assertEquals(2, list.size());
  }

  @Test
  void where_like_null() {
    Address_ a = new Address_();

    List<Address> list = entityql.from(a).where(c -> c.like(a.street, null)).fetch();
    assertEquals(15, list.size());
  }

  @Test
  void innerJoin() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();

    List<Employee> list =
        entityql.from(e).innerJoin(d, on -> on.eq(e.departmentId, d.departmentId)).fetch();

    assertEquals(14, list.size());
  }

  @Test
  void innerJoin_dynamic() {
    List<Employee> list = innerJoin_dynamic(true);
    assertEquals(13, list.size());

    List<Employee> list2 = innerJoin_dynamic(false);
    assertEquals(14, list2.size());
  }

  @SuppressWarnings("UnnecessaryLocalVariable")
  private List<Employee> innerJoin_dynamic(boolean join) {
    Employee_ e = new Employee_();
    Employee_ e2 = new Employee_();

    List<Employee> list =
        entityql
            .from(e)
            .innerJoin(
                e2,
                on -> {
                  if (join) {
                    on.eq(e.managerId, e2.employeeId);
                  }
                })
            .fetch();
    return list;
  }

  @Test
  void leftJoin() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();

    List<Employee> list =
        entityql.from(e).leftJoin(d, on -> on.eq(e.departmentId, d.departmentId)).fetch();

    assertEquals(14, list.size());
  }

  @Test
  void associate() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();

    List<Employee> list =
        entityql
            .from(e)
            .innerJoin(d, on -> on.eq(e.departmentId, d.departmentId))
            .where(c -> c.eq(d.departmentName, "SALES"))
            .associate(
                e,
                d,
                (employee, department) -> {
                  employee.setDepartment(department);
                  department.getEmployeeList().add(employee);
                })
            .fetch();

    assertEquals(6, list.size());
    assertTrue(
        list.stream().allMatch(it -> it.getDepartment().getDepartmentName().equals("SALES")));
    assertEquals(list.get(0).getDepartment().getEmployeeList().size(), 6);
  }

  @Test
  void associate_dynamic() {
    List<Employee> list = associate_dynamic(true);
    assertEquals(14, list.size());
    assertTrue(list.stream().allMatch(emp -> emp.getDepartment() != null));
    List<Employee> list2 = associate_dynamic(false);
    assertEquals(14, list2.size());
    assertTrue(list2.stream().allMatch(emp -> emp.getDepartment() == null));
  }

  @SuppressWarnings("UnnecessaryLocalVariable")
  private List<Employee> associate_dynamic(boolean join) {
    Employee_ e = new Employee_();
    Department_ d = new Department_();

    List<Employee> list =
        entityql
            .from(e)
            .innerJoin(
                d,
                on -> {
                  if (join) {
                    on.eq(e.departmentId, d.departmentId);
                  }
                })
            .associate(
                e,
                d,
                (employee, department) -> {
                  employee.setDepartment(department);
                  department.getEmployeeList().add(employee);
                },
                AssociationOption.optional())
            .fetch();

    return list;
  }

  @Test
  void associate_multi() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();
    Address_ a = new Address_();

    List<Employee> list =
        entityql
            .from(e)
            .innerJoin(d, on -> on.eq(e.departmentId, d.departmentId))
            .innerJoin(a, on -> on.eq(e.addressId, a.addressId))
            .where(c -> c.eq(d.departmentName, "SALES"))
            .associate(
                e,
                d,
                (employee, department) -> {
                  employee.setDepartment(department);
                  department.getEmployeeList().add(employee);
                })
            .associate(e, a, Employee::setAddress)
            .fetch();

    assertEquals(6, list.size());
    assertTrue(
        list.stream().allMatch(it -> it.getDepartment().getDepartmentName().equals("SALES")));
    assertEquals(list.get(0).getDepartment().getEmployeeList().size(), 6);
    assertTrue(list.stream().allMatch(it -> it.getAddress() != null));
  }

  @Test
  void associate_multi_oneToMany() {
    Team_ t = new Team_();
    Player_ p = new Player_();
    Coach_ c = new Coach_();

    entityql.insert(t, new Team(1, "Tokyo")).execute();
    entityql.insert(t, new Team(2, "Osaka")).execute();
    entityql.insert(p, new Player(1, "Player 1", 1)).execute();
    entityql.insert(p, new Player(2, "Player 2", 1)).execute();
    entityql.insert(p, new Player(3, "Player 3", 1)).execute();
    entityql.insert(p, new Player(4, "Player 4", 2)).execute();
    entityql.insert(p, new Player(5, "Player 5", 2)).execute();
    entityql.insert(c, new Coach(1, "Coach 1", 1)).execute();
    entityql.insert(c, new Coach(2, "Coach 2", 1)).execute();
    entityql.insert(c, new Coach(3, "Coach 3", 2)).execute();

    List<Team> teams =
        entityql
            .from(t)
            .leftJoin(p, on -> on.eq(t.id, p.teamId))
            .leftJoin(c, on -> on.eq(t.id, c.teamId))
            .where(w -> w.eq(t.id, 1))
            .associate(t, p, (team, player) -> team.getPlayers().add(player))
            .associate(t, c, (team, coach) -> team.getCoaches().add(coach))
            .execute();

    assertEquals(1, teams.size());
    Team team = teams.iterator().next();
    assertEquals("Tokyo", team.getName());
    assertEquals(3, team.getPlayers().size());
    List<Integer> playerIds =
        team.getPlayers().stream().map(Player::getId).collect(Collectors.toList());
    assertTrue(playerIds.contains(1));
    assertTrue(playerIds.contains(2));
    assertTrue(playerIds.contains(3));
    assertEquals(2, team.getCoaches().size());
    List<Integer> coachIds =
        team.getCoaches().stream().map(Coach::getId).collect(Collectors.toList());
    assertTrue(coachIds.contains(1));
    assertTrue(coachIds.contains(2));
  }

  @Test
  void orderBy() {
    Employee_ e = new Employee_();

    List<Employee> list =
        entityql
            .from(e)
            .orderBy(
                c -> {
                  c.asc(e.departmentId);
                  c.desc(e.salary);
                })
            .fetch();

    assertEquals(14, list.size());
  }

  @Test
  void asSql() {
    Department_ d = new Department_();

    Listable<Department> stmt = entityql.from(d).where(c -> c.eq(d.departmentName, "SALES"));

    Sql<?> sql = stmt.asSql();
    System.out.printf("Raw SQL      : %s\n", sql.getRawSql());
    System.out.printf("Formatted SQL: %s\n", sql.getFormattedSql());
  }

  @Test
  void peek() {
    Department_ d = new Department_();

    entityql
        .from(d)
        .peek(System.out::println)
        .where(c -> c.eq(d.departmentName, "SALES"))
        .peek(System.out::println)
        .orderBy(c -> c.asc(d.location))
        .peek(sql -> System.out.println(sql.getFormattedSql()))
        .fetch();
  }

  @Test
  void tableName_replacement() {
    Employee_ e = new Employee_();
    Department_ d = new Department_("DEPARTMENT_ARCHIVE");

    Department department = new Department();
    department.setDepartmentId(1);
    department.setDepartmentNo(1);
    department.setDepartmentName("aaa");
    department.setLocation("bbb");

    Result<Department> result = entityql.insert(d, department).execute();
    assertEquals(1, result.getCount());

    List<Department> list =
        entityql
            .from(d)
            .innerJoin(e, on -> on.eq(d.departmentId, e.departmentId))
            .associate(d, e, (dept, employee) -> dept.getEmployeeList().add(employee))
            .fetch();

    assertEquals(1, list.size());
    assertEquals(3, list.get(0).getEmployeeList().size());
  }

  @Test
  void select() {
    Employee_ e = new Employee_();

    List<Employee> list = entityql.from(e).select(e).fetch();
    assertEquals(14, list.size());
  }

  @Test
  void select_join() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();

    List<Department> list =
        entityql
            .from(e)
            .innerJoin(d, on -> on.eq(e.departmentId, d.departmentId))
            .select(d)
            .fetch();
    assertEquals(3, list.size());
  }

  @Test
  void selectTo() {
    Employee_ e = new Employee_();

    List<Employee> list = entityql.from(e).selectTo(e, e.employeeName).fetch();
    assertEquals(14, list.size());
    assertTrue(list.stream().map(Employee::getEmployeeId).allMatch(Objects::nonNull));
    assertTrue(list.stream().map(Employee::getEmployeeName).allMatch(Objects::nonNull));
  }

  @Test
  void selectTo_associate() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();

    List<Employee> list =
        entityql
            .from(e)
            .innerJoin(d, on -> on.eq(e.departmentId, d.departmentId))
            .associate(e, d, Employee::setDepartment)
            .selectTo(e, e.employeeName)
            .fetch();
    assertEquals(14, list.size());
    assertTrue(list.stream().map(Employee::getEmployeeId).allMatch(Objects::nonNull));
    assertTrue(list.stream().map(Employee::getEmployeeName).allMatch(Objects::nonNull));
    assertTrue(list.stream().map(Employee::getDepartment).allMatch(Objects::nonNull));
  }

  @Test
  void expressions_literal_int() {
    Employee_ e = new Employee_();

    Employee employee = entityql.from(e).where(c -> c.eq(e.employeeId, literal(1))).fetchOne();

    assertNotNull(employee);
  }

  @Test
  void iterableDomain() {
    Division_ d = new Division_();

    Division division = entityql.from(d).where(c -> c.eq(d.departmentId, 1)).fetchOne();
    division.location = new Names("TOKYO, KYOTO");
    entityql.update(d, division).execute();
    Division division2 = entityql.from(d).where(c -> c.eq(d.departmentId, 1)).fetchOne();
    Iterator<String> iterator = division2.location.iterator();

    assertEquals("TOKYO", iterator.next());
    assertEquals("KYOTO", iterator.next());
  }
}

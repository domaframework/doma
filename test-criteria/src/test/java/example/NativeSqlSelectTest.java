package example;

import static java.util.stream.Collectors.counting;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.seasar.doma.jdbc.criteria.AggregateFunctions.count;
import static org.seasar.doma.jdbc.criteria.AggregateFunctions.min;
import static org.seasar.doma.jdbc.criteria.AggregateFunctions.sum;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.criteria.NativeSql;
import org.seasar.doma.jdbc.criteria.def.PropertyDef;
import org.seasar.doma.jdbc.criteria.statement.Collectable;
import org.seasar.doma.jdbc.criteria.statement.Listable;
import org.seasar.doma.jdbc.criteria.statement.SetOperand;
import org.seasar.doma.jdbc.criteria.tuple.Tuple2;

@ExtendWith(Env.class)
public class NativeSqlSelectTest {

  private final NativeSql nativeSql;

  public NativeSqlSelectTest(Config config) {
    this.nativeSql = new NativeSql(config);
  }

  @Test
  void from() {
    Employee_ e = new Employee_();

    Listable<Employee> stmt = nativeSql.from(e);

    List<Employee> list = stmt.fetch();

    assertEquals(14, list.size());
  }

  @Test
  void stream() {
    Employee_ e = new Employee_();

    Collectable<Employee> stmt = nativeSql.from(e);

    long count = stmt.mapStream(Stream::count);

    assertEquals(14, count);
  }

  @Test
  void collect() {
    Employee_ e = new Employee_();

    Collectable<Employee> stmt = nativeSql.from(e);

    long count = stmt.collect(counting());

    assertEquals(14, count);
  }

  @Test
  void select() {
    Employee_ e = new Employee_();

    Collectable<String> stmt = nativeSql.from(e).select(e.employeeName);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.EMPLOYEE_NAME from EMPLOYEE t0_", sql.getFormattedSql());
    List<String> list = stmt.fetch();
    assertEquals(14, list.size());
    assertEquals("SMITH", list.get(0));
  }

  @Test
  void select_mapStream() {
    Employee_ e = new Employee_();

    Collectable<String> stmt = nativeSql.from(e).select(e.employeeName);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.EMPLOYEE_NAME from EMPLOYEE t0_", sql.getFormattedSql());

    long count = stmt.mapStream(Stream::count);
    assertEquals(14, count);
  }

  @Test
  void select_collect() {
    Employee_ e = new Employee_();

    Collectable<String> stmt = nativeSql.from(e).select(e.employeeName);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.EMPLOYEE_NAME from EMPLOYEE t0_", sql.getFormattedSql());
    long count = stmt.collect(counting());
    assertEquals(14, count);
  }

  @Test
  void selectAsMap() {
    Employee_ e = new Employee_();

    Collectable<Map<PropertyDef<?>, Object>> stmt =
        nativeSql
            .from(e)
            .orderBy(c -> c.asc(e.employeeId))
            .select(new PropertyDef<?>[] {e.employeeId, e.employeeName});

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.EMPLOYEE_ID, t0_.EMPLOYEE_NAME from EMPLOYEE t0_ order by t0_.EMPLOYEE_ID asc",
        sql.getFormattedSql());
    List<Map<PropertyDef<?>, Object>> list = stmt.fetch();
    assertEquals(14, list.size());
    Map<PropertyDef<?>, Object> row = list.get(0);
    assertEquals(2, row.size());
    assertEquals(1, row.get(e.employeeId));
    assertEquals("SMITH", row.get(e.employeeName));
  }

  @Test
  void selectAsMap_empty() {
    Employee_ e = new Employee_();

    Collectable<Map<PropertyDef<?>, Object>> stmt =
        nativeSql.from(e).orderBy(c -> c.asc(e.employeeId)).select();

    Sql<?> sql = stmt.asSql();
    assertEquals(
        "select t0_.EMPLOYEE_ID, t0_.EMPLOYEE_NO, t0_.EMPLOYEE_NAME, t0_.MANAGER_ID, t0_.HIREDATE, t0_.SALARY, t0_.DEPARTMENT_ID, t0_.ADDRESS_ID, t0_.VERSION from EMPLOYEE t0_ order by t0_.EMPLOYEE_ID asc",
        sql.getFormattedSql());
    List<Map<PropertyDef<?>, Object>> list = stmt.fetch();
    assertEquals(14, list.size());
    Map<PropertyDef<?>, Object> row = list.get(0);
    assertEquals(9, row.size());
    assertEquals(1, row.get(e.employeeId));
    assertEquals("SMITH", row.get(e.employeeName));
  }

  @Test
  void where() {
    Employee_ e = new Employee_();

    Listable<Employee> stmt = nativeSql.from(e).where(c -> c.eq(e.departmentId, 2));

    List<Employee> list = stmt.fetch();

    assertEquals(5, list.size());
  }

  @Test
  void aggregate() {
    Employee_ e = new Employee_();

    Listable<Salary> stmt = nativeSql.from(e).select(sum(e.salary));

    List<Salary> list = stmt.fetch();

    assertEquals(1, list.size());
    assertEquals(0, list.get(0).getValue().compareTo(new BigDecimal("29025")));
  }

  @Test
  void groupBy() {
    Employee_ e = new Employee_();

    Listable<Tuple2<Integer, Long>> stmt =
        nativeSql.from(e).groupBy(e.departmentId).select(e.departmentId, count());

    List<Tuple2<Integer, Long>> list = stmt.fetch();

    assertEquals(3, list.size());
  }

  @Test
  void groupBy_auto_generation() {
    Employee_ e = new Employee_();

    Listable<Tuple2<Integer, Long>> stmt = nativeSql.from(e).select(e.departmentId, count());

    List<Tuple2<Integer, Long>> list = stmt.fetch();

    assertEquals(3, list.size());
  }

  @Test
  void having() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();

    Listable<Tuple2<Long, String>> stmt =
        nativeSql
            .from(e)
            .innerJoin(d, on -> on.eq(e.departmentId, d.departmentId))
            .groupBy(d.departmentName)
            .having(c -> c.gt(count(), 3L))
            .orderBy(c -> c.asc(count()))
            .select(count(), d.departmentName);

    List<Tuple2<Long, String>> list = stmt.fetch();

    assertEquals(2, list.size());
    assertEquals(new Tuple2<>(5L, "RESEARCH"), list.get(0));
    assertEquals(new Tuple2<>(6L, "SALES"), list.get(1));
  }

  @Test
  void having_multi_conditions() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();

    Listable<Tuple2<Long, String>> stmt =
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
            .select(count(), d.departmentName);

    List<Tuple2<Long, String>> list = stmt.fetch();

    assertEquals(3, list.size());
    assertEquals(new Tuple2<>(3L, "ACCOUNTING"), list.get(0));
    assertEquals(new Tuple2<>(5L, "RESEARCH"), list.get(1));
    assertEquals(new Tuple2<>(6L, "SALES"), list.get(2));
  }

  @Test
  void limit_offset() {
    Employee_ e = new Employee_();

    Listable<Employee> stmt =
        nativeSql.from(e).limit(5).offset(3).orderBy(c -> c.asc(e.employeeNo));

    List<Employee> list = stmt.fetch();

    assertEquals(5, list.size());
  }

  @Test
  void forUpdate() {
    Employee_ e = new Employee_();

    Listable<Employee> stmt = nativeSql.from(e).where(c -> c.eq(e.employeeId, 1)).forUpdate();

    List<Employee> list = stmt.fetch();

    assertEquals(1, list.size());
  }

  @Test
  void union() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();

    SetOperand<Tuple2<Integer, String>> stmt1 =
        nativeSql.from(e).select(e.employeeId, e.employeeName);
    SetOperand<Tuple2<Integer, String>> stmt2 =
        nativeSql.from(d).select(d.departmentId, d.departmentName);
    SetOperand<Tuple2<Integer, String>> stmt3 = stmt1.union(stmt2);

    List<Tuple2<Integer, String>> list = stmt3.fetch();

    assertEquals(18, list.size());
  }

  @Test
  void union_mapStream() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();

    SetOperand<Tuple2<Integer, String>> stmt1 =
        nativeSql.from(e).select(e.employeeId, e.employeeName);
    SetOperand<Tuple2<Integer, String>> stmt2 =
        nativeSql.from(d).select(d.departmentId, d.departmentName);
    SetOperand<Tuple2<Integer, String>> stmt3 = stmt1.union(stmt2);

    long count = stmt3.mapStream(Stream::count);

    assertEquals(18, count);
  }

  @Test
  void unionAll_entity() {
    Department_ d = new Department_();

    SetOperand<Department> stmt1 = nativeSql.from(d);
    SetOperand<Department> stmt2 = nativeSql.from(d);
    SetOperand<Department> stmt3 = stmt1.unionAll(stmt2);

    List<Department> list = stmt3.fetch();

    assertEquals(8, list.size());
  }

  @Test
  void union_orderBy() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();

    SetOperand<Tuple2<Integer, String>> stmt1 =
        nativeSql.from(e).select(e.employeeId, e.employeeName);
    SetOperand<Tuple2<Integer, String>> stmt2 =
        nativeSql.from(d).select(d.departmentId, d.departmentName);
    SetOperand<Tuple2<Integer, String>> stmt3 = stmt1.union(stmt2).orderBy(c -> c.asc(2));

    List<Tuple2<Integer, String>> list = stmt3.fetch();

    assertEquals(18, list.size());
  }

  @Test
  void union_multi_orderBy() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();

    SetOperand<Tuple2<Integer, String>> stmt1 =
        nativeSql.from(e).select(e.employeeId, e.employeeName);
    SetOperand<Tuple2<Integer, String>> stmt2 =
        nativeSql.from(d).select(d.departmentId, d.departmentName);
    SetOperand<Tuple2<Integer, String>> stmt3 =
        nativeSql.from(e).select(e.employeeId, e.employeeName);

    SetOperand<Tuple2<Integer, String>> stmt4 =
        stmt1.union(stmt2).unionAll(stmt3).orderBy(c -> c.asc(2));

    List<Tuple2<Integer, String>> list = stmt4.fetch();

    assertEquals(32, list.size());
  }
}

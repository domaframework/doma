package example;

import static java.util.stream.Collectors.counting;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.seasar.doma.jdbc.criteria.AggregateFunctions.count;
import static org.seasar.doma.jdbc.criteria.AggregateFunctions.min;
import static org.seasar.doma.jdbc.criteria.AggregateFunctions.sum;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.criteria.NativeSql;
import org.seasar.doma.jdbc.criteria.Tuple2;
import org.seasar.doma.jdbc.criteria.statement.Mappable;
import org.seasar.doma.jdbc.criteria.statement.Statement;

@ExtendWith(Env.class)
public class NativeSqlSelectTest {

  private final NativeSql nativeSql;

  public NativeSqlSelectTest(Config config) {
    this.nativeSql = new NativeSql(config);
  }

  @Test
  void from() {
    Employee_ e = new Employee_();

    Statement<List<Employee>> stmt = nativeSql.from(e);

    List<Employee> list = stmt.execute();
    assertEquals(14, list.size());
  }

  @Test
  void stream() {
    Employee_ e = new Employee_();

    Statement<Long> stmt = nativeSql.from(e).stream(Stream::count);

    long count = stmt.execute();
    assertEquals(14, count);
  }

  @Test
  void collect() {
    Employee_ e = new Employee_();

    Statement<Long> stmt = nativeSql.from(e).collect(counting());

    long count = stmt.execute();
    assertEquals(14, count);
  }

  @Test
  void map() {
    Employee_ e = new Employee_();

    Statement<List<String>> stmt =
        nativeSql.from(e).<String>select(e.employeeName).map(row -> row.get(e.employeeName));

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.EMPLOYEE_NAME from EMPLOYEE t0_", sql.getFormattedSql());
    List<String> list = stmt.execute();
    assertEquals(14, list.size());
    assertEquals("SMITH", list.get(0));
  }

  @Test
  void map_stream() {
    Employee_ e = new Employee_();

    Statement<Long> stmt =
        nativeSql.from(e).<String>select(e.employeeName).map(row -> row.get(e.employeeName)).stream(
            Stream::count);

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.EMPLOYEE_NAME from EMPLOYEE t0_", sql.getFormattedSql());
    long count = stmt.execute();
    assertEquals(14, count);
  }

  @Test
  void map_collect() {
    Employee_ e = new Employee_();

    Statement<Long> stmt =
        nativeSql
            .from(e)
            .<String>select(e.employeeName)
            .map(row -> row.get(e.employeeName))
            .collect(counting());

    Sql<?> sql = stmt.asSql();
    assertEquals("select t0_.EMPLOYEE_NAME from EMPLOYEE t0_", sql.getFormattedSql());
    long count = stmt.execute();
    assertEquals(14, count);
  }

  @Test
  void where() {
    Employee_ e = new Employee_();

    Statement<List<Employee>> stmt = nativeSql.from(e).where(c -> c.eq(e.departmentId, 2));

    List<Employee> list = stmt.execute();
    assertEquals(5, list.size());
  }

  @Test
  void aggregate() {
    Employee_ e = new Employee_();

    Statement<List<Salary>> stmt =
        nativeSql.from(e).<Salary>select(sum(e.salary)).map(row -> row.get(sum(e.salary)));

    List<Salary> list = stmt.execute();
    assertEquals(1, list.size());
    assertEquals(0, list.get(0).getValue().compareTo(new BigDecimal("29025")));

    System.out.println(stmt.asSql().getRawSql());
  }

  @Test
  void groupBy() {
    Employee_ e = new Employee_();

    Statement<List<Tuple2<Integer, Long>>> stmt =
        nativeSql
            .from(e)
            .groupBy(e.departmentId)
            .<Tuple2<Integer, Long>>select(e.departmentId, count())
            .map(
                row -> {
                  Integer id = row.get(e.departmentId);
                  Long count = row.get(count());
                  return new Tuple2<>(id, count);
                });

    List<Tuple2<Integer, Long>> list = stmt.execute();
    assertEquals(3, list.size());

    System.out.println(stmt.asSql().getRawSql());
  }

  @Test
  void having() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();

    Statement<List<Tuple2<Long, String>>> stmt =
        nativeSql
            .from(e)
            .innerJoin(d, on -> on.eq(e.departmentId, d.departmentId))
            .groupBy(d.departmentName)
            .having(c -> c.gt(count(), 3L))
            .orderBy(c -> c.asc(count()))
            .<Tuple2<Long, String>>select(count(), d.departmentName)
            .map(
                row -> {
                  Long first = row.get(count());
                  String second = row.get(d.departmentName);
                  return new Tuple2<>(first, second);
                });

    List<Tuple2<Long, String>> list = stmt.execute();
    assertEquals(2, list.size());
    assertEquals(new Tuple2<>(5L, "RESEARCH"), list.get(0));
    assertEquals(new Tuple2<>(6L, "SALES"), list.get(1));

    System.out.println(stmt.asSql().getRawSql());
  }

  @Test
  void having_multi_conditions() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();

    Statement<List<Tuple2<Long, String>>> stmt =
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
            .<Tuple2<Long, String>>select(count(), d.departmentName)
            .map(
                row -> {
                  Long first = row.get(count());
                  String second = row.get(d.departmentName);
                  return new Tuple2<>(first, second);
                });

    List<Tuple2<Long, String>> list = stmt.execute();
    assertEquals(3, list.size());
    assertEquals(new Tuple2<>(3L, "ACCOUNTING"), list.get(0));
    assertEquals(new Tuple2<>(5L, "RESEARCH"), list.get(1));
    assertEquals(new Tuple2<>(6L, "SALES"), list.get(2));

    System.out.println(stmt.asSql().getRawSql());
  }

  @Test
  void limit_offset() {
    Employee_ e = new Employee_();

    Statement<List<Employee>> stmt =
        nativeSql.from(e).limit(5).offset(3).orderBy(c -> c.asc(e.employeeNo));

    List<Employee> list = stmt.execute();
    assertEquals(5, list.size());

    System.out.println(stmt.asSql());
  }

  @Test
  void forUpdate() {
    Employee_ e = new Employee_();

    Statement<List<Employee>> stmt =
        nativeSql.from(e).where(c -> c.eq(e.employeeId, 1)).forUpdate();

    List<Employee> list = stmt.execute();
    assertEquals(1, list.size());

    System.out.println(stmt.asSql());
  }

  @Test
  void union() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();

    Mappable<Tuple2<Integer, String>> stmt1 =
        nativeSql.from(e).select(e.employeeId, e.employeeName);
    Mappable<Tuple2<Integer, String>> stmt2 =
        nativeSql.from(d).select(d.departmentId, d.departmentName);
    Statement<List<Tuple2<Integer, String>>> stmt3 =
        stmt1
            .union(stmt2)
            .map(
                row -> {
                  Integer id = row.get(e.employeeId);
                  String name = row.get(e.employeeName);
                  return new Tuple2<>(id, name);
                });

    List<Tuple2<Integer, String>> list = stmt3.execute();
    assertEquals(18, list.size());
  }

  @Test
  void union_stream() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();

    Mappable<Tuple2<Integer, String>> stmt1 =
        nativeSql.from(e).select(e.employeeId, e.employeeName);
    Mappable<Tuple2<Integer, String>> stmt2 =
        nativeSql.from(d).select(d.departmentId, d.departmentName);
    Statement<List<Tuple2<Integer, String>>> stmt3 =
        stmt1
            .union(stmt2)
            .map(
                row -> {
                  Integer id = row.get(e.employeeId);
                  String name = row.get(e.employeeName);
                  return new Tuple2<>(id, name);
                });

    List<Tuple2<Integer, String>> list = stmt3.execute();
    assertEquals(18, list.size());
  }
}

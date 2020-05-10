package example;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
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
import org.seasar.doma.jdbc.criteria.NativeSql;
import org.seasar.doma.jdbc.criteria.def.PropertyDef;
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
  void select() {
    Employee_ e = new Employee_();

    List<String> list = nativeSql.from(e).select(e.employeeName).fetch();

    assertEquals(14, list.size());
    assertEquals("SMITH", list.get(0));
  }

  @Test
  void select_tuple2() {
    Employee_ e = new Employee_();

    List<Tuple2<String, Integer>> list =
        nativeSql.from(e).select(e.employeeName, e.employeeNo).fetch();

    assertEquals(14, list.size());
    assertEquals("SMITH", list.get(0).getItem1());
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
  void selectAsList() {
    Employee_ e = new Employee_();

    List<List<Object>> list =
        nativeSql
            .from(e)
            .orderBy(c -> c.asc(e.employeeId))
            .select(new PropertyDef<?>[] {e.employeeId, e.employeeName})
            .fetch();

    assertEquals(14, list.size());
    List<Object> row = list.get(0);
    assertEquals(2, row.size());
    assertEquals(1, row.get(0));
    assertEquals("SMITH", row.get(1));
  }

  @Test
  void selectAsList_emptySelect() {
    Employee_ e = new Employee_();

    List<List<Object>> list = nativeSql.from(e).orderBy(c -> c.asc(e.employeeId)).select().fetch();

    assertEquals(14, list.size());
    List<Object> row = list.get(0);
    assertEquals(9, row.size());
    assertEquals(1, row.get(0));
    assertEquals(7369, row.get(1));
    assertEquals("SMITH", row.get(2));
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
}

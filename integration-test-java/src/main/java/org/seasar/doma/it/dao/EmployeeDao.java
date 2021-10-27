package org.seasar.doma.it.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;
import org.seasar.doma.BatchDelete;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.Select;
import org.seasar.doma.SelectType;
import org.seasar.doma.Sql;
import org.seasar.doma.Suppress;
import org.seasar.doma.it.domain.Hiredate;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.builder.SelectBuilder;
import org.seasar.doma.message.Message;

@Dao
public interface EmployeeDao {

  @Select
  List<Employee> selectByExample(Employee e);

  @Select
  List<Employee> selectWithOptionalOrderBy(String employeeName, String orderBy);

  @Select
  Employee selectById(Integer employeeId);

  @Select
  Employee selectById(Integer employeeId, SelectOptions options);

  @Sql("select * from EMPLOYEE where EMPLOYEE_NAME in /*names*/('aaa', 'bbb')")
  @Select
  List<Employee> selectByNames(List<String> names);

  @Select(mapKeyNaming = MapKeyNamingType.CAMEL_CASE)
  Map<String, Object> selectByIdAsMap(Integer employeeId);

  @Select(mapKeyNaming = MapKeyNamingType.CAMEL_CASE)
  List<Map<String, Object>> selectAllAsMapList();

  @Select(strategy = SelectType.STREAM, mapKeyNaming = MapKeyNamingType.CAMEL_CASE)
  <R> R selectAllAsMapList(Function<Stream<Map<String, Object>>, R> mapper);

  @Select(strategy = SelectType.STREAM, mapKeyNaming = MapKeyNamingType.CAMEL_CASE)
  <R> R selectAllAsMapList(Function<Stream<Map<String, Object>>, R> mapper, SelectOptions options);

  @Select
  List<Employee> selectByNamePrefix(String employeeName);

  @Select
  List<Employee> selectByNameInfix(String employeeName);

  @Select
  List<Employee> selectByNameSuffix(String employeeName);

  @Select
  List<Employee> selectAll();

  @Select
  List<Employee> selectAll(SelectOptions options);

  @Select(ensureResultMapping = true)
  List<Employee> selectOnlyNameWithMappingCheck();

  @Select(ensureResultMapping = false)
  List<Employee> selectOnlyNameWithoutMappingCheck();

  @Select
  List<Employee> selectByHiredate(Hiredate hiredate);

  @Select
  List<Employee> selectByHiredates(List<Hiredate> hiredates);

  @Select(strategy = SelectType.STREAM)
  <R> R streamAll(Function<Stream<Employee>, R> mapper);

  @Select(strategy = SelectType.STREAM)
  <R> R streamAll(Function<Stream<Employee>, R> mapper, SelectOptions options);

  @Select(strategy = SelectType.STREAM)
  <R> R streamAllSalary(Function<Stream<BigDecimal>, R> mapper);

  @Select(strategy = SelectType.STREAM)
  <R> R streamAllSalary(Function<Stream<BigDecimal>, R> mapper, SelectOptions options);

  @Select(strategy = SelectType.STREAM)
  <R> R streamBySalary(BigDecimal salary, Function<Stream<Employee>, R> mapper);

  @Select(strategy = SelectType.COLLECT)
  <R> R collectAll(Collector<Employee, ?, R> collector);

  @Select
  @Suppress(messages = {Message.DOMA4274})
  Stream<Employee> streamAll();

  @Select
  @Suppress(messages = {Message.DOMA4274})
  Stream<Employee> streamAll(SelectOptions options);

  @Select
  @Suppress(messages = {Message.DOMA4274})
  Stream<Employee> streamBySalary(BigDecimal salary);

  default List<Employee> selectWithBuilder() {
    Config config = Config.get(this);
    SelectBuilder builder = SelectBuilder.newInstance(config);
    builder.sql("select * from EMPLOYEE");
    return builder.getEntityResultList(Employee.class);
  }

  default Stream<Employee> streamWithBuilder() {
    Config config = Config.get(this);
    SelectBuilder builder = SelectBuilder.newInstance(config);
    builder.sql("select * from EMPLOYEE");
    return builder.streamEntity(Employee.class);
  }

  @Delete
  int delete(Employee entity);

  @Delete(sqlFile = true)
  int deleteBySqlFile(Employee entity);

  @Delete(ignoreVersion = true)
  int delete_ignoreVersion(Employee entity);

  @Delete(suppressOptimisticLockException = true)
  int delete_suppressOptimisticLockException(Employee entity);

  @BatchDelete
  int[] delete(List<Employee> entity);

  @BatchDelete(ignoreVersion = true)
  int[] delete_ignoreVersion(List<Employee> entity);

  @BatchDelete(suppressOptimisticLockException = true)
  int[] delete_suppressOptimisticLockException(List<Employee> entity);

  @BatchDelete(sqlFile = true)
  int[] deleteBySqlFile(List<Employee> entity);

  @Insert
  int insert(Employee entity);
}

/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.it.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;
import org.seasar.doma.AggregateStrategy;
import org.seasar.doma.AssociationLinker;
import org.seasar.doma.BatchDelete;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.Select;
import org.seasar.doma.SelectType;
import org.seasar.doma.Sql;
import org.seasar.doma.Suppress;
import org.seasar.doma.Update;
import org.seasar.doma.it.domain.Hiredate;
import org.seasar.doma.it.entity.Address;
import org.seasar.doma.it.entity.Department;
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

  @Select(aggregateStrategy = EmployeeStrategy.class)
  Employee selectByIdAsAggregate(Integer employeeId);

  @Select(aggregateStrategy = EmployeeStrategy.class)
  Optional<Employee> selectOptionalByIdAsAggregate(Integer employeeId);

  @Select(aggregateStrategy = EmployeeStrategy.class)
  List<Employee> selectAllAsAggregate();

  @Select(aggregateStrategy = ManagerStrategy.class)
  List<Employee> selectAllWithManager();

  @Select(aggregateStrategy = ManagerStrategy.class)
  List<Employee> selectByIdWithManager(List<Integer> ids);

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

  @Select
  List<Employee> selectDistinctAll(SelectOptions options);

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

  @Update
  int update(Employee entity);
}

@AggregateStrategy(root = Employee.class, tableAlias = "e")
interface EmployeeStrategy {
  @AssociationLinker(propertyPath = "department", tableAlias = "d")
  BiFunction<Employee, Department, Employee> department =
      (e, d) -> {
        e.setDepartment(d);
        d.getEmployeeList().add(e);
        return e;
      };

  @AssociationLinker(propertyPath = "address", tableAlias = "a")
  BiConsumer<Employee, Address> address = Employee::setAddress;
}

@AggregateStrategy(root = Employee.class, tableAlias = "e")
interface ManagerStrategy {

  @AssociationLinker(propertyPath = "manager", tableAlias = "m")
  BiFunction<Employee, Employee, Employee> manager =
      (e, m) -> {
        m.getAssistants().add(e);
        e.setManager(m);
        return e;
      };
}

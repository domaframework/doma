/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.it.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.seasar.doma.AccessLevel;
import org.seasar.doma.BatchDelete;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.ResultHandlerType;
import org.seasar.doma.Select;
import org.seasar.doma.it.ItConfig;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.builder.SelectBuilder;

@Dao(config = ItConfig.class, accessLevel = AccessLevel.PACKAGE)
public interface EmployeeDao {

    static EmployeeDao get() {
        return new EmployeeDaoImpl();
    }

    static EmployeeDao get(Connection connection) {
        return new EmployeeDaoImpl(connection);
    }

    static EmployeeDao get(DataSource dataSource) {
        return new EmployeeDaoImpl(dataSource);
    }

    @Select
    List<Employee> selectByExample(Employee e);

    @Select
    List<Employee> selectWithOptionalOrderBy(String employeeName, String orderBy);

    @Select
    Employee selectById(Integer employeeId);

    @Select
    Employee selectById(Integer employeeId, SelectOptions options);

    @Select
    List<Employee> selectByNames(List<String> names);

    @Select(mapKeyNaming = MapKeyNamingType.CAMEL_CASE)
    Map<String, Object> selectByIdAsMap(Integer employeeId);

    @Select(mapKeyNaming = MapKeyNamingType.CAMEL_CASE)
    List<Map<String, Object>> selectAllAsMapList();

    @Select(resultHandler = ResultHandlerType.ITERATION, mapKeyNaming = MapKeyNamingType.CAMEL_CASE)
    <R> R selectAllAsMapList(IterationCallback<Map<String, Object>, R> callback);

    @Select(resultHandler = ResultHandlerType.ITERATION, mapKeyNaming = MapKeyNamingType.CAMEL_CASE)
    <R> R selectAllAsMapList(
            IterationCallback<Map<String, Object>, R> callback,
            SelectOptions options);

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

    @Select(resultHandler = ResultHandlerType.ITERATION)
    <R> R selectAll(IterationCallback<Employee, R> callback);

    @Select(resultHandler = ResultHandlerType.ITERATION)
    <R> R selectAll(IterationCallback<Employee, R> callback,
            SelectOptions options);

    @Select(resultHandler = ResultHandlerType.ITERATION)
    <R> R selectAllSalary(IterationCallback<BigDecimal, R> callback);

    @Select(resultHandler = ResultHandlerType.ITERATION)
    <R> R selectAllSalary(IterationCallback<BigDecimal, R> callback,
            SelectOptions options);

    @Select(resultHandler = ResultHandlerType.STREAM)
    <R> R streamAll(Function<Stream<Employee>, R> mapper);

    @Select(resultHandler = ResultHandlerType.STREAM)
    <R> R streamBySalary(BigDecimal salary, Function<Stream<Employee>, R> mapper);

    default List<Employee> selectWithBuilder() {
        Config config = Config.get(this);
        SelectBuilder builder = SelectBuilder.newInstance(config);
        builder.sql("select * from Employee");
        return builder.getEntityResultList(Employee.class);
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
}

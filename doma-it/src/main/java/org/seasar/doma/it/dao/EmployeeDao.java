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

import java.util.List;

import org.seasar.doma.BatchDelete;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Select;
import org.seasar.doma.it.ItConfig;
import org.seasar.doma.it.domain.IdDomain;
import org.seasar.doma.it.domain.SalaryDomain;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.SelectOptions;

@Dao(config = ItConfig.class)
public interface EmployeeDao {

    @Select
    Employee selectById(IdDomain employee_id);

    @Select
    Employee selectById(IdDomain employee_id, SelectOptions options);

    @Select
    List<Employee> selectAll();

    @Select
    List<Employee> selectAll(SelectOptions options);

    @Select(iterate = true)
    <R> R selectAll(IterationCallback<R, Employee> callback);

    @Select(iterate = true)
    <R> R selectAll(IterationCallback<R, Employee> callback,
            SelectOptions options);

    @Select(iterate = true)
    <R> R selectAllSalary(IterationCallback<R, SalaryDomain> callback);

    @Select(iterate = true)
    <R> R selectAllSalary(IterationCallback<R, SalaryDomain> callback,
            SelectOptions options);

    @Delete
    int delete(Employee entity);

    @Delete(sqlFile = true)
    int deleteBySqlFile(Employee entity);

    @Delete(ignoreVersion = true)
    int delete_ignoresVersion(Employee entity);

    @Delete(suppressOptimisticLockException = true)
    int delete_suppressesOptimisticLockException(Employee entity);

    @BatchDelete
    int[] delete(List<Employee> entity);

    @BatchDelete(ignoreVersion = true)
    int[] delete_ignoresVersion(List<Employee> entity);

    @BatchDelete(suppressOptimisticLockException = true)
    int[] delete_suppressesOptimisticLockException(List<Employee> entity);

    @BatchDelete(sqlFile = true)
    int[] deleteBySqlFile(List<Employee> entity);
}

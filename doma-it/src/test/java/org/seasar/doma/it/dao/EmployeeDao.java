package org.seasar.doma.it.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.it.ItConfig;
import org.seasar.doma.it.domain.IdDomain;
import org.seasar.doma.it.domain.SalaryDomain;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.SelectOptions;

@Dao(config = ItConfig.class)
public interface EmployeeDao extends GenericDao<Employee> {

    @Select
    Employee selectById(IdDomain employee_id);

    @Select
    Employee selectById(IdDomain employee_id, SelectOptions options);

    @Select
    List<Employee> selectAll();

    @Select
    List<Employee> selectAll(SelectOptions options);

    @Select(iteration = true)
    <R> R selectAll(IterationCallback<R, Employee> callback);

    @Select(iteration = true)
    <R> R selectAll(IterationCallback<R, Employee> callback,
            SelectOptions options);

    @Select(iteration = true)
    <R> R selectAllSalary(IterationCallback<R, SalaryDomain> callback);

    @Select(iteration = true)
    <R> R selectAllSalary(IterationCallback<R, SalaryDomain> callback,
            SelectOptions options);
}

package org.seasar.doma.it.dao;

import java.util.List;
import java.util.Optional;
import org.seasar.doma.Dao;
import org.seasar.doma.it.entity.Department_;
import org.seasar.doma.it.entity.Employee;
import org.seasar.doma.it.entity.Employee_;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.Entityql;

@Dao
public interface CriteriaDao {

  default List<Employee> selectAll() {
    Employee_ e = new Employee_();
    Department_ d = new Department_();
    Entityql entityql = new Entityql(Config.get(this));
    return entityql
        .from(e)
        .innerJoin(d, on -> on.eq(e.departmentId, d.departmentId))
        .associate(
            e,
            d,
            (employee, department) -> {
              employee.setDepartment(department);
              department.getEmployeeList().add(employee);
            })
        .fetch();
  }

  default Optional<Employee> selectById(Integer id) {
    Employee_ e = new Employee_();
    Department_ d = new Department_();
    Entityql entityql = new Entityql(Config.get(this));
    return entityql
        .from(e)
        .innerJoin(d, on -> on.eq(e.departmentId, d.departmentId))
        .associate(
            e,
            d,
            (employee, department) -> {
              employee.setDepartment(department);
              department.getEmployeeList().add(employee);
            })
        .where(c -> c.eq(e.employeeId, id))
        .fetchOptional();
  }
}

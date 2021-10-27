package org.seasar.doma.it.entity;

import java.util.Optional;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.Version;
import org.seasar.doma.it.domain.Identity;
import org.seasar.doma.it.domain.Salary;

@Entity
@Table(name = "EMPLOYEE")
public class Worker {

  @Id public Optional<Integer> employeeId;

  public Optional<Integer> employeeNo;

  public Optional<String> employeeName;

  public Optional<Integer> managerId;

  public Optional<java.sql.Date> hiredate;

  public Optional<Salary> salary;

  public Optional<Identity<Department>> departmentId;

  public Optional<Integer> addressId;

  @Version public Optional<Integer> version;
}

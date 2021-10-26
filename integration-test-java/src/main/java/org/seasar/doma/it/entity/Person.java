package org.seasar.doma.it.entity;

import java.sql.Date;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.Version;
import org.seasar.doma.it.domain.Identity;
import org.seasar.doma.it.domain.Salary;

@Entity(immutable = true, listener = PersonListener.class)
@Table(name = "EMPLOYEE")
public class Person {

  @Id final Integer employeeId;

  final Integer employeeNo;

  final String employeeName;

  final Integer managerId;

  final java.sql.Date hiredate;

  final Salary salary;

  final Identity<Department> departmentId;

  final Integer addressId;

  @Version final Integer version;

  public Person(
      Integer employeeId,
      Integer employeeNo,
      String employeeName,
      Integer managerId,
      Date hiredate,
      Salary salary,
      Identity<Department> departmentId,
      Integer addressId,
      Integer version) {
    this.employeeId = employeeId;
    this.employeeNo = employeeNo;
    this.employeeName = employeeName;
    this.managerId = managerId;
    this.hiredate = hiredate;
    this.salary = salary;
    this.departmentId = departmentId;
    this.addressId = addressId;
    this.version = version;
  }

  public Integer getEmployeeId() {
    return employeeId;
  }

  public Integer getEmployeeNo() {
    return employeeNo;
  }

  public String getEmployeeName() {
    return employeeName;
  }

  public Integer getManagerId() {
    return managerId;
  }

  public java.sql.Date getHiredate() {
    return hiredate;
  }

  public Salary getSalary() {
    return salary;
  }

  public Identity<Department> getDepartmentId() {
    return departmentId;
  }

  public Integer getAddressId() {
    return addressId;
  }

  public Integer getVersion() {
    return version;
  }
}

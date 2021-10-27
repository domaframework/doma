package org.seasar.doma.it.criteria;

import java.util.Date;
import java.util.Optional;
import java.util.OptionalInt;
import org.seasar.doma.Entity;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;

@Entity(metamodel = @Metamodel)
@Table(name = "EMPLOYEE")
public class Person {

  private Integer employeeId;
  private OptionalInt employeeNo;
  private String employeeName;
  private Optional<Integer> managerId;
  private java.sql.Date hiredate;
  private Optional<Salary> salary;
  private Integer departmentId;
  private Integer addressId;
  private Integer version;

  public Integer getEmployeeId() {
    return employeeId;
  }

  public void setEmployeeId(Integer employeeId) {
    this.employeeId = employeeId;
  }

  public OptionalInt getEmployeeNo() {
    return employeeNo;
  }

  public void setEmployeeNo(OptionalInt employeeNo) {
    this.employeeNo = employeeNo;
  }

  public String getEmployeeName() {
    return employeeName;
  }

  public void setEmployeeName(String employeeName) {
    this.employeeName = employeeName;
  }

  public Optional<Integer> getManagerId() {
    return managerId;
  }

  public void setManagerId(Optional<Integer> managerId) {
    this.managerId = managerId;
  }

  public Date getHiredate() {
    return hiredate;
  }

  public void setHiredate(java.sql.Date hiredate) {
    this.hiredate = hiredate;
  }

  public Optional<Salary> getSalary() {
    return salary;
  }

  public void setSalary(Optional<Salary> salary) {
    this.salary = salary;
  }

  public Integer getDepartmentId() {
    return departmentId;
  }

  public void setDepartmentId(Integer departmentId) {
    this.departmentId = departmentId;
  }

  public Integer getAddressId() {
    return addressId;
  }

  public void setAddressId(Integer addressId) {
    this.addressId = addressId;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }
}

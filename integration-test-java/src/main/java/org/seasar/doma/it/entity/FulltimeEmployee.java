package org.seasar.doma.it.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.Version;
import org.seasar.doma.it.domain.Identity;
import org.seasar.doma.it.domain.Money;

@Entity
@Table(name = "EMPLOYEE")
public class FulltimeEmployee {

  @Id private Integer employeeId;

  private Integer employeeNo;

  private String employeeName;

  private Integer managerId;

  private java.sql.Date hiredate;

  private Money salary;

  private Identity<Department> departmentId;

  private Integer addressId;

  @Version private Integer version;

  public Integer getEmployeeId() {
    return employeeId;
  }

  public void setEmployeeId(Integer employee_id) {
    this.employeeId = employee_id;
  }

  public Integer getEmployeeNo() {
    return employeeNo;
  }

  public void setEmployeeNo(Integer employeeNo) {
    this.employeeNo = employeeNo;
  }

  public String getEmployeeName() {
    return employeeName;
  }

  public void setEmployeeName(String employeeName) {
    this.employeeName = employeeName;
  }

  public Integer getManagerId() {
    return managerId;
  }

  public void setManagerId(Integer managerId) {
    this.managerId = managerId;
  }

  public java.sql.Date getHiredate() {
    return hiredate;
  }

  public void setHiredate(java.sql.Date hiredate) {
    this.hiredate = hiredate;
  }

  public Money getSalary() {
    return salary;
  }

  public void setSalary(Money salary) {
    this.salary = salary;
  }

  public Identity<Department> getDepartmentId() {
    return departmentId;
  }

  public void setDepartmentId(Identity<Department> departmentId) {
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

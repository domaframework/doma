package org.seasar.doma.it.criteria;

import java.time.LocalDate;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;
import org.seasar.doma.Transient;
import org.seasar.doma.Version;

@Entity(immutable = true, metamodel = @Metamodel)
@Table(name = "EMPLOYEE")
public class Emp {

  @Id private final Integer employeeId;
  private final Integer employeeNo;
  private final String employeeName;
  private final Integer managerId;
  private final LocalDate hiredate;
  private final Salary salary;
  private final Integer departmentId;
  private final Integer addressId;
  @Version private final Integer version;
  @Transient private final Dept department;
  @Transient private final Emp manager;

  public Emp(
      Integer employeeId,
      Integer employeeNo,
      String employeeName,
      Integer managerId,
      LocalDate hiredate,
      Salary salary,
      Integer departmentId,
      Integer addressId,
      Integer version) {
    this(
        employeeId,
        employeeNo,
        employeeName,
        managerId,
        hiredate,
        salary,
        departmentId,
        addressId,
        version,
        null,
        null);
  }

  public Emp(
      Integer employeeId,
      Integer employeeNo,
      String employeeName,
      Integer managerId,
      LocalDate hiredate,
      Salary salary,
      Integer departmentId,
      Integer addressId,
      Integer version,
      Dept department,
      Emp manager) {
    this.employeeId = employeeId;
    this.employeeNo = employeeNo;
    this.employeeName = employeeName;
    this.managerId = managerId;
    this.hiredate = hiredate;
    this.salary = salary;
    this.departmentId = departmentId;
    this.addressId = addressId;
    this.version = version;
    this.department = department;
    this.manager = manager;
  }

  public Emp withDept(Dept dept) {
    return new Emp(
        this.getEmployeeId(),
        this.getEmployeeNo(),
        this.getEmployeeName(),
        this.getManagerId(),
        this.getHiredate(),
        this.getSalary(),
        this.getDepartmentId(),
        this.getAddressId(),
        this.getVersion(),
        dept,
        this.getManager());
  }

  public Emp withManager(Emp manager) {
    return new Emp(
        this.getEmployeeId(),
        this.getEmployeeNo(),
        this.getEmployeeName(),
        this.getManagerId(),
        this.getHiredate(),
        this.getSalary(),
        this.getDepartmentId(),
        this.getAddressId(),
        this.getVersion(),
        this.getDepartment(),
        manager);
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

  public LocalDate getHiredate() {
    return hiredate;
  }

  public Salary getSalary() {
    return salary;
  }

  public Integer getDepartmentId() {
    return departmentId;
  }

  public Integer getAddressId() {
    return addressId;
  }

  public Integer getVersion() {
    return version;
  }

  public Dept getDepartment() {
    return department;
  }

  public Emp getManager() {
    return manager;
  }
}

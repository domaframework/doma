package example;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Transient;
import org.seasar.doma.Version;

@Entity
public class Employee {

  @Id private Integer employeeId;
  private Integer employeeNo;
  private String employeeName;
  private Integer managerId;
  private EmployeeInfo employeeInfo;
  private Salary salary;
  private Integer departmentId;
  private Integer addressId;
  @Version private Integer version;
  @Transient private Department department;
  @Transient private Employee manager;
  @Transient private Address address;

  public Integer getEmployeeId() {
    return employeeId;
  }

  public void setEmployeeId(Integer employeeId) {
    this.employeeId = employeeId;
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

  public EmployeeInfo getEmployeeInfo() {
    return employeeInfo;
  }

  public void setEmployeeInfo(EmployeeInfo employeeInfo) {
    this.employeeInfo = employeeInfo;
  }

  public Salary getSalary() {
    return salary;
  }

  public void setSalary(Salary salary) {
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

  public Department getDepartment() {
    return department;
  }

  public void setDepartment(Department department) {
    this.department = department;
  }

  public Employee getManager() {
    return manager;
  }

  public void setManager(Employee manager) {
    this.manager = manager;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }
}

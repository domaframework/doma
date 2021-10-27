package org.seasar.doma.it.entity;

import java.util.ArrayList;
import java.util.List;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.OriginalStates;
import org.seasar.doma.Transient;
import org.seasar.doma.Version;
import org.seasar.doma.it.domain.Identity;
import org.seasar.doma.it.domain.Location;

@Entity(metamodel = @Metamodel)
public class Department {

  @Id Identity<Department> departmentId;

  Integer departmentNo;

  String departmentName;

  Location<Department> location;

  @Version Integer version;

  @OriginalStates Department originalStates;

  @Transient List<Employee> employeeList = new ArrayList<>();

  public Identity<Department> getDepartmentId() {
    return departmentId;
  }

  public void setDepartmentId(Identity<Department> departmentId) {
    this.departmentId = departmentId;
  }

  public Integer getDepartmentNo() {
    return departmentNo;
  }

  public void setDepartmentNo(Integer departmentNo) {
    this.departmentNo = departmentNo;
  }

  public String getDepartmentName() {
    return departmentName;
  }

  public void setDepartmentName(String departmentName) {
    this.departmentName = departmentName;
  }

  public Location<Department> getLocation() {
    return location;
  }

  public void setLocation(Location<Department> location) {
    this.location = location;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public List<Employee> getEmployeeList() {
    return employeeList;
  }

  public void setEmployeeList(List<Employee> employeeList) {
    this.employeeList = employeeList;
  }
}

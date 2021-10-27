package org.seasar.doma.it.criteria;

import java.util.ArrayList;
import java.util.List;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.OriginalStates;
import org.seasar.doma.Transient;
import org.seasar.doma.Version;

@Entity(metamodel = @Metamodel)
public class DepartmentArchive {

  @Id private Integer departmentId;
  private Integer departmentNo;
  private String departmentName;
  private String location;
  @Version private Integer version;
  @OriginalStates private DepartmentArchive originalStates;
  @Transient private List<Employee> employeeList = new ArrayList<>();

  public Integer getDepartmentId() {
    return departmentId;
  }

  public void setDepartmentId(Integer departmentId) {
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

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public DepartmentArchive getOriginalStates() {
    return originalStates;
  }

  public void setOriginalStates(DepartmentArchive originalStates) {
    this.originalStates = originalStates;
  }

  public List<Employee> getEmployeeList() {
    return employeeList;
  }

  public void setEmployeeList(List<Employee> employeeList) {
    this.employeeList = employeeList;
  }
}

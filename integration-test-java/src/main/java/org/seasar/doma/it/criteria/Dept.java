package org.seasar.doma.it.criteria;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;
import org.seasar.doma.Version;

@Entity(immutable = true, metamodel = @Metamodel)
@Table(name = "DEPARTMENT")
public class Dept {

  @Id private final Integer departmentId;
  private final Integer departmentNo;
  private final String departmentName;
  private final String location;
  @Version private final Integer version;

  public Dept(
      Integer departmentId,
      Integer departmentNo,
      String departmentName,
      String location,
      Integer version) {
    this.departmentId = departmentId;
    this.departmentNo = departmentNo;
    this.departmentName = departmentName;
    this.location = location;
    this.version = version;
  }

  public Integer getDepartmentId() {
    return departmentId;
  }

  public Integer getDepartmentNo() {
    return departmentNo;
  }

  public String getDepartmentName() {
    return departmentName;
  }

  public String getLocation() {
    return location;
  }

  public Integer getVersion() {
    return version;
  }
}

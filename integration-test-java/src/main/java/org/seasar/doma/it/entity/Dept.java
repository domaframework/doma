package org.seasar.doma.it.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.Version;
import org.seasar.doma.it.domain.Identity;
import org.seasar.doma.it.domain.Location;

@Entity(immutable = true, listener = DeptListener.class)
@Table(name = "DEPARTMENT")
public class Dept {

  @Id final Identity<Dept> departmentId;

  final Integer departmentNo;

  final String departmentName;

  final Location<Dept> location;

  @Version final Integer version;

  public Dept(
      Identity<Dept> departmentId,
      Integer departmentNo,
      String departmentName,
      Location<Dept> location,
      Integer version) {
    super();
    this.departmentId = departmentId;
    this.departmentNo = departmentNo;
    this.departmentName = departmentName;
    this.location = location;
    this.version = version;
  }

  /** @return the departmentId */
  public Identity<Dept> getDepartmentId() {
    return departmentId;
  }

  /** @return the departmentNo */
  public Integer getDepartmentNo() {
    return departmentNo;
  }

  /** @return the departmentName */
  public String getDepartmentName() {
    return departmentName;
  }

  /** @return the location */
  public Location<Dept> getLocation() {
    return location;
  }

  /** @return the version */
  public Integer getVersion() {
    return version;
  }
}

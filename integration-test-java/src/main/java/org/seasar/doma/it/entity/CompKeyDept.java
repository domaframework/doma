package org.seasar.doma.it.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.Version;
import org.seasar.doma.it.domain.Identity;
import org.seasar.doma.it.domain.Location;

@Entity(immutable = true, listener = CompKeyDeptListener.class)
@Table(name = "COMP_KEY_DEPARTMENT")
public class CompKeyDept {

  @Id final Identity<CompKeyDept> departmentId1;

  @Id final Identity<CompKeyDept> departmentId2;

  final Integer departmentNo;

  final String departmentName;

  final Location<CompKeyDept> location;

  @Version final Integer version;

  public CompKeyDept(
      Identity<CompKeyDept> departmentId1,
      Identity<CompKeyDept> departmentId2,
      Integer departmentNo,
      String departmentName,
      Location<CompKeyDept> location,
      Integer version) {
    super();
    this.departmentId1 = departmentId1;
    this.departmentId2 = departmentId2;
    this.departmentNo = departmentNo;
    this.departmentName = departmentName;
    this.location = location;
    this.version = version;
  }

  /**
   * @return the departmentId1
   */
  public Identity<CompKeyDept> getDepartmentId1() {
    return departmentId1;
  }

  /**
   * @return the departmentId2
   */
  public Identity<CompKeyDept> getDepartmentId2() {
    return departmentId2;
  }

  /**
   * @return the departmentNo
   */
  public Integer getDepartmentNo() {
    return departmentNo;
  }

  /**
   * @return the departmentName
   */
  public String getDepartmentName() {
    return departmentName;
  }

  /**
   * @return the location
   */
  public Location<CompKeyDept> getLocation() {
    return location;
  }

  /**
   * @return the version
   */
  public Integer getVersion() {
    return version;
  }
}

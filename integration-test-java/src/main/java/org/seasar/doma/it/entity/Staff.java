package org.seasar.doma.it.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.Version;
import org.seasar.doma.it.embeddable.StaffInfo;

@Entity
@Table(name = "EMPLOYEE")
public class Staff {

  @Id public Integer employeeId;

  public Integer employeeNo;

  public String employeeName;

  public StaffInfo staffInfo;

  public Integer departmentId;

  public Integer addressId;

  @Version public Integer version;
}

package org.seasar.doma.it.entity;

import java.math.BigDecimal;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.TenantId;
import org.seasar.doma.Version;

@Entity
@Table(name = "EMPLOYEE")
public class Salesman {

  @Id public Integer employeeId;

  public Integer employeeNo;

  public String employeeName;

  public Integer managerId;

  public java.sql.Date hiredate;

  public BigDecimal salary;

  @TenantId public Integer departmentId;

  public Integer addressId;

  @Version public Integer version;
}

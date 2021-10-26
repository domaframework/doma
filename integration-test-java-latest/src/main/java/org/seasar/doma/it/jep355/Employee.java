package org.seasar.doma.it.jep355;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;
import org.seasar.doma.Version;

@Entity(metamodel = @Metamodel)
@Table(name = "EMPLOYEE")
public class Employee {
  @Id public Integer employeeId;
  public Integer employeeNo;
  public String employeeName;
  public Integer managerId;
  public LocalDate hiredate;
  public BigDecimal salary;
  public Integer departmentId;
  public Integer addressId;
  @Version private Integer version;
}

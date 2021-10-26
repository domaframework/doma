package org.seasar.doma.it.embeddable;

import java.sql.Date;
import org.seasar.doma.Embeddable;
import org.seasar.doma.it.domain.Salary;

@Embeddable
public class StaffInfo {

  public final int managerId;

  public final Date hiredate;

  public final Salary salary;

  public StaffInfo(int managerId, Date hiredate, Salary salary) {
    this.managerId = managerId;
    this.hiredate = hiredate;
    this.salary = salary;
  }
}

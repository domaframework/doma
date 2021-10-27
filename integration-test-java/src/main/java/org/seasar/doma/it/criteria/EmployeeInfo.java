package org.seasar.doma.it.criteria;

import java.time.LocalDate;
import org.seasar.doma.Embeddable;

@Embeddable
public class EmployeeInfo {
  private final LocalDate hiredate;

  public EmployeeInfo(LocalDate hiredate) {
    this.hiredate = hiredate;
  }

  public LocalDate getHiredate() {
    return hiredate;
  }
}

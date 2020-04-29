package org.seasar.doma.internal.apt.processor.entitydesc;

import java.time.LocalDate;
import org.seasar.doma.Embeddable;

@Embeddable
public class EmpInfo {
  private final LocalDate hiredate;

  public EmpInfo(LocalDate hiredate) {
    this.hiredate = hiredate;
  }
}

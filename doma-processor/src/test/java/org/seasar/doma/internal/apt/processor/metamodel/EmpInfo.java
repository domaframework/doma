package org.seasar.doma.internal.apt.processor.metamodel;

import java.time.LocalDate;
import org.seasar.doma.Embeddable;

@Embeddable
public class EmpInfo {
  @SuppressWarnings("FieldCanBeLocal")
  private final LocalDate hiredate;

  public EmpInfo(LocalDate hiredate) {
    this.hiredate = hiredate;
  }
}

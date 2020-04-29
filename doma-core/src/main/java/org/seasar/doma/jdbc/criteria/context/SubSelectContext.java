package org.seasar.doma.jdbc.criteria.context;

public class SubSelectContext<RESULT> {
  public final SelectContext context;

  public SubSelectContext(SelectContext context) {
    this.context = context;
  }
}

package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import org.seasar.doma.internal.apt.cttype.BasicCtType;

public class BasicResultListParameterMeta implements ResultListParameterMeta {

  protected final BasicCtType basicCtType;

  public BasicResultListParameterMeta(BasicCtType basicCtType) {
    assertNotNull(basicCtType);
    this.basicCtType = basicCtType;
  }

  public BasicCtType getBasicCtType() {
    return basicCtType;
  }

  @Override
  public <R, P> R accept(CallableSqlParameterMetaVisitor<R, P> visitor, P p) {
    return visitor.visitBasicResultListParameterMeta(this, p);
  }
}

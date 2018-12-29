package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import org.seasar.doma.internal.apt.cttype.BasicCtType;

/** @author taedium */
public class OptionalBasicResultListParameterMeta implements ResultListParameterMeta {

  protected final BasicCtType basicCtType;

  public OptionalBasicResultListParameterMeta(BasicCtType basicCtType) {
    assertNotNull(basicCtType);
    this.basicCtType = basicCtType;
  }

  public BasicCtType getBasicCtType() {
    return basicCtType;
  }

  @Override
  public <R, P> R accept(CallableSqlParameterMetaVisitor<R, P> visitor, P p) {
    return visitor.visitOptionalBasicResultListParameterMeta(this, p);
  }
}

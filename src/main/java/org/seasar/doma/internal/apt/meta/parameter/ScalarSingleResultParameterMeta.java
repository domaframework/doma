package org.seasar.doma.internal.apt.meta.parameter;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.internal.apt.cttype.ScalarCtType;

/** @author taedium */
public class ScalarSingleResultParameterMeta implements SingleResultParameterMeta {

  private final ScalarCtType scalarCtType;

  private final boolean optional;

  public ScalarSingleResultParameterMeta(ScalarCtType scalarCtType, boolean optional) {
    assertNotNull(scalarCtType);
    this.scalarCtType = scalarCtType;
    this.optional = optional;
  }

  public ScalarCtType getScalarCtType() {
    return scalarCtType;
  }

  public boolean isOptional() {
    return optional;
  }

  @Override
  public <P> void accept(CallableSqlParameterMetaVisitor<P> visitor, P p) {
    visitor.visitScalarSingleResultParameterMeta(this, p);
  }
}

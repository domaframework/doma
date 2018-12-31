package org.seasar.doma.internal.apt.meta.parameter;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import org.seasar.doma.internal.apt.cttype.BasicCtType;

public class BasicListParameterMeta implements CallableSqlParameterMeta {

  protected final String name;

  protected final BasicCtType basicCtType;

  public BasicListParameterMeta(String name, BasicCtType basicCtType) {
    assertNotNull(name, basicCtType);
    this.name = name;
    this.basicCtType = basicCtType;
  }

  public String getName() {
    return name;
  }

  public BasicCtType getBasicCtType() {
    return basicCtType;
  }

  @Override
  public <R, P> R accept(CallableSqlParameterMetaVisitor<R, P> visitor, P p) {
    return visitor.visitBasicListParameterMeta(this, p);
  }
}

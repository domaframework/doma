package org.seasar.doma.internal.apt.meta.parameter;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

public class OptionalDoubleListParameterMeta implements CallableSqlParameterMeta {

  protected final String name;

  public OptionalDoubleListParameterMeta(String name) {
    assertNotNull(name);
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public <R, P> R accept(CallableSqlParameterMetaVisitor<R, P> visitor, P p) {
    return visitor.visitOptionalDoubleListParameterMeta(this, p);
  }
}

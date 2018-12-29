package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

/** @author nakamura-to */
public class OptionalIntInOutParameterMeta implements CallableSqlParameterMeta {

  private final String name;

  public OptionalIntInOutParameterMeta(String name) {
    assertNotNull(name);
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public <R, P> R accept(CallableSqlParameterMetaVisitor<R, P> visitor, P p) {
    return visitor.visitOptionalIntInOutParameterMeta(this, p);
  }
}

package org.seasar.doma.internal.apt.meta.parameter;

public class OptionalLongSingleResultParameterMeta implements SingleResultParameterMeta {

  @Override
  public <R, P> R accept(CallableSqlParameterMetaVisitor<R, P> visitor, P p) {
    return visitor.visitOptionalLongSingleResultParameterMeta(this, p);
  }
}

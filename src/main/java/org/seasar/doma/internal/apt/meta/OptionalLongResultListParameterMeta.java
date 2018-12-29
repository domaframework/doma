package org.seasar.doma.internal.apt.meta;

public class OptionalLongResultListParameterMeta implements ResultListParameterMeta {

  @Override
  public <R, P> R accept(CallableSqlParameterMetaVisitor<R, P> visitor, P p) {
    return visitor.visitOptionalLongResultListParameterMeta(this, p);
  }
}

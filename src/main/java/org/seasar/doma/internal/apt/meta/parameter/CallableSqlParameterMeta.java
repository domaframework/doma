package org.seasar.doma.internal.apt.meta.parameter;

public interface CallableSqlParameterMeta {

  <P> void accept(CallableSqlParameterMetaVisitor<P> visitor, P p);
}

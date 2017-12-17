package org.seasar.doma.internal.apt.meta.parameter;

/**
 * @author taedium
 * 
 */
public interface CallableSqlParameterMeta {

    <R, P> R accept(CallableSqlParameterMetaVisitor<R, P> visitor, P p);
}

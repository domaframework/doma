package org.seasar.doma.internal.apt.meta;

/**
 * @author taedium
 * 
 */
public interface CallableStatementParameterMeta {

    String getName();

    void setName(String name);

    String getTypeName();

    void setTypeName(String typeName);

    <R, P> R accept(CallableStatementParameterMetaVisitor<R, P> visitor, P p);
}

package org.seasar.doma.internal.apt.meta;

import javax.lang.model.element.ExecutableElement;

/**
 * @author taedium
 * 
 */
public interface QueryMeta {

    String getName();

    ExecutableElement getExecutableElement();

    QueryKind getQueryKind();

    <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p);
}

package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;

/**
 * @author taedium
 * 
 */
public class DefaultQueryMeta extends AbstractQueryMeta {

    public DefaultQueryMeta(ExecutableElement method) {
        super(method);
    }

    @Override
    public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
        return visitor.visitDefaultQueryMeta(this, p);
    }

}

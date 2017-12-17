package org.seasar.doma.internal.apt.meta.query;

import javax.lang.model.element.ExecutableElement;

/**
 * @author taedium
 * 
 */
public abstract class AbstractCreateQueryMeta extends AbstractQueryMeta {

    protected AbstractCreateQueryMeta(ExecutableElement method) {
        super(method);
    }

    @Override
    public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
        return visitor.visitAbstractCreateQueryMeta(this, p);
    }

}

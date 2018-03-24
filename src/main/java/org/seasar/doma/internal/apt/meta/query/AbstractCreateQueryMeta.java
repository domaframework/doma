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
    public <P> void accept(QueryMetaVisitor<P> visitor, P p) {
        visitor.visitAbstractCreateQueryMeta(this, p);
    }

}

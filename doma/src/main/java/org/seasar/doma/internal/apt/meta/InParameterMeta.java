package org.seasar.doma.internal.apt.meta;

/**
 * @author taedium
 * 
 */
public class InParameterMeta extends AbstractParameterMeta {

    @Override
    public <R, P> R accept(CallableStatementParameterMetaVisitor<R, P> visitor, P p) {
        return visitor.visitInParameterMeta(this, p);
    }

}

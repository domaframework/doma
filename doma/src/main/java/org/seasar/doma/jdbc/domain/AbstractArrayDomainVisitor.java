package org.seasar.doma.jdbc.domain;

import org.seasar.doma.domain.DomainVisitor;

/**
 * @author taedium
 * 
 */
public interface AbstractArrayDomainVisitor<R, P, TH extends Throwable> extends
        DomainVisitor<R, P, TH> {

    public R visitAbstractArrayDomain(AbstractArrayDomain<?> domain, P p)
            throws TH;
}

package org.seasar.doma.domain;

/**
 * @author taedium
 * 
 */
public interface AbstractIntegerDomainVisitor<R, P, TH extends Throwable> extends
        DomainVisitor<R, P, TH> {

    R visitAbstractIntegerDomain(AbstractIntegerDomain<?> domain, P p)
            throws TH;

}

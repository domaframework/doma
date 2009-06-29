package org.seasar.doma.domain;

/**
 * @author taedium
 * 
 */
public interface AbstractStringDomainVisitor<R, P, TH extends Throwable> extends
        DomainVisitor<R, P, TH> {

    R visitAbstractStringDomain(AbstractStringDomain<?> domain, P p) throws TH;
}

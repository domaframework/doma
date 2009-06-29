package org.seasar.doma.domain;

/**
 * @author taedium
 * 
 */
public interface AbstractDateDomainVisitor<R, P, TH extends Throwable> extends
        DomainVisitor<R, P, TH> {

    R visitAbstractDateDomain(AbstractDateDomain<?> domain, P p) throws TH;

}

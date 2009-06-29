package org.seasar.doma.domain;

/**
 * @author taedium
 * 
 */
public interface AbstractTimeDomainVisitor<R, P, TH extends Throwable> extends
        DomainVisitor<R, P, TH> {

    R visitAbstractTimeDomain(AbstractTimeDomain<?> domain, P p) throws TH;

}

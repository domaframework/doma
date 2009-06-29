package org.seasar.doma.domain;

/**
 * @author taedium
 * 
 */
public interface AbstractTimestampDomainVisitor<R, P, TH extends Throwable>
        extends DomainVisitor<R, P, TH> {

    R visitAbstractTimestampDomain(AbstractTimestampDomain<?> domain, P p)
            throws TH;

}

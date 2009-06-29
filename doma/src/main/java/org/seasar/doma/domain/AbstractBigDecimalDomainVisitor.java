package org.seasar.doma.domain;

/**
 * @author taedium
 * 
 */
public interface AbstractBigDecimalDomainVisitor<R, P, TH extends Throwable> extends
        DomainVisitor<R, P, TH> {

    R visitAbstractBigDecimalDomain(AbstractBigDecimalDomain<?> domain, P p)
            throws TH;

}

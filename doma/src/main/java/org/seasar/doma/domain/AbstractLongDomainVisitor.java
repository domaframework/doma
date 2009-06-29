package org.seasar.doma.domain;

/**
 * @author taedium
 * 
 */
public interface AbstractLongDomainVisitor<R, P, TH extends Throwable> extends
        DomainVisitor<R, P, TH> {

    R visitAbstractLongDomain(AbstractLongDomain<?> domain, P p) throws TH;

}

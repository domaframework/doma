package org.seasar.doma.domain;


/**
 * @author taedium
 * 
 */
public interface BuiltInDomainVisitor<R, P, TH extends Throwable> extends
        DomainVisitor<R, P, TH>, AbstractBigDecimalDomainVisitor<R, P, TH>,
        AbstractDateDomainVisitor<R, P, TH>,
        AbstractIntegerDomainVisitor<R, P, TH>,
        AbstractStringDomainVisitor<R, P, TH>,
        AbstractTimeDomainVisitor<R, P, TH>,
        AbstractTimestampDomainVisitor<R, P, TH> {
}

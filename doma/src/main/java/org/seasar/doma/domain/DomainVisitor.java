package org.seasar.doma.domain;

/**
 * 
 * @author taedium
 * 
 * @param <P>
 * @param <TH>
 */
public interface DomainVisitor<R, P, TH extends Throwable> {

    R visitUnknownDomain(Domain<?, ?> domain, P p) throws TH;
}

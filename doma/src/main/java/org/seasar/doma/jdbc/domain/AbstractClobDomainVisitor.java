package org.seasar.doma.jdbc.domain;

import org.seasar.doma.domain.DomainVisitor;

/**
 * @author taedium
 * 
 */
public interface AbstractClobDomainVisitor<R, P, TH extends Throwable> extends
        DomainVisitor<R, P, TH> {

    R visitAbstractClobDomain(AbstractClobDomain domain, P p) throws TH;
}

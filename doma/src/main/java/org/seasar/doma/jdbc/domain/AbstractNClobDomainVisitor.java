package org.seasar.doma.jdbc.domain;

import org.seasar.doma.domain.DomainVisitor;

/**
 * @author taedium
 * 
 */
public interface AbstractNClobDomainVisitor<R, P, TH extends Throwable> extends
        DomainVisitor<R, P, TH> {

    R visitAbstractNClobDomain(AbstractNClobDomain domain, P p) throws TH;
}

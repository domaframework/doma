package org.seasar.doma.jdbc.domain;

import org.seasar.doma.domain.DomainVisitor;

/**
 * @author taedium
 * 
 */
public interface AbstractBlobDomainVisitor<R, P, TH extends Throwable> extends
        DomainVisitor<R, P, TH> {

    R visitAbstractBlobDomain(AbstractBlobDomain domain, P p) throws TH;
}

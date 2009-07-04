/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.jdbc.domain;

import java.sql.NClob;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.domain.AbstractDomain;
import org.seasar.doma.domain.DomainVisitor;

/**
 * @author taedium
 * 
 */
public abstract class AbstractNClobDomain<D extends AbstractNClobDomain<D>>
        extends AbstractDomain<NClob, D> {

    public AbstractNClobDomain() {
    }

    public AbstractNClobDomain(NClob v) {
        super(v);
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            DomainVisitor<R, P, TH> visitor, P p) throws TH {
        if (visitor == null) {
            throw new DomaIllegalArgumentException("visitor", visitor);
        }
        if (AbstractNClobDomainVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            AbstractNClobDomainVisitor<R, P, TH> v = AbstractNClobDomainVisitor.class
                    .cast(visitor);
            return v.visitAbstractNClobDomain(this, p);
        }
        return visitor.visitUnknownDomain(this, p);
    }

}

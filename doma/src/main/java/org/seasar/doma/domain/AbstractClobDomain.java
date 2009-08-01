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
package org.seasar.doma.domain;

import java.sql.Clob;

import org.seasar.doma.DomaNullPointerException;

/**
 * @author taedium
 * 
 */
public abstract class AbstractClobDomain<D extends AbstractClobDomain<D>>
        extends AbstractDomain<Clob, D> {

    protected AbstractClobDomain() {
        this(null);
    }

    protected AbstractClobDomain(Clob v) {
        super(Clob.class, v);
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            DomainVisitor<R, P, TH> visitor, P p) throws TH {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        if (AbstractClobDomainVisitor.class.isInstance(visitor)) {
            @SuppressWarnings("unchecked")
            AbstractClobDomainVisitor<R, P, TH> v = AbstractClobDomainVisitor.class
                    .cast(visitor);
            return v.visitAbstractClobDomain(this, p);
        }
        return visitor.visitUnknownDomain(this, p);
    }

}

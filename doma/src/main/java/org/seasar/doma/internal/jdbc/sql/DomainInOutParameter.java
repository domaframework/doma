/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.internal.wrapper.Holder;
import org.seasar.doma.jdbc.Reference;
import org.seasar.doma.jdbc.domain.DomainType;
import org.seasar.doma.wrapper.Wrapper;

/**
 * @author taedium
 * 
 */
public class DomainInOutParameter<V, D> implements InParameter, OutParameter<V> {

    protected final DomainType<V, D> domainType;

    protected final Reference<D> reference;

    protected final Holder<V, D> holder;

    public DomainInOutParameter(DomainType<V, D> domainType,
            Reference<D> reference) {
        assertNotNull(domainType, reference);
        this.domainType = domainType;
        this.reference = reference;
        this.holder = domainType.createDomainHolder();
        holder.set(reference.get());
    }

    @Override
    public Object getValue() {
        return reference.get();
    }

    @Override
    public Wrapper<V> getWrapper() {
        return holder.getWrapper();
    }

    @Override
    public void update() {
        reference.set(holder.get());
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            CallableSqlParameterVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitDomainInOutParameter(this, p);
    }

}

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
package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.ArrayList;
import java.util.List;

import org.seasar.doma.internal.domain.DomainType;
import org.seasar.doma.internal.domain.DomainTypeFactory;

/**
 * @author taedium
 * 
 */
public class DomainListResultParameter<V, D> implements
        ResultParameter<List<D>>, ListParameter<DomainType<V, D>> {

    protected final DomainTypeFactory<V, D> domainTypeFactory;

    protected final List<D> results = new ArrayList<D>();

    public DomainListResultParameter(DomainTypeFactory<V, D> domainTypeFactory) {
        assertNotNull(domainTypeFactory);
        this.domainTypeFactory = domainTypeFactory;
    }

    @Override
    public DomainType<V, D> getElementHolder() {
        return domainTypeFactory.createDomainType();
    }

    @Override
    public void putElementHolder(DomainType<V, D> domainType) {
        results.add(domainType.getDomain());
    }

    @Override
    public List<D> getResult() {
        return results;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            CallableSqlParameterVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitDomainListResultParameter(this, p);
    }

}

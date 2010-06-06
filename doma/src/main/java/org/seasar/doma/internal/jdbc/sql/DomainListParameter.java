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

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.List;

import org.seasar.doma.jdbc.domain.DomainType;
import org.seasar.doma.jdbc.domain.DomainWrapper;
import org.seasar.doma.wrapper.Wrapper;

/**
 * @author taedium
 * 
 */
public class DomainListParameter<V, D> implements ListParameter<Wrapper<V>, D> {

    protected final String name;

    protected final List<D> domains;

    protected final DomainType<V, D> domainType;

    public DomainListParameter(DomainType<V, D> domainType, List<D> domains,
            String name) {
        assertNotNull(domainType, domains, name);
        this.domainType = domainType;
        this.domains = domains;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Object getValue() {
        return domains;
    }

    public DomainWrapper<V, D> getWrapper() {
        return domainType.getWrapper(null);
    }

    @Override
    public void add(D domain) {
        domains.add(domain);
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            CallableSqlParameterVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitDomainListParameter(this, p);
    }

}

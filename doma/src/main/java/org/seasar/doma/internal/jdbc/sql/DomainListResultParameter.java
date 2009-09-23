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

import org.seasar.doma.internal.WrapException;
import org.seasar.doma.internal.util.ClassUtil;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.DomaMessageCode;
import org.seasar.doma.wrapper.Wrapper;

/**
 * @author taedium
 * 
 */
public class DomainListResultParameter<D extends Wrapper<?>> implements
        ResultParameter<List<D>>, ListParameter<D> {

    protected final Class<D> domainClass;

    protected final List<D> domains = new ArrayList<D>();

    public DomainListResultParameter(Class<D> domainClass) {
        assertNotNull(domainClass);
        this.domainClass = domainClass;
    }

    public D add() {
        D domain = createDomain();
        domains.add(domain);
        return domain;
    }

    protected D createDomain() {
        try {
            return ClassUtil.newInstance(domainClass);
        } catch (WrapException e) {
            Throwable cause = e.getCause();
            throw new JdbcException(DomaMessageCode.DOMA2006, cause,
                    domainClass.getName(), cause);
        }
    }

    @Override
    public List<D> getResult() {
        return domains;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            CallableSqlParameterVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitDomainListResultParameter(this, p);
    }
}

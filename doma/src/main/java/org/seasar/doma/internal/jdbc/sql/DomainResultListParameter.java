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

import java.util.ArrayList;
import java.util.List;

import org.seasar.doma.jdbc.domain.DomainType;

/**
 * @author taedium
 * 
 */
public class DomainResultListParameter<BASIC, DOMAIN> extends
        DomainListParameter<BASIC, DOMAIN> implements
        ResultListParameter<DOMAIN> {

    public DomainResultListParameter(DomainType<BASIC, DOMAIN> domainType) {
        super(domainType, new ArrayList<DOMAIN>(), "");
    }

    @Override
    public List<DOMAIN> getResult() {
        return list;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            CallableSqlParameterVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitDomainResultListParameter(this, p);
    }

}

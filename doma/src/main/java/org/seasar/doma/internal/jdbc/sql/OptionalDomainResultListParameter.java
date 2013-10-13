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
import java.util.Optional;

import org.seasar.doma.jdbc.domain.DomainType;

/**
 * @author taedium
 * 
 */
public class OptionalDomainResultListParameter<BASIC, DOMAIN> extends
        OptionalDomainListParameter<BASIC, DOMAIN> implements
        ResultListParameter<Optional<DOMAIN>> {

    public OptionalDomainResultListParameter(
            DomainType<BASIC, DOMAIN> domainType) {
        super(domainType, new ArrayList<Optional<DOMAIN>>(), "");
    }

    @Override
    public List<Optional<DOMAIN>> getResult() {
        return list;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            CallableSqlParameterVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitOptionalDomainResultListParameter(this, p);
    }

}

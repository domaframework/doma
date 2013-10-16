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

import org.seasar.doma.jdbc.Reference;
import org.seasar.doma.jdbc.domain.DomainType;

/**
 * @author taedium
 * 
 */
public class DomainOutParameter<BASIC, DOMAIN> extends
        AbstractOutParameter<BASIC, DOMAIN> {

    public DomainOutParameter(DomainType<BASIC, DOMAIN> domainType,
            Reference<DOMAIN> reference) {
        super(domainType.createHolder(), reference);
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            CallableSqlParameterVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitDomainOutParameter(this, p);
    }

}

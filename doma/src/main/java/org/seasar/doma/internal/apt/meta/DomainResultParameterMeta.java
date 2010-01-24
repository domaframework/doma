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
package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import org.seasar.doma.internal.apt.type.DomainType;

/**
 * @author taedium
 * 
 */
public class DomainResultParameterMeta implements ResultParameterMeta {

    protected final DomainType domainType;

    public DomainResultParameterMeta(DomainType domainType) {
        assertNotNull(domainType);
        this.domainType = domainType;
    }

    public DomainType getDomainType() {
        return domainType;
    }

    @Override
    public <R, P> R accept(CallableSqlParameterMetaVisitor<R, P> visitor, P p) {
        return visitor.visitDomainResultParameterMeta(this, p);
    }

}

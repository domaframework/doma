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

/**
 * @author taedium
 * 
 */
public interface CallableSqlParameterVisitor<R, P, TH extends Throwable> {

    R visitValueInParameter(ValueInParameter parameter, P p) throws TH;

    R visitDomainInParameter(DomainInParameter<?, ?> parameter, P p) throws TH;

    R visitValueOutParameter(ValueOutParameter<?> parameter, P p) throws TH;

    R visitDomainOutParameter(DomainOutParameter<?, ?> parameter, P p)
            throws TH;

    R visitValueInOutParameter(ValueInOutParameter<?> parameter, P p) throws TH;

    R visitDomainInOutParameter(DomainInOutParameter<?, ?> parameter, P p)
            throws TH;

    R visitValueListParameter(ValueListParameter<?> parameter, P p) throws TH;

    R visitDomainListParameter(DomainListParameter<?, ?> parameter, P p)
            throws TH;

    R visitEntityListParameter(EntityListParameter<?> parameter, P p) throws TH;

    R visitValueListResultParameter(ValueListResultParameter<?> parameter, P p)
            throws TH;

    R visitDomainListResultParameter(DomainListResultParameter<?, ?> parameter,
            P p) throws TH;

    R visitEntityListResultParameter(EntityListResultParameter<?> parameter, P p)
            throws TH;

    R visitValueResultParameter(ValueResultParameter<?> parameter, P p)
            throws TH;

    R visitDomainResultParameter(DomainResultParameter<?, ?> parameter, P p)
            throws TH;
}

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
package org.seasar.doma.internal.apt.meta;

/**
 * @author taedium
 * 
 */
public interface CallableSqlParameterMetaVisitor<R, P> {

    R visitValueInParameterMeta(ValueInParameterMeta m, P p);

    R visitDomainInParameterMeta(DomainInParameterMeta m, P p);

    R visistValueOutParameterMeta(ValueOutParameterMeta m, P p);

    R visistDomainOutParameterMeta(DomainOutParameterMeta m, P p);

    R visistValueInOutParameterMeta(ValueInOutParameterMeta m, P p);

    R visistDomainInOutParameterMeta(DomainInOutParameterMeta m, P p);

    R visistValueListParameterMeta(ValueListParameterMeta m, P p);

    R visistDomainListParameterMeta(DomainListParameterMeta m, P p);

    R visistEntityListParameterMeta(EntityListParameterMeta m, P p);

    R visistValueListResultParameterMeta(ValueListResultParameterMeta m, P p);

    R visistDomainListResultParameterMeta(DomainListResultParameterMeta m, P p);

    R visistEntityListResultParameterMeta(EntityListResultParameterMeta m, P p);

    R visistValueResultParameterMeta(ValueResultParameterMeta m, P p);

    R visistDomainResultParameterMeta(DomainResultParameterMeta m, P p);

}

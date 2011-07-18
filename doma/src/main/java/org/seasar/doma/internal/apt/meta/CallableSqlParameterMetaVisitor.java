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

/**
 * @author taedium
 * 
 */
public interface CallableSqlParameterMetaVisitor<R, P> {

    R visitBasicInParameterMeta(BasicInParameterMeta m, P p);

    R visitDomainInParameterMeta(DomainInParameterMeta m, P p);

    R visitBasicOutParameterMeta(BasicOutParameterMeta m, P p);

    R visitDomainOutParameterMeta(DomainOutParameterMeta m, P p);

    R visitBasicInOutParameterMeta(BasicInOutParameterMeta m, P p);

    R visitDomainInOutParameterMeta(DomainInOutParameterMeta m, P p);

    R visitBasicListParameterMeta(BasicListParameterMeta m, P p);

    R visitDomainListParameterMeta(DomainListParameterMeta m, P p);

    R visitEntityListParameterMeta(EntityListParameterMeta m, P p);

    R visitMapListParameterMeta(MapListParameterMeta m, P p);

    R visitBasicListResultParameterMeta(BasicListResultParameterMeta m, P p);

    R visitDomainListResultParameterMeta(DomainListResultParameterMeta m, P p);

    R visitEntityListResultParameterMeta(EntityListResultParameterMeta m, P p);

    R visitMapListResultParameterMeta(MapListResultParameterMeta m, P p);

    R visitBasicResultParameterMeta(BasicResultParameterMeta m, P p);

    R visitDomainResultParameterMeta(DomainResultParameterMeta m, P p);

}

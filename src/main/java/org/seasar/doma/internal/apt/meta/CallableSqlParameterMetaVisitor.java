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

    R visitBasicOutParameterMeta(BasicOutParameterMeta m, P p);

    R visitBasicInOutParameterMeta(BasicInOutParameterMeta m, P p);

    R visitBasicListParameterMeta(BasicListParameterMeta m, P p);

    R visitBasicSingleResultParameterMeta(BasicSingleResultParameterMeta m, P p);

    R visitBasicResultListParameterMeta(BasicResultListParameterMeta m, P p);

    R visitDomainInParameterMeta(DomainInParameterMeta m, P p);

    R visitDomainOutParameterMeta(DomainOutParameterMeta m, P p);

    R visitDomainInOutParameterMeta(DomainInOutParameterMeta m, P p);

    R visitDomainListParameterMeta(DomainListParameterMeta m, P p);

    R visitDomainSingleResultParameterMeta(DomainSingleResultParameterMeta m,
            P p);

    R visitDomainResultListParameterMeta(DomainResultListParameterMeta m, P p);

    R visitEntityListParameterMeta(EntityListParameterMeta m, P p);

    R visitEntityResultListParameterMeta(EntityResultListParameterMeta m, P p);

    R visitMapListParameterMeta(MapListParameterMeta m, P p);

    R visitMapResultListParameterMeta(MapResultListParameterMeta m, P p);

    R visitOptionalBasicInParameterMeta(OptionalBasicInParameterMeta m, P p);

    R visitOptionalBasicOutParameterMeta(OptionalBasicOutParameterMeta m, P p);

    R visitOptionalBasicInOutParameterMeta(OptionalBasicInOutParameterMeta m,
            P p);

    R visitOptionalBasicListParameterMeta(OptionalBasicListParameterMeta m, P p);

    R visitOptionalBasicSingleResultParameterMeta(
            OptionalBasicSingleResultParameterMeta m, P p);

    R visitOptionalBasicResultListParameterMeta(
            OptionalBasicResultListParameterMeta m, P p);

    R visitOptionalDomainInParameterMeta(OptionalDomainInParameterMeta m, P p);

    R visitOptionalDomainOutParameterMeta(OptionalDomainOutParameterMeta m, P p);

    R visitOptionalDomainInOutParameterMeta(OptionalDomainInOutParameterMeta m,
            P p);

    R visitOptionalDomainListParameterMeta(OptionalDomainListParameterMeta m,
            P p);

    R visitOptionalDomainSingleResultParameterMeta(
            OptionalDomainSingleResultParameterMeta m, P p);

    R visitOptionalDomainResultListParameterMeta(
            OptionalDomainResultListParameterMeta m, P p);
}

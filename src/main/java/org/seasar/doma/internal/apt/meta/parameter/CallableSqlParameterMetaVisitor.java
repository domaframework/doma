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
package org.seasar.doma.internal.apt.meta.parameter;

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

    R visitHolderInParameterMeta(HolderInParameterMeta m, P p);

    R visitHolderOutParameterMeta(HolderOutParameterMeta m, P p);

    R visitHolderInOutParameterMeta(HolderInOutParameterMeta m, P p);

    R visitHolderListParameterMeta(HolderListParameterMeta m, P p);

    R visitHolderSingleResultParameterMeta(HolderSingleResultParameterMeta m,
            P p);

    R visitHolderResultListParameterMeta(HolderResultListParameterMeta m, P p);

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

    R visitOptionalHolderInParameterMeta(OptionalHolderInParameterMeta m, P p);

    R visitOptionalHolderOutParameterMeta(OptionalHolderOutParameterMeta m, P p);

    R visitOptionalHolderInOutParameterMeta(OptionalHolderInOutParameterMeta m,
            P p);

    R visitOptionalHolderListParameterMeta(OptionalHolderListParameterMeta m,
            P p);

    R visitOptionalHolderSingleResultParameterMeta(
            OptionalHolderSingleResultParameterMeta m, P p);

    R visitOptionalHolderResultListParameterMeta(
            OptionalHolderResultListParameterMeta m, P p);

    R visitOptionalIntInParameterMeta(OptionalIntInParameterMeta m, P p);

    R visitOptionalIntOutParameterMeta(OptionalIntOutParameterMeta m, P p);

    R visitOptionalIntInOutParameterMeta(OptionalIntInOutParameterMeta m, P p);

    R visitOptionalIntListParameterMeta(OptionalIntListParameterMeta m, P p);

    R visitOptionalIntSingleResultParameterMeta(
            OptionalIntSingleResultParameterMeta m, P p);

    R visitOptionalIntResultListParameterMeta(
            OptionalIntResultListParameterMeta m, P p);

    R visitOptionalLongInParameterMeta(OptionalLongInParameterMeta m, P p);

    R visitOptionalLongOutParameterMeta(OptionalLongOutParameterMeta m, P p);

    R visitOptionalLongInOutParameterMeta(OptionalLongInOutParameterMeta m, P p);

    R visitOptionalLongListParameterMeta(OptionalLongListParameterMeta m, P p);

    R visitOptionalLongSingleResultParameterMeta(
            OptionalLongSingleResultParameterMeta m, P p);

    R visitOptionalLongResultListParameterMeta(
            OptionalLongResultListParameterMeta m, P p);

    R visitOptionalDoubleInParameterMeta(OptionalDoubleInParameterMeta m, P p);

    R visitOptionalDoubleOutParameterMeta(OptionalDoubleOutParameterMeta m, P p);

    R visitOptionalDoubleInOutParameterMeta(OptionalDoubleInOutParameterMeta m,
            P p);

    R visitOptionalDoubleListParameterMeta(OptionalDoubleListParameterMeta m,
            P p);

    R visitOptionalDoubleSingleResultParameterMeta(
            OptionalDoubleSingleResultParameterMeta m, P p);

    R visitOptionalDoubleResultListParameterMeta(
            OptionalDoubleResultListParameterMeta m, P p);

}

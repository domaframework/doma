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

    R visitScalarInParameterMeta(ScalarInParameterMeta m, P p);

    R visitScalarOutParameterMeta(ScalarOutParameterMeta m, P p);

    R visitScalarInOutParameterMeta(ScalarInOutParameterMeta m, P p);

    R visitScalarListParameterMeta(ScalarListParameterMeta m, P p);

    R visitScalarSingleResultParameterMeta(ScalarSingleResultParameterMeta m, P p);

    R visitScalarResultListParameterMeta(ScalarResultListParameterMeta m, P p);

    R visitEntityListParameterMeta(EntityListParameterMeta m, P p);

    R visitEntityResultListParameterMeta(EntityResultListParameterMeta m, P p);

    R visitMapListParameterMeta(MapListParameterMeta m, P p);

    R visitMapResultListParameterMeta(MapResultListParameterMeta m, P p);

}

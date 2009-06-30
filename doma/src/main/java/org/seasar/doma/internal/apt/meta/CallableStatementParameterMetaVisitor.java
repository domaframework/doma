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
public interface CallableStatementParameterMetaVisitor<R, P> {

    R visitInParameterMeta(InParameterMeta m, P p);

    R visistOutParameterMeta(OutParameterMeta m, P p);

    R visistInOutParameterMeta(InOutParameterMeta m, P p);

    R visistDomainListParameterMeta(DomainListParameterMeta m, P p);

    R visistEntityListParameterMeta(EntityListParameterMeta m, P p);

    R visistDomainResultParameterMeta(DomainResultParameterMeta m, P p);

    R visistDomainListResultParameterMeta(DomainListResultParameterMeta m, P p);

    R visistEntityListResultParameterMeta(EntityListResultParameterMeta m, P p);

}

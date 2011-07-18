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

/**
 * @author taedium
 * 
 */
public interface CallableSqlParameterVisitor<R, P, TH extends Throwable> {

    R visitBasicInParameter(BasicInParameter parameter, P p) throws TH;

    <V, D> R visitDomainInParameter(DomainInParameter<V, D> parameter, P p)
            throws TH;

    <V> R visitBasicOutParameter(BasicOutParameter<V> parameter, P p) throws TH;

    <V, D> R visitDomainOutParameter(DomainOutParameter<V, D> parameter, P p)
            throws TH;

    <V> R visitBasicInOutParameter(BasicInOutParameter<V> parameter, P p)
            throws TH;

    <V, D> R visitDomainInOutParameter(DomainInOutParameter<V, D> parameter, P p)
            throws TH;

    <V> R visitBasicListParameter(BasicListParameter<V> parameter, P p)
            throws TH;

    <V, D> R visitDomainListParameter(DomainListParameter<V, D> parameter, P p)
            throws TH;

    <E> R visitEntityListParameter(EntityListParameter<E> parameter, P p)
            throws TH;

    R visitMapListParameter(MapListParameter parameter, P p) throws TH;

    <V> R visitBasicListResultParameter(BasicListResultParameter<V> parameter,
            P p) throws TH;

    <V, D> R visitDomainListResultParameter(
            DomainListResultParameter<V, D> parameter, P p) throws TH;

    <E> R visitEntityListResultParameter(
            EntityListResultParameter<E> parameter, P p) throws TH;

    R visitMapListResultParameter(MapListResultParameter parameter, P p)
            throws TH;

    <V> R visitBasicResultParameter(BasicResultParameter<V> parameter, P p)
            throws TH;

    <V, D> R visitDomainResultParameter(DomainResultParameter<V, D> parameter,
            P p) throws TH;
}

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

    <BASIC> R visitBasicInParameter(BasicInParameter<BASIC> parameter, P p)
            throws TH;

    <BASIC> R visitBasicOutParameter(BasicOutParameter<BASIC> parameter, P p)
            throws TH;

    <BASIC> R visitBasicInOutParameter(BasicInOutParameter<BASIC> parameter, P p)
            throws TH;

    <BASIC> R visitBasicListParameter(BasicListParameter<BASIC> parameter, P p)
            throws TH;

    <BASIC> R visitBasicSingleResultParameter(
            BasicSingleResultParameter<BASIC> parameter, P p) throws TH;

    <BASIC> R visitBasicResultListParameter(
            BasicResultListParameter<BASIC> parameter, P p) throws TH;

    <BASIC, DOMAIN> R visitDomainInParameter(
            DomainInParameter<BASIC, DOMAIN> parameter, P p) throws TH;

    <BASIC, DOMAIN> R visitDomainOutParameter(
            DomainOutParameter<BASIC, DOMAIN> parameter, P p) throws TH;

    <BASIC, DOMAIN> R visitDomainInOutParameter(
            DomainInOutParameter<BASIC, DOMAIN> parameter, P p) throws TH;

    <BASIC, DOMAIN> R visitDomainListParameter(
            DomainListParameter<BASIC, DOMAIN> parameter, P p) throws TH;

    <BASIC, DOMAIN> R visitDomainSingleResultParameter(
            DomainSingleResultParameter<BASIC, DOMAIN> parameter, P p)
            throws TH;

    <BASIC, DOMAIN> R visitDomainResultListParameter(
            DomainResultListParameter<BASIC, DOMAIN> parameter, P p) throws TH;

    <ENTITY> R visitEntityListParameter(EntityListParameter<ENTITY> parameter,
            P p) throws TH;

    <ENTITY> R visitEntityResultListParameter(
            EntityResultListParameter<ENTITY> parameter, P p) throws TH;

    R visitMapListParameter(MapListParameter parameter, P p) throws TH;

    R visitMapResultListParameter(MapResultListParameter parameter, P p)
            throws TH;

    <BASIC> R visitOptionalBasicInParameter(
            OptionalBasicInParameter<BASIC> parameter, P p) throws TH;

    <BASIC> R visitOptionalBasicOutParameter(
            OptionalBasicOutParameter<BASIC> parameter, P p) throws TH;

    <BASIC> R visitOptionalBasicInOutParameter(
            OptionalBasicInOutParameter<BASIC> parameter, P p) throws TH;

    <BASIC> R visitOptionalBasicListParameter(
            OptionalBasicListParameter<BASIC> parameter, P p) throws TH;

    <BASIC> R visitOptionalBasicResultListParameter(
            OptionalBasicResultListParameter<BASIC> parameter, P p) throws TH;

    <BASIC> R visitOptionalBasicSingleResultParameter(
            OptionalBasicSingleResultParameter<BASIC> parameter, P p) throws TH;

    <BASIC, DOMAIN> R visitOptionalDomainInParameter(
            OptionalDomainInParameter<BASIC, DOMAIN> parameter, P p) throws TH;

    <BASIC, DOMAIN> R visitOptionalDomainOutParameter(
            OptionalDomainOutParameter<BASIC, DOMAIN> parameter, P p) throws TH;

    <BASIC, DOMAIN> R visitOptionalDomainInOutParameter(
            OptionalDomainInOutParameter<BASIC, DOMAIN> parameter, P p)
            throws TH;

    <BASIC, DOMAIN> R visitOptionalDomainListParameter(
            OptionalDomainListParameter<BASIC, DOMAIN> parameter, P p)
            throws TH;

    <BASIC, DOMAIN> R visitOptionalDomainSingleResultParameter(
            OptionalDomainSingleResultParameter<BASIC, DOMAIN> parameter, P p)
            throws TH;

    <BASIC, DOMAIN> R visitOptionalDomainResultListParameter(
            OptionalDomainResultListParameter<BASIC, DOMAIN> parameter, P p)
            throws TH;
}

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

    <BASIC> R visitInParameter(InParameter<BASIC> parameter, P p) throws TH;

    <BASIC> R visitOutParameter(OutParameter<BASIC> parameter, P p) throws TH;

    <BASIC, INOUT extends InParameter<BASIC> & OutParameter<BASIC>> R visitInOutParameter(
            INOUT parameter, P p) throws TH;

    <ELEMENT> R visitListParameter(ListParameter<ELEMENT> parameter, P p)
            throws TH;

    <BASIC, RESULT> R visitSingleResultParameter(
            SingleResultParameter<BASIC, RESULT> parameter, P p) throws TH;

    <ELEMENT> R visitResultListParameter(
            ResultListParameter<ELEMENT> parameter, P p) throws TH;

    default <BASIC> R visitBasicInParameter(BasicInParameter<BASIC> parameter,
            P p) throws TH {
        return visitInParameter(parameter, p);
    }

    default <BASIC> R visitBasicOutParameter(
            BasicOutParameter<BASIC> parameter, P p) throws TH {
        return visitOutParameter(parameter, p);
    }

    default <BASIC> R visitBasicInOutParameter(
            BasicInOutParameter<BASIC> parameter, P p) throws TH {
        return visitInOutParameter(parameter, p);
    }

    default <BASIC> R visitBasicListParameter(
            BasicListParameter<BASIC> parameter, P p) throws TH {
        return visitListParameter(parameter, p);
    }

    default <BASIC> R visitBasicSingleResultParameter(
            BasicSingleResultParameter<BASIC> parameter, P p) throws TH {
        return visitSingleResultParameter(parameter, p);
    }

    default <BASIC> R visitBasicResultListParameter(
            BasicResultListParameter<BASIC> parameter, P p) throws TH {
        return visitResultListParameter(parameter, p);
    }

    default <BASIC, DOMAIN> R visitDomainInParameter(
            DomainInParameter<BASIC, DOMAIN> parameter, P p) throws TH {
        return visitInParameter(parameter, p);
    }

    default <BASIC, DOMAIN> R visitDomainOutParameter(
            DomainOutParameter<BASIC, DOMAIN> parameter, P p) throws TH {
        return visitOutParameter(parameter, p);
    }

    default <BASIC, DOMAIN> R visitDomainInOutParameter(
            DomainInOutParameter<BASIC, DOMAIN> parameter, P p) throws TH {
        return visitInOutParameter(parameter, p);
    }

    default <BASIC, DOMAIN> R visitDomainListParameter(
            DomainListParameter<BASIC, DOMAIN> parameter, P p) throws TH {
        return visitListParameter(parameter, p);
    }

    default <BASIC, DOMAIN> R visitDomainSingleResultParameter(
            DomainSingleResultParameter<BASIC, DOMAIN> parameter, P p)
            throws TH {
        return visitSingleResultParameter(parameter, p);
    }

    default <BASIC, DOMAIN> R visitDomainResultListParameter(
            DomainResultListParameter<BASIC, DOMAIN> parameter, P p) throws TH {
        return visitResultListParameter(parameter, p);
    }

    default <ENTITY> R visitEntityListParameter(
            EntityListParameter<ENTITY> parameter, P p) throws TH {
        return visitListParameter(parameter, p);
    }

    default <ENTITY> R visitEntityResultListParameter(
            EntityResultListParameter<ENTITY> parameter, P p) throws TH {
        return visitResultListParameter(parameter, p);
    }

    default R visitMapListParameter(MapListParameter parameter, P p) throws TH {
        return visitListParameter(parameter, p);
    }

    default R visitMapResultListParameter(MapResultListParameter parameter, P p)
            throws TH {
        return visitResultListParameter(parameter, p);
    }

    default <BASIC> R visitOptionalBasicInParameter(
            OptionalBasicInParameter<BASIC> parameter, P p) throws TH {
        return visitInParameter(parameter, p);
    }

    default <BASIC> R visitOptionalBasicOutParameter(
            OptionalBasicOutParameter<BASIC> parameter, P p) throws TH {
        return visitOutParameter(parameter, p);
    }

    default <BASIC> R visitOptionalBasicInOutParameter(
            OptionalBasicInOutParameter<BASIC> parameter, P p) throws TH {
        return visitInOutParameter(parameter, p);
    }

    default <BASIC> R visitOptionalBasicListParameter(
            OptionalBasicListParameter<BASIC> parameter, P p) throws TH {
        return visitListParameter(parameter, p);
    }

    default <BASIC> R visitOptionalBasicResultListParameter(
            OptionalBasicResultListParameter<BASIC> parameter, P p) throws TH {
        return visitResultListParameter(parameter, p);
    }

    default <BASIC> R visitOptionalBasicSingleResultParameter(
            OptionalBasicSingleResultParameter<BASIC> parameter, P p) throws TH {
        return visitSingleResultParameter(parameter, p);
    }

    default <BASIC, DOMAIN> R visitOptionalDomainInParameter(
            OptionalDomainInParameter<BASIC, DOMAIN> parameter, P p) throws TH {
        return visitInParameter(parameter, p);
    }

    default <BASIC, DOMAIN> R visitOptionalDomainOutParameter(
            OptionalDomainOutParameter<BASIC, DOMAIN> parameter, P p) throws TH {
        return visitOutParameter(parameter, p);
    }

    default <BASIC, DOMAIN> R visitOptionalDomainInOutParameter(
            OptionalDomainInOutParameter<BASIC, DOMAIN> parameter, P p)
            throws TH {
        return visitInOutParameter(parameter, p);
    }

    default <BASIC, DOMAIN> R visitOptionalDomainListParameter(
            OptionalDomainListParameter<BASIC, DOMAIN> parameter, P p)
            throws TH {
        return visitListParameter(parameter, p);
    }

    default <BASIC, DOMAIN> R visitOptionalDomainSingleResultParameter(
            OptionalDomainSingleResultParameter<BASIC, DOMAIN> parameter, P p)
            throws TH {
        return visitSingleResultParameter(parameter, p);
    }

    default <BASIC, DOMAIN> R visitOptionalDomainResultListParameter(
            OptionalDomainResultListParameter<BASIC, DOMAIN> parameter, P p)
            throws TH {
        return visitResultListParameter(parameter, p);
    }

}

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
package org.seasar.doma.internal.apt.type;

/**
 * @author taedium
 * 
 */
public interface DataTypeVisitor<R, P, TH extends Throwable> {

    R visitAnyType(AnyType dataType, P p) throws TH;

    R visitBasicType(BasicType dataType, P p) throws TH;

    R visitDomainType(DomainType dataType, P p) throws TH;

    R visitEntityType(EntityType dataType, P p) throws TH;

    R visitIterationCallbackType(IterationCallbackType dataType, P p) throws TH;

    R visitIterableType(IterableType dataType, P p) throws TH;

    R visitReferenceType(ReferenceType dataType, P p) throws TH;

    R visitSelectOptionsType(SelectOptionsType dataType, P p) throws TH;

    R visitWrapperType(WrapperType dataType, P p) throws TH;

    R visitEnumWrapperType(EnumWrapperType dataType, P p) throws TH;

    R visitMapType(MapType dataType, P p) throws TH;

}

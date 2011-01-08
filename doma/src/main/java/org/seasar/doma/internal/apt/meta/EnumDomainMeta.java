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

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.mirror.EnumDomainMirror;

public class EnumDomainMeta extends DomainMeta {

    protected EnumDomainMirror enumDomainMirror;

    public EnumDomainMeta(TypeElement typeElement, TypeMirror type) {
        super(typeElement, type);
    }

    @Override
    public TypeMirror getValueType() {
        return enumDomainMirror.getValueTypeValue();
    }

    @Override
    public String getFactoryMethod() {
        return enumDomainMirror.getFactoryMethodValue();
    }

    @Override
    public String getAccessorMethod() {
        return enumDomainMirror.getAccessorMethodValue();
    }

    EnumDomainMirror getEnumDomainMirror() {
        return enumDomainMirror;
    }

    void setEnumDomainMirror(EnumDomainMirror enumDomainMirror) {
        this.enumDomainMirror = enumDomainMirror;
    }

}

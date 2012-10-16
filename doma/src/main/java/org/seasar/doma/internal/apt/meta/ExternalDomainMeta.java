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

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.lang.model.element.TypeElement;

import org.seasar.doma.internal.apt.type.WrapperType;

/**
 * @author taedium
 * 
 */
public class ExternalDomainMeta implements TypeElementMeta {

    protected final TypeElement typeElement;

    protected WrapperType wrapperType;

    protected TypeElement valueElement;

    protected TypeElement domainElement;

    public ExternalDomainMeta(TypeElement typeElement) {
        assertNotNull(typeElement);
        this.typeElement = typeElement;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public WrapperType getWrapperType() {
        return wrapperType;
    }

    public void setWrapperType(WrapperType wrapperType) {
        this.wrapperType = wrapperType;
    }

    public TypeElement getValueElement() {
        return valueElement;
    }

    public void setValueElement(TypeElement valueElement) {
        this.valueElement = valueElement;
    }

    public TypeElement getDomainElement() {
        return domainElement;
    }

    public void setDomainElement(TypeElement domainElement) {
        this.domainElement = domainElement;
    }

    @Override
    public boolean isError() {
        return false;
    }

}

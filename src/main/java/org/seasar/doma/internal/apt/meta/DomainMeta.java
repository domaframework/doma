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

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.WrapperCtType;
import org.seasar.doma.internal.apt.mirror.DomainMirror;

public class DomainMeta implements TypeElementMeta {

    protected final TypeElement typeElement;

    protected final TypeMirror type;

    protected final boolean parametarized;

    protected BasicCtType basicCtType;

    protected WrapperCtType wrapperCtType;

    protected DomainMirror domainMirror;

    protected String simpleTypeName;

    public DomainMeta(TypeElement typeElement, TypeMirror type) {
        assertNotNull(typeElement, type);
        this.typeElement = typeElement;
        this.type = type;
        this.parametarized = !typeElement.getTypeParameters().isEmpty();
    }

    public TypeMirror getType() {
        return type;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public BasicCtType getBasicCtType() {
        return basicCtType;
    }

    public void setBasicCtType(BasicCtType basicCtType) {
        this.basicCtType = basicCtType;
    }

    public WrapperCtType getWrapperCtType() {
        return wrapperCtType;
    }

    public void setWrapperCtType(WrapperCtType wrapperCtType) {
        this.wrapperCtType = wrapperCtType;
    }

    public TypeMirror getValueType() {
        return domainMirror.getValueTypeValue();
    }

    public String getFactoryMethod() {
        return domainMirror.getFactoryMethodValue();
    }

    public String getAccessorMethod() {
        return domainMirror.getAccessorMethodValue();
    }

    public boolean getAcceptNull() {
        return domainMirror.getAcceptNullValue();
    }

    DomainMirror getDomainMirror() {
        return domainMirror;
    }

    void setDomainMirror(DomainMirror domainMirror) {
        this.domainMirror = domainMirror;
    }

    public boolean providesConstructor() {
        return "new".equals(domainMirror.getFactoryMethodValue());
    }

    public boolean isParametarized() {
        return parametarized;
    }

    @Override
    public boolean isError() {
        return false;
    }

}

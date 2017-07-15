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
package org.seasar.doma.internal.apt.meta.holder;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.meta.TypeElementMeta;
import org.seasar.doma.internal.apt.reflection.HolderReflection;

public class HolderMeta implements TypeElementMeta {

    private final TypeElement holderElement;

    private final TypeMirror type;

    private final boolean parametarized;

    private final BasicCtType basicCtType;

    private final HolderReflection holderReflection;

    public HolderMeta(TypeElement typeElement, TypeMirror type, HolderReflection holderReflection,
            BasicCtType basicCtType) {
        assertNotNull(typeElement, type, holderReflection, basicCtType);
        this.holderElement = typeElement;
        this.type = type;
        this.parametarized = !typeElement.getTypeParameters().isEmpty();
        this.holderReflection = holderReflection;
        this.basicCtType = basicCtType;
    }

    public TypeMirror getType() {
        return type;
    }

    public TypeElement getHolderElement() {
        return holderElement;
    }

    public BasicCtType getBasicCtType() {
        return basicCtType;
    }

    public TypeMirror getValueType() {
        return holderReflection.getValueTypeValue();
    }

    public String getFactoryMethod() {
        return holderReflection.getFactoryMethodValue();
    }

    public String getAccessorMethod() {
        return holderReflection.getAccessorMethodValue();
    }

    public boolean getAcceptNull() {
        return holderReflection.getAcceptNullValue();
    }

    public HolderReflection getHolderReflection() {
        return holderReflection;
    }

    public boolean providesConstructor() {
        return "new".equals(holderReflection.getFactoryMethodValue());
    }

    public boolean isParametarized() {
        return parametarized;
    }

}

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

import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.meta.CanonicalName;
import org.seasar.doma.internal.apt.meta.TypeElementMeta;

/**
 * @author taedium
 * 
 */
public class ExternalHolderMeta implements TypeElementMeta {

    private final TypeElement typeElement;

    private final TypeElement holderElement;

    private final BasicCtType basicCtType;

    private final CanonicalName externalHolderDescCanonicalName;

    public ExternalHolderMeta(TypeElement typeElement, TypeElement holderElement,
            BasicCtType basicCtType, CanonicalName externalHolderDescCanonicalName) {
        assertNotNull(typeElement, holderElement, basicCtType, externalHolderDescCanonicalName);
        this.typeElement = typeElement;
        this.holderElement = holderElement;
        this.basicCtType = basicCtType;
        this.externalHolderDescCanonicalName = externalHolderDescCanonicalName;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public String getValueTypeName() {
        return basicCtType.getTypeName();
    }

    public TypeElement getHolderElement() {
        return holderElement;
    }

    public BasicCtType getBasicCtType() {
        return basicCtType;
    }

    public CanonicalName getExternalHolderDescCanonicalName() {
        return externalHolderDescCanonicalName;
    }

}

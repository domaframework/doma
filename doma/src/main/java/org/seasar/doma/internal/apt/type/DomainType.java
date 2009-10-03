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
package org.seasar.doma.internal.apt.type;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.Domain;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.TypeUtil;

public class DomainType {

    protected TypeMirror type;

    protected String typeName;

    protected ValueType valueType;

    protected String accessorMetod;

    protected DomainType() {
    }

    public TypeMirror getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public String getAccessorMetod() {
        return accessorMetod;
    }

    public static DomainType newInstance(TypeMirror type,
            ProcessingEnvironment env) {
        assertNotNull(type, env);
        TypeElement typeElement = TypeUtil.toTypeElement(type, env);
        if (typeElement == null) {
            return null;
        }
        Domain domain = typeElement.getAnnotation(Domain.class);
        if (domain == null) {
            return null;
        }
        TypeMirror valueTypeMirror = getValueType(domain);
        ValueType valueType = ValueType.newInstance(valueTypeMirror, env);
        if (valueType == null) {
            return null;
        }
        DomainType domainType = new DomainType();
        domainType.type = type;
        domainType.typeName = TypeUtil.getTypeName(type, env);
        domainType.valueType = valueType;
        domainType.accessorMetod = domain.accessorMethod();
        return domainType;
    }

    protected static TypeMirror getValueType(Domain domain) {
        try {
            domain.valueType();
        } catch (MirroredTypeException e) {
            return e.getTypeMirror();
        }
        throw new AptIllegalStateException("unreachable.");
    }
}

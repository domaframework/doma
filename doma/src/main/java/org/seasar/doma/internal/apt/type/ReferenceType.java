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

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.TypeUtil;
import org.seasar.doma.jdbc.Reference;

public class ReferenceType {

    protected TypeMirror type;

    protected String typeName;

    protected TypeMirror referentType;

    protected DomainType referentDomainType;

    protected BasicType referentBasicType;

    protected ReferenceType() {
    }

    public TypeMirror getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }

    public DomainType getReferentDomainType() {
        return referentDomainType;
    }

    public BasicType getReferentBasicType() {
        return referentBasicType;
    }

    public TypeMirror getReferentType() {
        return referentType;
    }

    public boolean isRaw() {
        return referentType == null;
    }

    public boolean isWildcardType() {
        return referentType != null
                && referentType.getKind() == TypeKind.WILDCARD;
    }

    public static ReferenceType newInstance(TypeMirror type,
            ProcessingEnvironment env) {
        assertNotNull(type, env);
        DeclaredType referenceDeclaredType = getReferenceDeclaredType(type, env);
        if (referenceDeclaredType == null) {
            return null;
        }
        ReferenceType referenceType = new ReferenceType();
        referenceType.type = type;
        referenceType.typeName = TypeUtil.getTypeName(type, env);
        List<? extends TypeMirror> typeArguments = referenceDeclaredType
                .getTypeArguments();
        if (typeArguments.size() == 1) {
            referenceType.referentType = typeArguments.get(0);
            referenceType.referentDomainType = DomainType.newInstance(
                    referenceType.referentType, env);
            if (referenceType.referentDomainType == null) {
                referenceType.referentBasicType = BasicType.newInstance(
                        referenceType.referentType, env);
            }
        }
        return referenceType;
    }

    protected static DeclaredType getReferenceDeclaredType(TypeMirror type,
            ProcessingEnvironment env) {
        if (TypeUtil.isSameType(type, Reference.class, env)) {
            return TypeUtil.toDeclaredType(type, env);
        }
        for (TypeMirror supertype : env.getTypeUtils().directSupertypes(type)) {
            if (TypeUtil.isSameType(supertype, Reference.class, env)) {
                return TypeUtil.toDeclaredType(supertype, env);
            }
            getReferenceDeclaredType(supertype, env);
        }
        return null;
    }
}

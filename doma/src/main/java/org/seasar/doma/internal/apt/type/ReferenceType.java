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

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.jdbc.Reference;

public class ReferenceType extends AbstractDataType {

    protected TypeMirror referentTypeMirror;

    protected DataType referentType;

    public ReferenceType(TypeMirror type, ProcessingEnvironment env) {
        super(type, env);
    }

    public DataType getReferentType() {
        return referentType;
    }

    public TypeMirror getReferentTypeMirror() {
        return referentTypeMirror;
    }

    public boolean isRaw() {
        return referentTypeMirror == null;
    }

    public boolean isWildcardType() {
        return referentTypeMirror != null
                && referentTypeMirror.getKind() == TypeKind.WILDCARD;
    }

    public static ReferenceType newInstance(TypeMirror type,
            ProcessingEnvironment env) {
        assertNotNull(type, env);
        DeclaredType referenceDeclaredType = getReferenceDeclaredType(type, env);
        if (referenceDeclaredType == null) {
            return null;
        }
        ReferenceType referenceType = new ReferenceType(type, env);
        List<? extends TypeMirror> typeArguments = referenceDeclaredType
                .getTypeArguments();
        if (typeArguments.size() == 1) {
            referenceType.referentTypeMirror = typeArguments.get(0);
            referenceType.referentType = DomainType.newInstance(
                    referenceType.referentTypeMirror, env);
            if (referenceType.referentType == null) {
                referenceType.referentType = BasicType.newInstance(
                        referenceType.referentTypeMirror, env);
                if (referenceType.referentType == null) {
                    referenceType.referentType = AnyType.newInstance(
                            referenceType.referentTypeMirror, env);
                }
            }
        }
        return referenceType;
    }

    protected static DeclaredType getReferenceDeclaredType(TypeMirror type,
            ProcessingEnvironment env) {
        if (TypeMirrorUtil.isSameType(type, Reference.class, env)) {
            return TypeMirrorUtil.toDeclaredType(type, env);
        }
        for (TypeMirror supertype : env.getTypeUtils().directSupertypes(type)) {
            if (TypeMirrorUtil.isSameType(supertype, Reference.class, env)) {
                return TypeMirrorUtil.toDeclaredType(supertype, env);
            }
            getReferenceDeclaredType(supertype, env);
        }
        return null;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            DataTypeVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitReferenceType(this, p);
    }
}

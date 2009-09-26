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
import org.seasar.doma.jdbc.IterationCallback;

public class IterationCallbackType {

    protected TypeMirror type;

    protected String typeName;

    protected TypeMirror resultType;

    protected TypeMirror targetType;

    protected AnyType returnType;

    protected EntityType entityType;

    protected DomainType domainType;

    protected ValueType valueType;

    protected boolean rawType;

    protected IterationCallbackType() {
    }

    public TypeMirror getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }

    public AnyType getReturnType() {
        return returnType;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public DomainType getDomainType() {
        return domainType;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public boolean isRawType() {
        return resultType == null || targetType == null;
    }

    public boolean isWildcardType() {
        return resultType != null && resultType.getKind() == TypeKind.WILDCARD
                || targetType != null
                && targetType.getKind() == TypeKind.WILDCARD;
    }

    public static IterationCallbackType newInstance(TypeMirror type,
            ProcessingEnvironment env) {
        assertNotNull(type, env);
        DeclaredType iterationCallbackDeclaredType = getIterationCallbackDeclaredType(
                type, env);
        if (iterationCallbackDeclaredType == null) {
            return null;
        }

        IterationCallbackType callbackType = new IterationCallbackType();
        callbackType.type = type;
        callbackType.typeName = TypeUtil.getTypeName(type, env);
        List<? extends TypeMirror> typeArguments = iterationCallbackDeclaredType
                .getTypeArguments();
        if (typeArguments.size() == 2) {
            callbackType.resultType = typeArguments.get(0);
            callbackType.targetType = typeArguments.get(1);

            callbackType.returnType = AnyType.newInstance(
                    callbackType.resultType, env);
            callbackType.entityType = EntityType.newInstance(
                    callbackType.targetType, env);
            if (callbackType.entityType == null) {
                callbackType.domainType = DomainType.newInstance(
                        callbackType.targetType, env);
                if (callbackType.domainType == null) {
                    callbackType.valueType = ValueType.newInstance(
                            callbackType.targetType, env);
                }
            }
        }

        return callbackType;
    }

    protected static DeclaredType getIterationCallbackDeclaredType(
            TypeMirror type, ProcessingEnvironment env) {
        if (TypeUtil.isSameType(type, IterationCallback.class, env)) {
            return TypeUtil.toDeclaredType(type, env);
        }
        for (TypeMirror supertype : env.getTypeUtils().directSupertypes(type)) {
            if (TypeUtil.isSameType(supertype, IterationCallback.class, env)) {
                return TypeUtil.toDeclaredType(supertype, env);
            }
            getIterationCallbackDeclaredType(supertype, env);
        }
        return null;
    }
}

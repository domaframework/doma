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
import org.seasar.doma.jdbc.IterationCallback;

public class IterationCallbackType extends AbstractDataType {

    protected TypeMirror returnTypeMirror;

    protected TypeMirror targetTypeMirror;

    protected AnyType returnType;

    protected DataType targetType;

    protected boolean rawType;

    public IterationCallbackType(TypeMirror type, ProcessingEnvironment env) {
        super(type, env);
    }

    public AnyType getReturnType() {
        return returnType;
    }

    public DataType getTargetType() {
        return targetType;
    }

    public boolean isRawType() {
        return returnTypeMirror == null || targetTypeMirror == null;
    }

    public boolean isWildcardType() {
        return returnTypeMirror != null
                && returnTypeMirror.getKind() == TypeKind.WILDCARD
                || targetTypeMirror != null
                && targetTypeMirror.getKind() == TypeKind.WILDCARD;
    }

    public static IterationCallbackType newInstance(TypeMirror type,
            ProcessingEnvironment env) {
        assertNotNull(type, env);
        DeclaredType iterationCallbackDeclaredType = getIterationCallbackDeclaredType(
                type, env);
        if (iterationCallbackDeclaredType == null) {
            return null;
        }

        IterationCallbackType callbackType = new IterationCallbackType(type,
                env);
        List<? extends TypeMirror> typeArguments = iterationCallbackDeclaredType
                .getTypeArguments();
        if (typeArguments.size() == 2) {
            callbackType.returnTypeMirror = typeArguments.get(0);
            callbackType.targetTypeMirror = typeArguments.get(1);

            callbackType.returnType = AnyType.newInstance(
                    callbackType.returnTypeMirror, env);
            callbackType.targetType = EntityType.newInstance(
                    callbackType.targetTypeMirror, env);
            if (callbackType.targetType == null) {
                callbackType.targetType = DomainType.newInstance(
                        callbackType.targetTypeMirror, env);
                if (callbackType.targetType == null) {
                    callbackType.targetType = BasicType.newInstance(
                            callbackType.targetTypeMirror, env);
                    if (callbackType.targetType == null) {
                        callbackType.targetType = MapType.newInstance(
                                callbackType.targetTypeMirror, env);
                        if (callbackType.targetType == null) {
                            callbackType.targetType = AnyType.newInstance(
                                    callbackType.targetTypeMirror, env);
                        }
                    }
                }
            }
        }

        return callbackType;
    }

    protected static DeclaredType getIterationCallbackDeclaredType(
            TypeMirror type, ProcessingEnvironment env) {
        if (TypeMirrorUtil.isSameType(type, IterationCallback.class, env)) {
            return TypeMirrorUtil.toDeclaredType(type, env);
        }
        for (TypeMirror supertype : env.getTypeUtils().directSupertypes(type)) {
            if (TypeMirrorUtil.isSameType(supertype, IterationCallback.class,
                    env)) {
                return TypeMirrorUtil.toDeclaredType(supertype, env);
            }
            DeclaredType result = getIterationCallbackDeclaredType(supertype,
                    env);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            DataTypeVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitIterationCallbackType(this, p);
    }
}

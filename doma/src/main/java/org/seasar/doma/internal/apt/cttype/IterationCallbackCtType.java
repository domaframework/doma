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
package org.seasar.doma.internal.apt.cttype;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.jdbc.IterationCallback;

public class IterationCallbackCtType extends AbstractCtType {

    protected AnyCtType returnCtType;

    protected CtType targetCtType;

    public IterationCallbackCtType(TypeMirror type, ProcessingEnvironment env) {
        super(type, env);
    }

    public AnyCtType getReturnType() {
        return returnCtType;
    }

    public CtType getTargetType() {
        return targetCtType;
    }

    public boolean isRawType() {
        return returnCtType.getTypeMirror() == null
                || targetCtType.getTypeMirror() == null;
    }

    public boolean isWildcardType() {
        return returnCtType.getTypeMirror() != null
                && returnCtType.getTypeMirror().getKind() == TypeKind.WILDCARD
                || targetCtType.getTypeMirror() != null
                && targetCtType.getTypeMirror().getKind() == TypeKind.WILDCARD;
    }

    public static IterationCallbackCtType newInstance(TypeMirror type,
            ProcessingEnvironment env) {
        assertNotNull(type, env);
        DeclaredType iterationCallbackDeclaredType = getIterationCallbackDeclaredType(
                type, env);
        if (iterationCallbackDeclaredType == null) {
            return null;
        }

        IterationCallbackCtType callbackType = new IterationCallbackCtType(
                type, env);
        List<? extends TypeMirror> typeArguments = iterationCallbackDeclaredType
                .getTypeArguments();
        if (typeArguments.size() == 2) {
            TypeMirror returnTypeMirror = typeArguments.get(0);
            TypeMirror targetTypeMirror = typeArguments.get(1);

            callbackType.returnCtType = AnyCtType.newInstance(returnTypeMirror,
                    env);
            callbackType.targetCtType = EntityCtType.newInstance(
                    targetTypeMirror, env);
            if (callbackType.targetCtType == null) {
                callbackType.targetCtType = DomainCtType.newInstance(
                        targetTypeMirror, env);
                if (callbackType.targetCtType == null) {
                    callbackType.targetCtType = BasicCtType.newInstance(
                            targetTypeMirror, env);
                    if (callbackType.targetCtType == null) {
                        callbackType.targetCtType = MapCtType.newInstance(
                                targetTypeMirror, env);
                        if (callbackType.targetCtType == null) {
                            callbackType.targetCtType = AnyCtType.newInstance(
                                    targetTypeMirror, env);
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
            CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitIterationCallbackCtType(this, p);
    }
}

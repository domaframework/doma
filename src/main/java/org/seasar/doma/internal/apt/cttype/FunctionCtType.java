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
import java.util.function.Function;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.util.TypeMirrorUtil;

public class FunctionCtType extends AbstractCtType {

    protected CtType targetCtType;

    protected AnyCtType returnCtType;

    public FunctionCtType(TypeMirror type, ProcessingEnvironment env) {
        super(type, env);
    }

    public CtType getTargetCtType() {
        return targetCtType;
    }

    public AnyCtType getReturnCtType() {
        return returnCtType;
    }

    public boolean isRawType() {
        return returnCtType.getTypeMirror() == null
                || targetCtType.getTypeMirror() == null;
    }

    public boolean isWildcardType() {
        return returnCtType.getTypeMirror() != null
                && returnCtType.getTypeMirror().getKind() == TypeKind.WILDCARD
                || targetCtType.getTypeMirror() != null && targetCtType
                        .getTypeMirror().getKind() == TypeKind.WILDCARD;
    }

    public static FunctionCtType newInstance(TypeMirror type,
            ProcessingEnvironment env) {
        assertNotNull(type, env);
        DeclaredType functionDeclaredType = getFunctionDeclaredType(type, env);
        if (functionDeclaredType == null) {
            return null;
        }

        FunctionCtType functionCtType = new FunctionCtType(type, env);
        List<? extends TypeMirror> typeArguments = functionDeclaredType
                .getTypeArguments();
        if (typeArguments.size() == 2) {
            TypeMirror targetTypeMirror = typeArguments.get(0);
            TypeMirror returnTypeMirror = typeArguments.get(1);

            functionCtType.targetCtType = StreamCtType
                    .newInstance(targetTypeMirror, env);
            if (functionCtType.targetCtType == null) {
                functionCtType.targetCtType = PreparedSqlCtType
                        .newInstance(targetTypeMirror, env);
                if (functionCtType.targetCtType == null) {
                    functionCtType.targetCtType = AnyCtType
                            .newInstance(targetTypeMirror, env);
                }
            }

            functionCtType.returnCtType = AnyCtType
                    .newInstance(returnTypeMirror, env);
        }

        return functionCtType;
    }

    protected static DeclaredType getFunctionDeclaredType(TypeMirror type,
            ProcessingEnvironment env) {
        if (TypeMirrorUtil.isSameType(type, Function.class, env)) {
            return TypeMirrorUtil.toDeclaredType(type, env);
        }
        for (TypeMirror supertype : env.getTypeUtils().directSupertypes(type)) {
            if (TypeMirrorUtil.isSameType(supertype, Function.class, env)) {
                return TypeMirrorUtil.toDeclaredType(supertype, env);
            }
            DeclaredType result = getFunctionDeclaredType(supertype, env);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitFunctionCtType(this, p);
    }
}

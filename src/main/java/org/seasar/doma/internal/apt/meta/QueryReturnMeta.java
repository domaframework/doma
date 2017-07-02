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

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.TypeKindVisitor8;

import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.Result;

public class QueryReturnMeta {

    private final Context ctx;

    private final ExecutableElement methodElement;

    private final CtType ctType;

    public QueryReturnMeta(Context ctx, ExecutableElement methodElement,
            CtType ctType) {
        assertNotNull(ctx, methodElement, ctType);
        this.ctx = ctx;
        this.methodElement = methodElement;
        this.ctType = ctType;
    }

    public String getTypeName() {
        return ctType.getTypeName();
    }

    public boolean isPrimitiveInt() {
        return ctType.getType().getKind() == TypeKind.INT;
    }

    public boolean isPrimitiveIntArray() {
        return ctType.getType()
                .accept(new TypeKindVisitor8<Boolean, Void>(false) {

            @Override
            public Boolean visitArray(ArrayType t, Void p) {
                return t.getComponentType().getKind() == TypeKind.INT;
            }
        }, null);
    }

    public boolean isPrimitiveVoid() {
        return ctType.getType().getKind() == TypeKind.VOID;
    }

    public boolean isResult(EntityCtType entityCtType) {
        TypeMirror type = ctType.getType();
        if (!ctx.getTypes().isSameType(type, Result.class)) {
            return false;
        }
        DeclaredType declaredType = ctx.getTypes().toDeclaredType(type);
        if (declaredType == null) {
            return false;
        }
        List<? extends TypeMirror> typeArgs = declaredType.getTypeArguments();
        if (typeArgs.size() != 1) {
            return false;
        }
        TypeMirror typeArg = typeArgs.get(0);
        return ctx.getTypes().isSameType(typeArg, entityCtType.getType());
    }

    public boolean isBatchResult(EntityCtType entityCtType) {
        TypeMirror type = ctType.getType();
        if (!ctx.getTypes().isSameType(type, BatchResult.class)) {
            return false;
        }
        DeclaredType declaredType = ctx.getTypes().toDeclaredType(type);
        if (declaredType == null) {
            return false;
        }
        List<? extends TypeMirror> typeArgs = declaredType.getTypeArguments();
        if (typeArgs.size() != 1) {
            return false;
        }
        TypeMirror typeArg = typeArgs.get(0);
        return ctx.getTypes().isSameType(typeArg, entityCtType.getType());
    }

    public ExecutableElement getMethodElement() {
        return methodElement;
    }

    public TypeMirror getType() {
        return ctType.getType();
    }

    public CtType getCtType() {
        return ctType;
    }

}

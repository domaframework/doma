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

import javax.lang.model.element.ElementKind;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.Context;

public class BasicCtType extends AbstractCtType {

    private final String boxedTypeName;

    private final String boxedClassName;

    private final String wrapperTypeName;

    BasicCtType(Context ctx, TypeMirror type, TypeMirror wrapperType) {
        super(ctx, type);
        this.boxedTypeName = ctx.getTypes().getBoxedTypeName(type);
        this.boxedClassName = ctx.getTypes().getBoxedClassName(type);
        this.wrapperTypeName = ctx.getTypes().getTypeName(wrapperType);
    }

    public String getBoxedTypeName() {
        return boxedTypeName;
    }

    public String getBoxedClassName() {
        return boxedClassName;
    }

    public String getWrapperTypeName() {
        return wrapperTypeName;
    }

    public boolean isEnum() {
        return typeElement != null && typeElement.getKind() == ElementKind.ENUM;
    }

    public boolean isPrimitive() {
        return typeMirror.getKind().isPrimitive();
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitBasicCtType(this, p);
    }

}

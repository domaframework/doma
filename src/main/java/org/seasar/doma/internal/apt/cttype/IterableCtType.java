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

import java.util.List;

import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.Context;

public class IterableCtType extends AbstractCtType {

    private final CtType elementCtType;

    IterableCtType(Context ctx, TypeMirror type, CtType elementCtType) {
        super(ctx, type);
        this.elementCtType = elementCtType;
    }

    public CtType getElementCtType() {
        return elementCtType;
    }

    public boolean isList() {
        return ctx.getTypes().isSameType(type, List.class);
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitIterableCtType(this, p);
    }
}

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

import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.Context;

public class CollectorCtType extends AbstractCtType {

    private final CtType targetCtType;

    private final AnyCtType returnCtType;

    CollectorCtType(Context ctx, TypeMirror type, CtType targetCtType, AnyCtType returnCtType) {
        super(ctx, type);
        this.targetCtType = targetCtType;
        this.returnCtType = returnCtType;
    }

    public CtType getTargetCtType() {
        return targetCtType;
    }

    public AnyCtType getReturnCtType() {
        return returnCtType;
    }

    public boolean hasWildcardType() {
        return returnCtType.getType() != null
                && returnCtType.getType().getKind() == TypeKind.WILDCARD
                || targetCtType.getType() != null
                        && targetCtType.getType().getKind() == TypeKind.WILDCARD;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitCollectorCtType(this, p);
    }
}

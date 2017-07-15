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

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.codespec.CodeSpec;

/**
 * @author taedium
 * 
 */
public class EntityCtType extends AbstractCtType {

    private final boolean immutable;

    private final String descClassName;

    EntityCtType(Context ctx, TypeMirror type, boolean immutable) {
        super(ctx, type);
        assertNotNull(typeElement);
        this.immutable = immutable;
        CodeSpec codeSpec = ctx.getCodeSpecs().newEntityDescCodeSpec(typeElement);
        this.descClassName = codeSpec.getQualifiedName();
    }

    public boolean isImmutable() {
        return immutable;
    }

    public boolean isAbstract() {
        return typeElement.getModifiers().contains(Modifier.ABSTRACT);
    }

    public String getDescClassName() {
        return descClassName;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitEntityCtType(this, p);
    }
}

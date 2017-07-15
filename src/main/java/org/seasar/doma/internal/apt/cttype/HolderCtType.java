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

import java.util.function.Function;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.codespec.CodeSpec;

public class HolderCtType extends AbstractCtType {

    private final BasicCtType basicCtType;

    private final String descClassName;

    private final String typeArgDecl;

    HolderCtType(Context ctx, TypeMirror type, BasicCtType basicCtType,
            Function<TypeElement, CodeSpec> codeSpecFactory) {
        super(ctx, type);
        assertNotNull(basicCtType, codeSpecFactory, typeElement);
        this.basicCtType = basicCtType;
        CodeSpec codeSpec = codeSpecFactory.apply(typeElement);
        this.descClassName = codeSpec.getQualifiedName();
        int pos = typeName.indexOf('<');
        if (pos > -1) {
            this.typeArgDecl = typeName.substring(pos);
        } else {
            this.typeArgDecl = "";
        }
    }

    public BasicCtType getBasicCtType() {
        return basicCtType;
    }

    public String getInstantiationCode() {
        return descClassName + "." + typeArgDecl + "getSingletonInternal()";
    }

    @Override
    public <R, P, TH extends Throwable> R accept(CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitHolderCtType(this, p);
    }

}

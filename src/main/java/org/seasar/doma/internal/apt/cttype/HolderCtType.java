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

import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.Context;

public class HolderCtType extends AbstractCtType {

    private final BasicCtType basicCtType;

    private final boolean external;

    private final String descClassName;

    private final String typeArgDecl;

    HolderCtType(Context ctx, TypeMirror type, BasicCtType basicCtType,
            boolean external) {
        super(ctx, type);
        assertNotNull(basicCtType);
        this.basicCtType = basicCtType;
        this.external = external;
        int pos = descTypeName.indexOf('<');
        if (pos > -1) {
            this.descClassName = descTypeName.substring(0, pos);
            this.typeArgDecl = descTypeName.substring(pos);
        } else {
            this.descClassName = descTypeName;
            this.typeArgDecl = "";
        }
    }

    public BasicCtType getBasicCtType() {
        return basicCtType;
    }

    public String getInstantiationCommand() {
        return normalize(descClassName) + "." + typeArgDecl
                + "getSingletonInternal()";
    }

    protected String normalize(String name) {
        if (external) {
            return Constants.EXTERNAL_HOLDER_DESC_ROOT_PACKAGE + "." + name;
        }
        return name;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitHolderCtType(this, p);
    }

}

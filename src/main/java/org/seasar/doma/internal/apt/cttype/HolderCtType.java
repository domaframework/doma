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

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;

public class HolderCtType extends AbstractCtType {

    private final BasicCtType basicCtType;

    private final boolean external;

    private final String metaClassName;

    private final String typeArgDecl;

    private boolean isRawType;

    private boolean isWildcardType;

    HolderCtType(Context ctx, TypeMirror holderType,
            BasicCtType basicCtType, boolean external) {
        super(ctx, holderType);
        assertNotNull(basicCtType);
        this.basicCtType = basicCtType;
        this.external = external;
        int pos = metaTypeName.indexOf('<');
        if (pos > -1) {
            this.metaClassName = metaTypeName.substring(0, pos);
            this.typeArgDecl = metaTypeName.substring(pos);
        } else {
            this.metaClassName = metaTypeName;
            this.typeArgDecl = "";
        }
        if (!typeElement.getTypeParameters().isEmpty()) {
            DeclaredType declaredType = ctx.getTypes()
                    .toDeclaredType(getTypeMirror());
            if (declaredType == null) {
                throw new AptIllegalStateException(getTypeName());
            }
            if (declaredType.getTypeArguments().isEmpty()) {
                isRawType = true;
            }
            for (TypeMirror typeArg : declaredType.getTypeArguments()) {
                if (typeArg.getKind() == TypeKind.WILDCARD
                        || typeArg.getKind() == TypeKind.TYPEVAR) {
                    isWildcardType = true;
                }
            }
        }
    }

    public BasicCtType getBasicCtType() {
        return basicCtType;
    }

    public boolean isRawType() {
        return isRawType;
    }

    public boolean isWildcardType() {
        return isWildcardType;
    }

    public String getInstantiationCommand() {
        return normalize(metaClassName) + "." + typeArgDecl
                + "getSingletonInternal()";
    }

    protected String normalize(String name) {
        if (external) {
            return Constants.EXTERNAL_HOLDER_METATYPE_ROOT_PACKAGE + "." + name;
        }
        return name;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitHolderCtType(this, p);
    }

}

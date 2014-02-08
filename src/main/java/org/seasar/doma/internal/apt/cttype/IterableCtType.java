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

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.util.TypeMirrorUtil;

public class IterableCtType extends AbstractCtType {

    protected TypeMirror elementTypeMirror;

    protected CtType elementCtType;

    public IterableCtType(TypeMirror type, ProcessingEnvironment env) {
        super(type, env);
    }

    public CtType getElementCtType() {
        return elementCtType;
    }

    public boolean isRawType() {
        return elementTypeMirror == null;
    }

    public boolean isWildcardType() {
        return elementTypeMirror != null
                && elementTypeMirror.getKind() == TypeKind.WILDCARD;
    }

    public boolean isList() {
        return TypeMirrorUtil.isSameType(typeMirror, List.class, env);
    }

    public static IterableCtType newInstance(TypeMirror type,
            ProcessingEnvironment env) {
        assertNotNull(type, env);
        TypeMirror supertype = TypeMirrorUtil.getSupertypeMirror(type,
                Iterable.class, env);
        if (supertype == null) {
            return null;
        }
        IterableCtType iterableCtType = new IterableCtType(type, env);
        DeclaredType declaredType = TypeMirrorUtil.toDeclaredType(supertype,
                env);
        if (declaredType == null) {
            return null;
        }
        List<? extends TypeMirror> typeArgs = declaredType.getTypeArguments();
        if (typeArgs.size() > 0) {
            iterableCtType.elementTypeMirror = typeArgs.get(0);
            iterableCtType.elementCtType = EntityCtType.newInstance(
                    iterableCtType.elementTypeMirror, env);
            if (iterableCtType.elementCtType == null) {
                iterableCtType.elementCtType = OptionalCtType.newInstance(
                        iterableCtType.elementTypeMirror, env);
                if (iterableCtType.elementCtType == null) {
                    iterableCtType.elementCtType = DomainCtType.newInstance(
                            iterableCtType.elementTypeMirror, env);
                    if (iterableCtType.elementCtType == null) {
                        iterableCtType.elementCtType = BasicCtType.newInstance(
                                iterableCtType.elementTypeMirror, env);
                        if (iterableCtType.elementCtType == null) {
                            iterableCtType.elementCtType = MapCtType.newInstance(
                                    iterableCtType.elementTypeMirror, env);
                            if (iterableCtType.elementCtType == null) {
                                iterableCtType.elementCtType = AnyCtType
                                        .newInstance(
                                                iterableCtType.elementTypeMirror,
                                                env);
                            }
                        }
                    }
                }
            }
        }
        return iterableCtType;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            CtTypeVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitIterableCtType(this, p);
    }
}

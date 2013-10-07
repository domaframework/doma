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

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.TypeKindVisitor6;

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.cttype.AnyCtType;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.DomainCtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.IterableCtType;
import org.seasar.doma.internal.apt.cttype.MapCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.Result;
import org.seasar.doma.message.Message;

public class QueryReturnMeta {

    protected final ProcessingEnvironment env;

    protected final ExecutableElement element;

    protected final TypeMirror type;

    protected final String typeName;

    protected final CtType ctType;

    public QueryReturnMeta(ExecutableElement methodElement,
            ProcessingEnvironment env) {
        assertNotNull(methodElement, env);
        this.element = methodElement;
        this.env = env;
        type = methodElement.getReturnType();
        typeName = TypeMirrorUtil.getTypeName(type, env);
        ctType = createCtType(methodElement, type, env);
    }

    protected CtType createCtType(final ExecutableElement methodElement,
            final TypeMirror type, final ProcessingEnvironment env) {
        IterableCtType iterableCtType = IterableCtType.newInstance(type, env);
        if (iterableCtType != null) {
            if (iterableCtType.isRawType()) {
                throw new AptException(Message.DOMA4109, env, methodElement,
                        typeName);
            }
            if (iterableCtType.isWildcardType()) {
                throw new AptException(Message.DOMA4113, env, methodElement,
                        typeName);
            }
            iterableCtType.getElementType().accept(
                    new SimpleCtTypeVisitor<Void, Void, RuntimeException>() {

                        @Override
                        public Void visitDomainCtType(final DomainCtType ctType,
                                Void p) throws RuntimeException {
                            if (ctType.isRawType()) {
                                throw new AptException(Message.DOMA4210, env,
                                        methodElement,
                                        ctType.getQualifiedName());
                            }
                            if (ctType.isWildcardType()) {
                                throw new AptException(Message.DOMA4211, env,
                                        methodElement,
                                        ctType.getQualifiedName());
                            }
                            return null;
                        }

                    }, null);
            return iterableCtType;
        }

        EntityCtType entityCtType = EntityCtType.newInstance(type, env);
        if (entityCtType != null) {
            return entityCtType;
        }

        final DomainCtType domainCtType = DomainCtType.newInstance(type, env);
        if (domainCtType != null) {
            if (domainCtType.isRawType()) {
                throw new AptException(Message.DOMA4206, env, methodElement,
                        domainCtType.getQualifiedName());
            }
            if (domainCtType.isWildcardType()) {
                throw new AptException(Message.DOMA4207, env, methodElement,
                        domainCtType.getQualifiedName());
            }
            return domainCtType;
        }

        BasicCtType basicCtType = BasicCtType.newInstance(type, env);
        if (basicCtType != null) {
            return basicCtType;
        }

        MapCtType mapCtType = MapCtType.newInstance(type, env);
        if (mapCtType != null) {
            return mapCtType;
        }

        return AnyCtType.newInstance(type, env);
    }

    public String getTypeName() {
        return typeName;
    }

    public String getBoxedTypeName() {
        return ctType.getBoxedTypeName();
    }

    public boolean isPrimitiveInt() {
        return type.getKind() == TypeKind.INT;
    }

    public boolean isPrimitiveIntArray() {
        return type.accept(new TypeKindVisitor6<Boolean, Void>(false) {

            @Override
            public Boolean visitArray(ArrayType t, Void p) {
                return t.getComponentType().getKind() == TypeKind.INT;
            }
        }, null);
    }

    public boolean isPrimitiveVoid() {
        return type.getKind() == TypeKind.VOID;
    }

    public boolean isResult(EntityCtType entityCtType) {
        if (!TypeMirrorUtil.isSameType(env.getTypeUtils().erasure(type),
                Result.class, env)) {
            return false;
        }
        DeclaredType declaredType = TypeMirrorUtil.toDeclaredType(type, env);
        if (declaredType == null) {
            return false;
        }
        List<? extends TypeMirror> typeArgs = declaredType.getTypeArguments();
        if (typeArgs.size() != 1) {
            return false;
        }
        TypeMirror typeArg = typeArgs.get(0);
        return TypeMirrorUtil.isSameType(typeArg, entityCtType.getTypeMirror(),
                env);
    }

    public boolean isBatchResult(EntityCtType entityCtType) {
        if (!TypeMirrorUtil.isSameType(env.getTypeUtils().erasure(type),
                BatchResult.class, env)) {
            return false;
        }
        DeclaredType declaredType = TypeMirrorUtil.toDeclaredType(type, env);
        if (declaredType == null) {
            return false;
        }
        List<? extends TypeMirror> typeArgs = declaredType.getTypeArguments();
        if (typeArgs.size() != 1) {
            return false;
        }
        TypeMirror typeArg = typeArgs.get(0);
        return TypeMirrorUtil.isSameType(typeArg, entityCtType.getTypeMirror(),
                env);
    }

    public ExecutableElement getElement() {
        return element;
    }

    public TypeMirror getType() {
        return type;
    }

    public CtType getCtType() {
        return ctType;
    }

}

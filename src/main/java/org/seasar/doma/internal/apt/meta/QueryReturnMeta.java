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

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.TypeKindVisitor8;

import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.cttype.AnyCtType;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.DomainCtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.IterableCtType;
import org.seasar.doma.internal.apt.cttype.MapCtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.OptionalDoubleCtType;
import org.seasar.doma.internal.apt.cttype.OptionalIntCtType;
import org.seasar.doma.internal.apt.cttype.OptionalLongCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.cttype.StreamCtType;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.Result;
import org.seasar.doma.message.Message;

public class QueryReturnMeta {

    protected final ProcessingEnvironment env;

    protected final ExecutableElement methodElement;

    protected final TypeElement daoElement;

    protected final TypeMirror type;

    protected final String typeName;

    protected final CtType ctType;

    public QueryReturnMeta(QueryMeta queryMeta, ProcessingEnvironment env) {
        assertNotNull(queryMeta, env);
        this.env = env;
        methodElement = queryMeta.getMethodElement();
        daoElement = queryMeta.getDaoElement();
        type = methodElement.getReturnType();
        typeName = TypeMirrorUtil.getTypeName(type, env);
        ctType = createCtType();
    }

    protected CtType createCtType() {
        IterableCtType iterableCtType = IterableCtType.newInstance(type, env);
        if (iterableCtType != null) {
            if (iterableCtType.isRawType()) {
                throw new AptException(Message.DOMA4109, env, methodElement,
                        new Object[] { typeName, daoElement.getQualifiedName(),
                                methodElement.getSimpleName() });
            }
            if (iterableCtType.isWildcardType()) {
                throw new AptException(Message.DOMA4113, env, methodElement,
                        new Object[] { typeName, daoElement.getQualifiedName(),
                                methodElement.getSimpleName() });
            }
            iterableCtType.getElementCtType().accept(
                    new IterableElementCtTypeVisitor(), null);
            return iterableCtType;
        }

        StreamCtType streamCtType = StreamCtType.newInstance(type, env);
        if (streamCtType != null) {
            return streamCtType;
        }

        EntityCtType entityCtType = EntityCtType.newInstance(type, env);
        if (entityCtType != null) {
            return entityCtType;
        }

        OptionalCtType optionalCtType = OptionalCtType.newInstance(type, env);
        if (optionalCtType != null) {
            if (optionalCtType.isRawType()) {
                throw new AptException(Message.DOMA4236, env, methodElement,
                        new Object[] { optionalCtType.getQualifiedName(),
                                daoElement.getQualifiedName(),
                                methodElement.getSimpleName() });
            }
            if (optionalCtType.isWildcardType()) {
                throw new AptException(Message.DOMA4237, env, methodElement,
                        new Object[] { optionalCtType.getQualifiedName(),
                                daoElement.getQualifiedName(),
                                methodElement.getSimpleName() });
            }
            optionalCtType.getElementCtType().accept(
                    new OptionalElementCtTypeVisitor(), null);
            return optionalCtType;
        }

        OptionalIntCtType optionalIntCtType = OptionalIntCtType.newInstance(
                type, env);
        if (optionalIntCtType != null) {
            return optionalIntCtType;
        }

        OptionalLongCtType optionalLongCtType = OptionalLongCtType.newInstance(
                type, env);
        if (optionalLongCtType != null) {
            return optionalLongCtType;
        }

        OptionalDoubleCtType optionalDoubleCtType = OptionalDoubleCtType
                .newInstance(type, env);
        if (optionalDoubleCtType != null) {
            return optionalDoubleCtType;
        }

        DomainCtType domainCtType = DomainCtType.newInstance(type, env);
        if (domainCtType != null) {
            if (domainCtType.isRawType()) {
                throw new AptException(Message.DOMA4206, env, methodElement,
                        new Object[] { domainCtType.getQualifiedName(),
                                daoElement.getQualifiedName(),
                                methodElement.getSimpleName() });
            }
            if (domainCtType.isWildcardType()) {
                throw new AptException(Message.DOMA4207, env, methodElement,
                        new Object[] { domainCtType.getQualifiedName(),
                                daoElement.getQualifiedName(),
                                methodElement.getSimpleName() });
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
        return type.accept(new TypeKindVisitor8<Boolean, Void>(false) {

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

    public ExecutableElement getMethodElement() {
        return methodElement;
    }

    public TypeElement getDaoElement() {
        return daoElement;
    }

    public TypeMirror getType() {
        return type;
    }

    public CtType getCtType() {
        return ctType;
    }

    /**
     * 
     * @author nakamura-to
     * 
     */
    protected class IterableElementCtTypeVisitor extends
            SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        @Override
        public Void visitDomainCtType(final DomainCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4210, env, methodElement,
                        new Object[] { ctType.getQualifiedName(),
                                daoElement.getQualifiedName(),
                                methodElement.getSimpleName() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4211, env, methodElement,
                        new Object[] { ctType.getQualifiedName(),
                                daoElement.getQualifiedName(),
                                methodElement.getSimpleName() });
            }
            return null;
        }
    }

    /**
     * 
     * @author nakamura-to
     * 
     */
    protected class OptionalElementCtTypeVisitor extends
            SimpleCtTypeVisitor<Void, Void, RuntimeException> {

        @Override
        public Void visitDomainCtType(final DomainCtType ctType, Void p)
                throws RuntimeException {
            if (ctType.isRawType()) {
                throw new AptException(Message.DOMA4238, env, methodElement,
                        new Object[] { ctType.getQualifiedName(),
                                daoElement.getQualifiedName(),
                                methodElement.getSimpleName() });
            }
            if (ctType.isWildcardType()) {
                throw new AptException(Message.DOMA4239, env, methodElement,
                        new Object[] { ctType.getQualifiedName(),
                                daoElement.getQualifiedName(),
                                methodElement.getSimpleName() });
            }
            return null;
        }
    }
}

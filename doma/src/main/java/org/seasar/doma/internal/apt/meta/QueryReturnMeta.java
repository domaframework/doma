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
import org.seasar.doma.internal.apt.type.AnyType;
import org.seasar.doma.internal.apt.type.BasicType;
import org.seasar.doma.internal.apt.type.DataType;
import org.seasar.doma.internal.apt.type.DomainType;
import org.seasar.doma.internal.apt.type.EntityType;
import org.seasar.doma.internal.apt.type.IterableType;
import org.seasar.doma.internal.apt.type.MapType;
import org.seasar.doma.internal.apt.type.SimpleDataTypeVisitor;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.jdbc.BatchResult;
import org.seasar.doma.jdbc.Result;
import org.seasar.doma.message.Message;

public class QueryReturnMeta {

    protected final ProcessingEnvironment env;

    protected final ExecutableElement element;

    protected final TypeMirror type;

    protected final String typeName;

    protected final DataType dataType;

    public QueryReturnMeta(ExecutableElement methodElement,
            ProcessingEnvironment env) {
        assertNotNull(methodElement, env);
        this.element = methodElement;
        this.env = env;
        type = methodElement.getReturnType();
        typeName = TypeMirrorUtil.getTypeName(type, env);
        dataType = createDataType(methodElement, type, env);
    }

    protected DataType createDataType(final ExecutableElement methodElement,
            final TypeMirror type, final ProcessingEnvironment env) {
        IterableType iterableType = IterableType.newInstance(type, env);
        if (iterableType != null) {
            if (iterableType.isRawType()) {
                throw new AptException(Message.DOMA4109, env, methodElement,
                        typeName);
            }
            if (iterableType.isWildcardType()) {
                throw new AptException(Message.DOMA4113, env, methodElement,
                        typeName);
            }
            iterableType.getElementType().accept(
                    new SimpleDataTypeVisitor<Void, Void, RuntimeException>() {

                        @Override
                        public Void visitDomainType(final DomainType dataType,
                                Void p) throws RuntimeException {
                            if (dataType.isRawType()) {
                                throw new AptException(Message.DOMA4210, env,
                                        methodElement,
                                        dataType.getQualifiedName());
                            }
                            if (dataType.isWildcardType()) {
                                throw new AptException(Message.DOMA4211, env,
                                        methodElement,
                                        dataType.getQualifiedName());
                            }
                            return null;
                        }

                    }, null);
            return iterableType;
        }

        EntityType entityType = EntityType.newInstance(type, env);
        if (entityType != null) {
            return entityType;
        }

        final DomainType domainType = DomainType.newInstance(type, env);
        if (domainType != null) {
            if (domainType.isRawType()) {
                throw new AptException(Message.DOMA4206, env, methodElement,
                        domainType.getQualifiedName());
            }
            if (domainType.isWildcardType()) {
                throw new AptException(Message.DOMA4207, env, methodElement,
                        domainType.getQualifiedName());
            }
            return domainType;
        }

        BasicType basicType = BasicType.newInstance(type, env);
        if (basicType != null) {
            return basicType;
        }

        MapType mapType = MapType.newInstance(type, env);
        if (mapType != null) {
            return mapType;
        }

        return AnyType.newInstance(type, env);
    }

    public String getTypeName() {
        return typeName;
    }

    public String getTypeNameAsTypeParameter() {
        return dataType.getTypeNameAsTypeParameter();
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

    public boolean isResult(EntityType entityType) {
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
        return TypeMirrorUtil.isSameType(typeArg, entityType.getTypeMirror(),
                env);
    }

    public boolean isBatchResult(EntityType entityType) {
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
        return TypeMirrorUtil.isSameType(typeArg, entityType.getTypeMirror(),
                env);
    }

    public ExecutableElement getElement() {
        return element;
    }

    public TypeMirror getType() {
        return type;
    }

    public DataType getDataType() {
        return dataType;
    }

}

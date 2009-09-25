/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.doma.internal.apt.meta.type;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.Collection;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.TypeUtil;

public class CollectionType {

    protected TypeMirror type;

    protected String typeName;

    protected TypeMirror elementType;

    protected EntityType entityType;

    protected DomainType domainType;

    protected ValueType valueType;

    protected CollectionType() {
    }

    public TypeMirror getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public DomainType getDomainType() {
        return domainType;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public boolean isParametarized() {
        return elementType != null;
    }

    public boolean hasSupportedElementType() {
        return entityType != null || domainType != null || valueType != null;
    }

    public static CollectionType newInstance(TypeMirror type,
            ProcessingEnvironment env) {
        assertNotNull(type, env);
        if (!TypeUtil.isAssignable(type, Collection.class, env)) {
            return null;
        }
        CollectionType collectionType = new CollectionType();
        collectionType.type = type;
        collectionType.typeName = TypeUtil.getTypeName(type, env);

        DeclaredType declaredType = TypeUtil.toDeclaredType(type, env);
        if (declaredType == null) {
            return null;
        }
        List<? extends TypeMirror> typeArgs = declaredType.getTypeArguments();
        if (typeArgs.size() > 0) {
            collectionType.elementType = typeArgs.get(0);
            collectionType.entityType = EntityType.newInstance(
                    collectionType.elementType, env);
            if (collectionType.entityType == null) {
                collectionType.domainType = DomainType.newInstance(
                        collectionType.elementType, env);
                if (collectionType.domainType == null) {
                    collectionType.valueType = ValueType.newInstance(
                            collectionType.elementType, env);
                }
            }
        }
        return collectionType;
    }
}

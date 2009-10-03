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
package org.seasar.doma.internal.apt.type;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.TypeUtil;

public class ListType {

    protected TypeMirror type;

    protected String typeName;

    protected TypeMirror elementType;

    protected EntityType entityType;

    protected DomainType domainType;

    protected BasicType basicType;

    protected ListType() {
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

    public BasicType getValueType() {
        return basicType;
    }

    public boolean isRawType() {
        return elementType == null;
    }

    public boolean isWildcardType() {
        return elementType != null
                && elementType.getKind() == TypeKind.WILDCARD;
    }

    public boolean hasSupportedElementType() {
        return entityType != null || domainType != null || basicType != null;
    }

    public static ListType newInstance(TypeMirror type,
            ProcessingEnvironment env) {
        assertNotNull(type, env);
        if (!TypeUtil.isSameType(type, List.class, env)) {
            return null;
        }
        ListType listType = new ListType();
        listType.type = type;
        listType.typeName = TypeUtil.getTypeName(type, env);

        DeclaredType declaredType = TypeUtil.toDeclaredType(type, env);
        if (declaredType == null) {
            return null;
        }
        List<? extends TypeMirror> typeArgs = declaredType.getTypeArguments();
        if (typeArgs.size() > 0) {
            listType.elementType = typeArgs.get(0);
            listType.entityType = EntityType.newInstance(listType.elementType,
                    env);
            if (listType.entityType == null) {
                listType.domainType = DomainType.newInstance(
                        listType.elementType, env);
                if (listType.domainType == null) {
                    listType.basicType = BasicType.newInstance(
                            listType.elementType, env);
                }
            }
        }
        return listType;
    }
}

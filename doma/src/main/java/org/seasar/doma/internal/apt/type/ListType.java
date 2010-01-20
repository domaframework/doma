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

import org.seasar.doma.internal.apt.util.TypeMirrorUtil;

public class ListType extends AbstractDataType {

    protected TypeMirror elementTypeMirror;

    protected DataType elementType;

    public ListType(TypeMirror type, ProcessingEnvironment env) {
        super(type, env);
    }

    public DataType getElementType() {
        return elementType;
    }

    public boolean isRawType() {
        return elementTypeMirror == null;
    }

    public boolean isWildcardType() {
        return elementTypeMirror != null
                && elementTypeMirror.getKind() == TypeKind.WILDCARD;
    }

    public static ListType newInstance(TypeMirror type,
            ProcessingEnvironment env) {
        assertNotNull(type, env);
        if (!TypeMirrorUtil.isSameType(type, List.class, env)) {
            return null;
        }
        ListType listType = new ListType(type, env);
        DeclaredType declaredType = TypeMirrorUtil.toDeclaredType(type, env);
        if (declaredType == null) {
            return null;
        }
        List<? extends TypeMirror> typeArgs = declaredType.getTypeArguments();
        if (typeArgs.size() > 0) {
            listType.elementTypeMirror = typeArgs.get(0);
            listType.elementType = EntityType.newInstance(
                    listType.elementTypeMirror, env);
            if (listType.elementType == null) {
                listType.elementType = DomainType.newInstance(
                        listType.elementTypeMirror, env);
                if (listType.elementType == null) {
                    listType.elementType = BasicType.newInstance(
                            listType.elementTypeMirror, env);
                    if (listType.elementType == null) {
                        listType.elementType = AnyType.newInstance(
                                listType.elementTypeMirror, env);
                    }
                }
            }
        }
        return listType;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            DataTypeVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitListType(this, p);
    }
}

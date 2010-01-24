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
package org.seasar.doma.internal.apt.type;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.Entity;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;

/**
 * @author taedium
 * 
 */
public class EntityType extends AbstractDataType {

    public EntityType(TypeMirror type, ProcessingEnvironment env) {
        super(type, env);
    }

    public static EntityType newInstance(TypeMirror type,
            ProcessingEnvironment env) {
        assertNotNull(type, env);
        TypeElement typeElement = TypeMirrorUtil.toTypeElement(type, env);
        if (typeElement == null
                || typeElement.getAnnotation(Entity.class) == null) {
            return null;
        }
        return new EntityType(type, env);
    }

    public boolean isAbstract() {
        return typeElement.getModifiers().contains(Modifier.ABSTRACT);
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            DataTypeVisitor<R, P, TH> visitor, P p) throws TH {
        return visitor.visitEntityType(this, p);
    }
}

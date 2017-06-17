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

import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import org.seasar.doma.Transient;
import org.seasar.doma.internal.apt.Context;

/**
 * @author taedium
 * 
 */
public class EntityPropertyNameCollector {

    private final Context ctx;

    public EntityPropertyNameCollector(Context ctx) {
        assertNotNull(ctx);
        this.ctx = ctx;
    }

    public Set<String> collect(TypeMirror entityType) {
        Set<String> names = new HashSet<String>();
        collectNames(entityType, names);
        return names;
    }

    protected void collectNames(TypeMirror type, Set<String> names) {
        for (TypeElement t = ctx.getTypes().toTypeElement(type); t != null
                && t.asType().getKind() != TypeKind.NONE; t = ctx.getTypes()
                        .toTypeElement(t.getSuperclass())) {
            for (VariableElement field : ElementFilter.fieldsIn(t
                    .getEnclosedElements())) {
                if (isPersistent(field)) {
                    names.add(field.getSimpleName().toString());
                }
            }
        }
    }

    protected boolean isPersistent(VariableElement field) {
        return field.getAnnotation(Transient.class) == null
                && !field.getModifiers().contains(Modifier.STATIC);
    }
}

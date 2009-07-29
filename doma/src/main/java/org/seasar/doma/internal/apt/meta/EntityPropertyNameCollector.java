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
package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import org.seasar.doma.Delete;
import org.seasar.doma.Transient;
import org.seasar.doma.internal.apt.TypeUtil;

/**
 * @author taedium
 * 
 */
public class EntityPropertyNameCollector {

    protected final ProcessingEnvironment env;

    public EntityPropertyNameCollector(ProcessingEnvironment env) {
        assertNotNull(env);
        this.env = env;
    }

    public Set<String> collect(TypeMirror entityType) {
        Set<String> names = new HashSet<String>();
        collectNames(entityType, names);
        return names;
    }

    protected void collectNames(TypeMirror type, Set<String> names) {
        TypeElement typeElement = TypeUtil.toTypeElement(type, env);
        for (ExecutableElement method : ElementFilter.methodsIn(typeElement
                .getEnclosedElements())) {
            if (isPersistent(method)) {
                names.add(method.getSimpleName().toString());
            }
        }
        for (TypeMirror supertype : env.getTypeUtils().directSupertypes(type)) {
            collectNames(supertype, names);
        }
    }

    protected boolean isPersistent(ExecutableElement method) {
        return method.getAnnotation(Delete.class) == null
                && method.getAnnotation(Transient.class) == null;
    }
}

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
package org.seasar.doma.internal.apt.mirror;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.Entity;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.internal.apt.util.ElementUtil;
import org.seasar.doma.jdbc.entity.NamingType;

/**
 * @author taedium
 * 
 */
public class EntityMirror {

    protected final AnnotationMirror annotationMirror;

    protected AnnotationValue listener;

    protected AnnotationValue naming;

    protected AnnotationValue immutable;

    public EntityMirror(AnnotationMirror annotationMirror) {
        this.annotationMirror = annotationMirror;
    }

    public AnnotationMirror getAnnotationMirror() {
        return annotationMirror;
    }

    public AnnotationValue getListener() {
        return listener;
    }

    public AnnotationValue getNaming() {
        return naming;
    }

    public AnnotationValue getImmutable() {
        return immutable;
    }

    public TypeMirror getListenerValue() {
        TypeMirror result = AnnotationValueUtil.toType(listener);
        if (result == null) {
            throw new AptIllegalStateException("listener");
        }
        return result;
    }

    public NamingType getNamingValue() {
        VariableElement enumConstant = AnnotationValueUtil
                .toEnumConstant(naming);
        if (enumConstant == null) {
            throw new AptIllegalStateException("naming");
        }
        return NamingType.valueOf(enumConstant.getSimpleName().toString());
    }

    public boolean getImmutableValue() {
        Boolean result = AnnotationValueUtil.toBoolean(immutable);
        if (result == null) {
            throw new AptIllegalStateException("immutable");
        }
        return result.booleanValue();
    }

    public static EntityMirror newInstance(TypeElement clazz,
            ProcessingEnvironment env) {
        assertNotNull(env);
        AnnotationMirror annotationMirror = ElementUtil.getAnnotationMirror(
                clazz, Entity.class, env);
        if (annotationMirror == null) {
            return null;
        }
        EntityMirror result = new EntityMirror(annotationMirror);
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : env
                .getElementUtils()
                .getElementValuesWithDefaults(annotationMirror).entrySet()) {
            String name = entry.getKey().getSimpleName().toString();
            AnnotationValue value = entry.getValue();
            if ("listener".equals(name)) {
                result.listener = value;
            } else if ("naming".equals(name)) {
                result.naming = value;
            } else if ("immutable".equals(name)) {
                result.immutable = value;
            }
        }
        return result;
    }

}

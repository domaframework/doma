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

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Options;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.internal.apt.util.ElementUtil;

/**
 * 
 * @author nakamura-to
 *
 */
public class AllArgsConstructorMirror {

    protected final AnnotationMirror annotationMirror;

    protected AnnotationValue staticName;

    protected AnnotationValue access;

    protected AllArgsConstructorMirror(AnnotationMirror annotationMirror) {
        assertNotNull(annotationMirror);
        this.annotationMirror = annotationMirror;
    }

    public static AllArgsConstructorMirror newInstance(TypeElement typeElement,
            ProcessingEnvironment env) {
        assertNotNull(env);
        AnnotationMirror annotationMirror = ElementUtil.getAnnotationMirror(
                typeElement, Options.getLombokAllArgsConstructor(env), env);
        if (annotationMirror == null) {
            return null;
        }
        AllArgsConstructorMirror result = new AllArgsConstructorMirror(
                annotationMirror);
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : env
                .getElementUtils()
                .getElementValuesWithDefaults(annotationMirror).entrySet()) {
            String name = entry.getKey().getSimpleName().toString();
            AnnotationValue value = entry.getValue();
            if ("staticName".equals(name)) {
                result.staticName = value;
            } else if ("access".equals(name)) {
                result.access = value;
            }
        }
        return result;
    }

    public AnnotationMirror getAnnotationMirror() {
        return annotationMirror;
    }

    public AnnotationValue getStaticName() {
        return staticName;
    }

    public AnnotationValue getAccess() {
        return access;
    }

    public String getStaticNameValue() {
        String value = AnnotationValueUtil.toString(staticName);
        if (value == null) {
            throw new AptIllegalStateException("staticConstructor");
        }
        return value;
    }

    public boolean isAccessPrivate() {
        VariableElement enumConstant = AnnotationValueUtil
                .toEnumConstant(access);
        if (enumConstant == null) {
            throw new AptIllegalStateException("access");
        }
        return "PRIVATE".equals(enumConstant.getSimpleName().toString());
    }

    public boolean isAccessNone() {
        VariableElement enumConstant = AnnotationValueUtil
                .toEnumConstant(access);
        if (enumConstant == null) {
            throw new AptIllegalStateException("access");
        }
        return "NONE".equals(enumConstant.getSimpleName().toString());
    }

}

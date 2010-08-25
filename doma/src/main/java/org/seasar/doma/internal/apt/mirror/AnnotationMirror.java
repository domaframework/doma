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
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

/**
 * @author taedium
 * 
 */
public class AnnotationMirror {
    protected final javax.lang.model.element.AnnotationMirror annotationMirror;

    protected AnnotationValue target;

    protected AnnotationValue type;

    protected AnnotationValue elements;

    protected AnnotationMirror(
            javax.lang.model.element.AnnotationMirror annotationMirror) {
        assertNotNull(annotationMirror);
        this.annotationMirror = annotationMirror;
    }

    public static AnnotationMirror newInstance(
            javax.lang.model.element.AnnotationMirror annotationMirror,
            ProcessingEnvironment env) {
        assertNotNull(annotationMirror);
        AnnotationMirror result = new AnnotationMirror(annotationMirror);
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : env
                .getElementUtils()
                .getElementValuesWithDefaults(annotationMirror).entrySet()) {
            String name = entry.getKey().getSimpleName().toString();
            AnnotationValue value = entry.getValue();
            if ("target".equals(name)) {
                result.target = value;
            } else if ("type".equals(name)) {
                result.type = value;
            } else if ("elements".equals(name)) {
                result.elements = value;
            }
        }
        return result;
    }

    public javax.lang.model.element.AnnotationMirror getAnnotationMirror() {
        return annotationMirror;
    }

    public AnnotationValue getTarget() {
        return target;
    }

    public AnnotationValue getType() {
        return type;
    }

    public AnnotationValue getElements() {
        return elements;
    }

    public VariableElement getTargetValue() {
        VariableElement value = AnnotationValueUtil.toEnumConstant(target);
        if (value == null) {
            throw new AptIllegalStateException("target");
        }
        return value;
    }

    public TypeMirror getTypeValue() {
        TypeMirror value = AnnotationValueUtil.toType(type);
        if (value == null) {
            throw new AptIllegalStateException("type");
        }
        return value;
    }

    public String getElementsValue() {
        String value = AnnotationValueUtil.toString(elements);
        if (value == null) {
            throw new AptIllegalStateException("elements");
        }
        return value;
    }
}

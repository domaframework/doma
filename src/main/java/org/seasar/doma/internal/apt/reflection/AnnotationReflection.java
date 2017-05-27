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
package org.seasar.doma.internal.apt.reflection;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNullValue;

import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

/**
 * @author taedium
 * 
 */
public class AnnotationReflection {

    protected final AnnotationMirror annotationMirror;

    protected final AnnotationValue target;

    protected final AnnotationValue type;

    protected final AnnotationValue elements;

    protected AnnotationReflection(
            AnnotationMirror annotationMirror,
            Map<String, AnnotationValue> values) {
        assertNotNull(annotationMirror, values);
        this.annotationMirror = annotationMirror;
        this.target = assertNotNullValue(values, "target");
        this.type = assertNotNullValue(values, "type");
        this.elements = assertNotNullValue(values, "elements");
    }

    public AnnotationMirror getAnnotationMirror() {
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

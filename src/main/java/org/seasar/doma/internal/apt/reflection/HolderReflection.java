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
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

/**
 * @author taedium
 * 
 */
public class HolderReflection {

    protected final AnnotationMirror annotationMirror;

    protected final AnnotationValue valueType;

    protected final AnnotationValue factoryMethod;

    protected final AnnotationValue accessorMethod;

    protected final AnnotationValue acceptNull;

    protected HolderReflection(AnnotationMirror annotationMirror,
            Map<String, AnnotationValue> values) {
        assertNotNull(annotationMirror, values);
        this.annotationMirror = annotationMirror;
        this.valueType = assertNotNullValue(values, "valueType");
        this.factoryMethod = assertNotNullValue(values, "factoryMethod");
        this.accessorMethod = assertNotNullValue(values, "accessorMethod");
        this.acceptNull = assertNotNullValue(values, "acceptNull");
    }

    public AnnotationMirror getAnnotationMirror() {
        return annotationMirror;
    }

    public AnnotationValue getValueType() {
        return valueType;
    }

    public AnnotationValue getFactoryMethod() {
        return factoryMethod;
    }

    public AnnotationValue getAccessorMethod() {
        return accessorMethod;
    }

    public AnnotationValue getAcceptNull() {
        return acceptNull;
    }

    public TypeMirror getValueTypeValue() {
        TypeMirror value = AnnotationValueUtil.toType(valueType);
        if (value == null) {
            throw new AptIllegalStateException("valueType");
        }
        return value;
    }

    public String getFactoryMethodValue() {
        String value = AnnotationValueUtil.toString(factoryMethod);
        if (value == null) {
            throw new AptIllegalStateException("factoryMethod");
        }
        return value;
    }

    public String getAccessorMethodValue() {
        String value = AnnotationValueUtil.toString(accessorMethod);
        if (value == null) {
            throw new AptIllegalStateException("accessorMethod");
        }
        return value;
    }

    public boolean getAcceptNullValue() {
        Boolean value = AnnotationValueUtil.toBoolean(acceptNull);
        if (value == null) {
            throw new AptIllegalStateException("acceptNull");
        }
        return value.booleanValue();
    }
}

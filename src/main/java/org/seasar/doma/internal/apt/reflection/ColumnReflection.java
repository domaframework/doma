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

import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

/**
 * @author taedium
 * 
 */
public class ColumnReflection extends AbstractReflection {

    public static final String QUOTE = "quote";

    public static final String UPDATABLE = "updatable";

    public static final String INSERTABLE = "insertable";

    public static final String NAME = "name";

    private final AnnotationValue name;

    private final AnnotationValue insertable;

    private final AnnotationValue updatable;

    private final AnnotationValue quote;

    ColumnReflection(AnnotationMirror annotationMirror, Map<String, AnnotationValue> values) {
        super(annotationMirror);
        assertNotNull(values);
        this.name = assertNotNullValue(values, NAME);
        this.insertable = assertNotNullValue(values, INSERTABLE);
        this.updatable = assertNotNullValue(values, UPDATABLE);
        this.quote = assertNotNullValue(values, QUOTE);
    }

    public AnnotationValue getName() {
        return name;
    }

    public AnnotationValue getInsertable() {
        return insertable;
    }

    public AnnotationValue getUpdatable() {
        return updatable;
    }

    public AnnotationValue getQuote() {
        return quote;
    }

    public String getNameValue() {
        String value = AnnotationValueUtil.toString(name);
        if (value == null) {
            throw new AptIllegalStateException(NAME);
        }
        return value;
    }

    public boolean getInsertableValue() {
        Boolean value = AnnotationValueUtil.toBoolean(insertable);
        if (value == null) {
            throw new AptIllegalStateException(INSERTABLE);
        }
        return value.booleanValue();
    }

    public boolean getUpdatableValue() {
        Boolean value = AnnotationValueUtil.toBoolean(updatable);
        if (value == null) {
            throw new AptIllegalStateException(UPDATABLE);
        }
        return value.booleanValue();
    }

    public boolean getQuoteValue() {
        Boolean value = AnnotationValueUtil.toBoolean(quote);
        if (value == null) {
            throw new AptIllegalStateException(QUOTE);
        }
        return value.booleanValue();
    }
}

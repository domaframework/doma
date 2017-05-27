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
public class TableReflection {

    protected final AnnotationMirror annotationMirror;

    protected final AnnotationValue catalog;

    protected final AnnotationValue schema;

    protected final AnnotationValue name;

    protected final AnnotationValue quote;

    protected TableReflection(AnnotationMirror annotationMirror,
            Map<String, AnnotationValue> values) {
        assertNotNull(annotationMirror, values);
        this.annotationMirror = annotationMirror;
        this.catalog = assertNotNullValue(values, "catalog");
        this.schema = assertNotNullValue(values, "schema");
        this.name = assertNotNullValue(values, "name");
        this.quote = assertNotNullValue(values, "quote");
    }

    public String getCatalogValue() {
        String value = AnnotationValueUtil.toString(catalog);
        if (value == null) {
            throw new AptIllegalStateException("catalog");
        }
        return value;
    }

    public String getSchemaValue() {
        String value = AnnotationValueUtil.toString(schema);
        if (value == null) {
            throw new AptIllegalStateException("catalog");
        }
        return value;
    }

    public String getNameValue() {
        String value = AnnotationValueUtil.toString(name);
        if (value == null) {
            throw new AptIllegalStateException("name");
        }
        return value;
    }

    public boolean getQuoteValue() {
        Boolean value = AnnotationValueUtil.toBoolean(quote);
        if (value == null) {
            throw new AptIllegalStateException("quote");
        }
        return value.booleanValue();
    }

}

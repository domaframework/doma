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
public class TableGeneratorReflection extends AbstractReflection {

    private final AnnotationValue catalog;

    private final AnnotationValue schema;

    private final AnnotationValue table;

    private final AnnotationValue pkColumnName;

    private final AnnotationValue valueColumnName;

    private final AnnotationValue pkColumnValue;

    private final AnnotationValue initialValue;

    private final AnnotationValue allocationSize;

    private final AnnotationValue implementer;

    TableGeneratorReflection(AnnotationMirror annotationMirror,
            Map<String, AnnotationValue> values) {
        super(annotationMirror);
        assertNotNull(values);
        this.catalog = assertNotNullValue(values, "catalog");
        this.schema = assertNotNullValue(values, "schema");
        this.table = assertNotNullValue(values, "table");
        this.pkColumnName = assertNotNullValue(values, "pkColumnName");
        this.valueColumnName = assertNotNullValue(values, "valueColumnName");
        this.pkColumnValue = assertNotNullValue(values, "pkColumnValue");
        this.initialValue = assertNotNullValue(values, "initialValue");
        this.allocationSize = assertNotNullValue(values, "allocationSize");
        this.implementer = assertNotNullValue(values, "implementer");
    }

    public AnnotationValue getCatalog() {
        return catalog;
    }

    public AnnotationValue getSchema() {
        return schema;
    }

    public AnnotationValue getTable() {
        return table;
    }

    public AnnotationValue getPkColumnName() {
        return pkColumnName;
    }

    public AnnotationValue getValueColumnName() {
        return valueColumnName;
    }

    public AnnotationValue getPkColumnValue() {
        return pkColumnValue;
    }

    public AnnotationValue getInitialValue() {
        return initialValue;
    }

    public AnnotationValue getAllocationSize() {
        return allocationSize;
    }

    public AnnotationValue getImplementer() {
        return implementer;
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
            throw new AptIllegalStateException("schema");
        }
        return value;
    }

    public String getTableValue() {
        String value = AnnotationValueUtil.toString(table);
        if (value == null) {
            throw new AptIllegalStateException("table");
        }
        return value;
    }

    public String getPkColumnNameValue() {
        String value = AnnotationValueUtil.toString(pkColumnName);
        if (value == null) {
            throw new AptIllegalStateException("pkColumnName");
        }
        return value;
    }

    public String getValueColumnNameValue() {
        String value = AnnotationValueUtil.toString(valueColumnName);
        if (value == null) {
            throw new AptIllegalStateException("valueColumnName");
        }
        return value;
    }

    public String getPkColumnValueValue() {
        String value = AnnotationValueUtil.toString(pkColumnValue);
        if (value == null) {
            throw new AptIllegalStateException("pkColumnValue");
        }
        return value;
    }

    public Long getInitialValueValue() {
        Long value = AnnotationValueUtil.toLong(initialValue);
        if (value == null) {
            throw new AptIllegalStateException("initialValue");
        }
        return value;
    }

    public Long getAllocationSizeValue() {
        Long value = AnnotationValueUtil.toLong(allocationSize);
        if (value == null) {
            throw new AptIllegalStateException("allocationSize");
        }
        return value;
    }

    public TypeMirror getImplementerValue() {
        TypeMirror value = AnnotationValueUtil.toType(implementer);
        if (value == null) {
            throw new AptIllegalStateException("implementer");
        }
        return value;
    }
}

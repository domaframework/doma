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
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.TableGenerator;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.internal.apt.util.ElementUtil;

/**
 * @author taedium
 * 
 */
public class TableGeneratorMirror {

    protected final AnnotationMirror annotationMirror;

    protected AnnotationValue catalog;

    protected AnnotationValue schema;

    protected AnnotationValue table;

    protected AnnotationValue pkColumnName;

    protected AnnotationValue valueColumnName;

    protected AnnotationValue pkColumnValue;

    protected AnnotationValue initialValue;

    protected AnnotationValue allocationSize;

    protected AnnotationValue implementer;

    protected TableGeneratorMirror(AnnotationMirror annotationMirror) {
        assertNotNull(annotationMirror);
        this.annotationMirror = annotationMirror;
    }

    public static TableGeneratorMirror newInstance(VariableElement field,
            ProcessingEnvironment env) {
        assertNotNull(env);
        AnnotationMirror annotationMirror = ElementUtil.getAnnotationMirror(
                field, TableGenerator.class, env);
        if (annotationMirror == null) {
            return null;
        }
        TableGeneratorMirror result = new TableGeneratorMirror(annotationMirror);
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : env
                .getElementUtils()
                .getElementValuesWithDefaults(annotationMirror).entrySet()) {
            String name = entry.getKey().getSimpleName().toString();
            AnnotationValue value = entry.getValue();
            if ("catalog".equals(name)) {
                result.catalog = value;
            } else if ("schema".equals(name)) {
                result.schema = value;
            } else if ("table".equals(name)) {
                result.table = value;
            } else if ("pkColumnName".equals(name)) {
                result.pkColumnName = value;
            } else if ("valueColumnName".equals(name)) {
                result.valueColumnName = value;
            } else if ("pkColumnValue".equals(name)) {
                result.pkColumnValue = value;
            } else if ("initialValue".equals(name)) {
                result.initialValue = value;
            } else if ("allocationSize".equals(name)) {
                result.allocationSize = value;
            } else if ("implementer".equals(name)) {
                result.implementer = value;
            }
        }
        return result;
    }

    public AnnotationMirror getAnnotationMirror() {
        return annotationMirror;
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

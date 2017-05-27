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

import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.jdbc.SqlLogType;

/**
 * @author taedium
 * 
 */
public class FunctionReflection {

    protected final AnnotationMirror annotationMirror;

    protected final String defaultName;

    protected final AnnotationValue catalog;

    protected final AnnotationValue schema;

    protected final AnnotationValue name;

    protected final AnnotationValue quote;

    protected final AnnotationValue queryTimeout;

    protected final AnnotationValue mapKeyNaming;

    protected final AnnotationValue ensureResultMapping;

    protected final AnnotationValue sqlLog;

    protected FunctionReflection(AnnotationMirror annotationMirror,
            String defaultName, Map<String, AnnotationValue> values) {
        assertNotNull(annotationMirror, defaultName, values);

        this.annotationMirror = annotationMirror;
        this.defaultName = defaultName;

        this.catalog = assertNotNullValue(values, "catalog");
        this.schema = assertNotNullValue(values, "schema");
        this.name = assertNotNullValue(values, "name");
        this.quote = assertNotNullValue(values, "quote");
        this.queryTimeout = assertNotNullValue(values, "queryTimeout");
        this.mapKeyNaming = assertNotNullValue(values, "mapKeyNaming");
        this.ensureResultMapping = assertNotNullValue(values,
                "ensureResultMapping");
        this.sqlLog = assertNotNullValue(values, "sqlLog");
    }

    public AnnotationValue getQueryTimeout() {
        return queryTimeout;
    }

    public AnnotationValue getMapKeyNaming() {
        return mapKeyNaming;
    }

    public AnnotationValue getSqlLog() {
        return sqlLog;
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

    public String getNameValue() {
        String value = AnnotationValueUtil.toString(name);
        if (value == null || value.isEmpty()) {
            return defaultName;
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

    public int getQueryTimeoutValue() {
        Integer value = AnnotationValueUtil.toInteger(queryTimeout);
        if (value == null) {
            throw new AptIllegalStateException("queryTimeout");
        }
        return value.intValue();
    }

    public MapKeyNamingType getMapKeyNamingValue() {
        VariableElement enumConstant = AnnotationValueUtil
                .toEnumConstant(mapKeyNaming);
        if (enumConstant == null) {
            throw new AptIllegalStateException("mapKeyNaming");
        }
        return MapKeyNamingType
                .valueOf(enumConstant.getSimpleName().toString());
    }

    public boolean getEnsureResultMappingValue() {
        Boolean value = AnnotationValueUtil.toBoolean(ensureResultMapping);
        if (value == null) {
            throw new AptIllegalStateException("ensureResultMapping");
        }
        return value.booleanValue();
    }

    public SqlLogType getSqlLogValue() {
        VariableElement enumConstant = AnnotationValueUtil
                .toEnumConstant(sqlLog);
        if (enumConstant == null) {
            throw new AptIllegalStateException("sqlLog");
        }
        return SqlLogType.valueOf(enumConstant.getSimpleName().toString());
    }

}

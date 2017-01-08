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
import javax.lang.model.element.VariableElement;

import org.seasar.doma.MapInsert;
import org.seasar.doma.internal.apt.util.ElementUtil;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.jdbc.SqlLogType;

/**
 * @author bakenezumi
 *
 */
public class MapInsertMirror {

    protected final AnnotationMirror annotationMirror;

    protected AnnotationValue queryTimeout;

    protected AnnotationValue sqlLog;

    protected MapInsertMirror(AnnotationMirror annotationMirror) {
        assertNotNull(annotationMirror);
        this.annotationMirror = annotationMirror;
    }

    public AnnotationMirror getAnnotationMirror() {
        return annotationMirror;
    }

    public AnnotationValue getQueryTimeout() {
        return queryTimeout;
    }

    public AnnotationValue getSqlLog() {
        return sqlLog;
    }

    public int getQueryTimeoutValue() {
        Integer value = AnnotationValueUtil.toInteger(queryTimeout);
        if (value == null) {
            throw new AptIllegalStateException("queryTimeout");
        }
        return value.intValue();
    }

    public SqlLogType getSqlLogValue() {
        VariableElement enumConstant = AnnotationValueUtil
                                       .toEnumConstant(sqlLog);
        if (enumConstant == null) {
            throw new AptIllegalStateException("sqlLog");
        }
        return SqlLogType.valueOf(enumConstant.getSimpleName().toString());
    }

    public static MapInsertMirror newInstance(ExecutableElement method,
            ProcessingEnvironment env) {
        assertNotNull(env);
        AnnotationMirror annotationMirror = ElementUtil.getAnnotationMirror(
                                                method, MapInsert.class, env);
        if (annotationMirror == null) {
            return null;
        }
        MapInsertMirror result = new MapInsertMirror(annotationMirror);
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : env
                .getElementUtils()
                .getElementValuesWithDefaults(annotationMirror).entrySet()) {
            String name = entry.getKey().getSimpleName().toString();
            AnnotationValue value = entry.getValue();
            if ("queryTimeout".equals(name)) {
                result.queryTimeout = value;
            } else if ("sqlLog".equals(name)) {
                result.sqlLog = value;
            } else if ("tableName".equals(name)) {
                result.tableName = value;
            }
        }
        return result;
    }

    protected AnnotationValue tableName;

    public String getTableNameValue() {
        String value = AnnotationValueUtil.toString(tableName);
        if (value == null) {
            throw new AptIllegalStateException("tableName");
        }
        return value;
    }
}

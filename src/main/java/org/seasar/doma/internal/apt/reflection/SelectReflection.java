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

import org.seasar.doma.FetchType;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.SelectType;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.jdbc.SqlLogType;

/**
 * @author taedium
 * 
 */
public class SelectReflection {

    protected final AnnotationMirror annotationMirror;

    protected final AnnotationValue strategy;

    protected final AnnotationValue fetch;

    protected final AnnotationValue ensureResult;

    protected final AnnotationValue ensureResultMapping;

    protected final AnnotationValue queryTimeout;

    protected final AnnotationValue fetchSize;

    protected final AnnotationValue maxRows;

    protected final AnnotationValue mapKeyNaming;

    protected final AnnotationValue sqlLog;

    protected SelectReflection(AnnotationMirror annotationMirror,
            Map<String, AnnotationValue> values) {
        assertNotNull(annotationMirror, values);
        this.annotationMirror = annotationMirror;
        this.strategy = assertNotNullValue(values, "strategy");
        this.fetch = assertNotNullValue(values, "fetch");
        this.ensureResult = assertNotNullValue(values, "ensureResult");
        this.ensureResultMapping = assertNotNullValue(values,
                "ensureResultMapping");
        this.queryTimeout = assertNotNullValue(values, "queryTimeout");
        this.fetchSize = assertNotNullValue(values, "fetchSize");
        this.maxRows = assertNotNullValue(values, "maxRows");
        this.mapKeyNaming = assertNotNullValue(values, "mapKeyNaming");
        this.sqlLog = assertNotNullValue(values, "sqlLog");
    }

    public AnnotationValue getStrategy() {
        return strategy;
    }

    public AnnotationValue getFetch() {
        return fetch;
    }

    public AnnotationValue getEnsureResult() {
        return ensureResult;
    }

    public AnnotationValue getEnsureResultMapping() {
        return ensureResultMapping;
    }

    public AnnotationValue getQueryTimeout() {
        return queryTimeout;
    }

    public AnnotationValue getFetchSize() {
        return fetchSize;
    }

    public AnnotationValue getMaxRows() {
        return maxRows;
    }

    public AnnotationValue getMapKeyNaming() {
        return mapKeyNaming;
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

    public int getFetchSizeValue() {
        Integer value = AnnotationValueUtil.toInteger(fetchSize);
        if (value == null) {
            throw new AptIllegalStateException("fetchSize");
        }
        return value.intValue();
    }

    public int getMaxRowsValue() {
        Integer value = AnnotationValueUtil.toInteger(maxRows);
        if (value == null) {
            throw new AptIllegalStateException("maxRows");
        }
        return value.intValue();
    }

    public SelectType getStrategyValue() {
        VariableElement enumConstant = AnnotationValueUtil
                .toEnumConstant(strategy);
        if (enumConstant == null) {
            throw new AptIllegalStateException("strategy");
        }
        return SelectType.valueOf(enumConstant.getSimpleName().toString());
    }

    public FetchType getFetchValue() {
        VariableElement enumConstant = AnnotationValueUtil
                .toEnumConstant(fetch);
        if (enumConstant == null) {
            throw new AptIllegalStateException("fetch");
        }
        return FetchType.valueOf(enumConstant.getSimpleName().toString());
    }

    public boolean getEnsureResultValue() {
        Boolean value = AnnotationValueUtil.toBoolean(ensureResult);
        if (value == null) {
            throw new AptIllegalStateException("ensureResult");
        }
        return value.booleanValue();
    }

    public boolean getEnsureResultMappingValue() {
        Boolean value = AnnotationValueUtil.toBoolean(ensureResultMapping);
        if (value == null) {
            throw new AptIllegalStateException("ensureResultMapping");
        }
        return value.booleanValue();
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

    public SqlLogType getSqlLogValue() {
        VariableElement enumConstant = AnnotationValueUtil
                .toEnumConstant(sqlLog);
        if (enumConstant == null) {
            throw new AptIllegalStateException("sqlLog");
        }
        return SqlLogType.valueOf(enumConstant.getSimpleName().toString());
    }

    public AnnotationMirror getAnnotationMirror() {
        return annotationMirror;
    }

}

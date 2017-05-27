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

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNullValue;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.VariableElement;

import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.jdbc.SqlLogType;

/**
 * @author taedium
 * 
 */
public abstract class ModifyReflection {

    protected final AnnotationMirror annotationMirror;

    protected final AnnotationValue sqlFile;

    protected final AnnotationValue queryTimeout;

    protected final AnnotationValue sqlLog;

    protected final AnnotationValue ignoreVersion;

    protected final AnnotationValue excludeNull;

    protected final AnnotationValue suppressOptimisticLockException;

    protected final AnnotationValue includeUnchanged;

    protected final AnnotationValue include;

    protected final AnnotationValue exclude;


    protected ModifyReflection(AnnotationMirror annotationMirror,
            Map<String, AnnotationValue> values) {
        assertNotNull(annotationMirror, values);

        this.annotationMirror = annotationMirror;

        // non null values
        this.sqlFile = assertNotNullValue(values, "sqlFile");
        this.queryTimeout = assertNotNullValue(values, "queryTimeout");
        this.sqlLog = assertNotNullValue(values, "sqlLog");

        // nullable values
        this.ignoreVersion = values.get("ignoreVersion");
        this.excludeNull = values.get("excludeNull");
        this.suppressOptimisticLockException = values
                .get("suppressOptimisticLockException");
        this.includeUnchanged = values.get("includeUnchanged");
        this.include = values.get("include");
        this.exclude = values.get("exclude");
    }

    public AnnotationMirror getAnnotationMirror() {
        return annotationMirror;
    }

    public AnnotationValue getSqlFile() {
        return sqlFile;
    }

    public AnnotationValue getQueryTimeout() {
        return queryTimeout;
    }

    public AnnotationValue getIgnoreVersion() {
        return ignoreVersion;
    }

    public AnnotationValue getExcludeNull() {
        return excludeNull;
    }

    public AnnotationValue getSuppressOptimisticLockException() {
        return suppressOptimisticLockException;
    }

    public AnnotationValue getIncludeUnchanged() {
        return includeUnchanged;
    }

    public AnnotationValue getInclude() {
        return include;
    }

    public AnnotationValue getExclude() {
        return exclude;
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

    public Boolean getIgnoreVersionValue() {
        return AnnotationValueUtil.toBoolean(ignoreVersion);
    }

    public Boolean getExcludeNullValue() {
        return AnnotationValueUtil.toBoolean(excludeNull);
    }

    public Boolean getSuppressOptimisticLockExceptionValue() {
        return AnnotationValueUtil.toBoolean(suppressOptimisticLockException);
    }

    public Boolean getIncludeUnchangedValue() {
        return AnnotationValueUtil.toBoolean(includeUnchanged);
    }

    public List<String> getIncludeValue() {
        return AnnotationValueUtil.toStringList(include);
    }

    public List<String> getExcludeValue() {
        return AnnotationValueUtil.toStringList(exclude);
    }

    public SqlLogType getSqlLogValue() {
        VariableElement enumConstant = AnnotationValueUtil
                .toEnumConstant(sqlLog);
        if (enumConstant == null) {
            throw new AptIllegalStateException("sqlLog");
        }
        return SqlLogType.valueOf(enumConstant.getSimpleName().toString());
    }

    public boolean getSqlFileValue() {
        Boolean value = AnnotationValueUtil.toBoolean(sqlFile);
        if (value == null) {
            throw new AptIllegalStateException("sqlFile");
        }
        return value.booleanValue();
    }

}

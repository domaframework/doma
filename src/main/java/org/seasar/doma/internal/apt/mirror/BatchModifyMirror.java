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

import java.util.List;

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
public abstract class BatchModifyMirror {

    protected final AnnotationMirror annotationMirror;

    protected AnnotationValue sqlFile;

    protected AnnotationValue queryTimeout;

    protected AnnotationValue batchSize;

    protected AnnotationValue ignoreVersion;

    protected AnnotationValue suppressOptimisticLockException;

    protected AnnotationValue include;

    protected AnnotationValue exclude;

    protected AnnotationValue sqlLog;

    protected BatchModifyMirror(AnnotationMirror annotationMirror) {
        assertNotNull(annotationMirror);
        this.annotationMirror = annotationMirror;
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

    public AnnotationValue getBatchSize() {
        return batchSize;
    }

    public AnnotationValue getIgnoreVersion() {
        return ignoreVersion;
    }

    public AnnotationValue getSuppressOptimisticLockException() {
        return suppressOptimisticLockException;
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
        return value;
    }

    public int getBatchSizeValue() {
        Integer value = AnnotationValueUtil.toInteger(batchSize);
        if (value == null) {
            throw new AptIllegalStateException("batchSize");
        }
        return value;
    }

    public Boolean getIgnoreVersionValue() {
        return AnnotationValueUtil.toBoolean(ignoreVersion);
    }

    public Boolean getSuppressOptimisticLockExceptionValue() {
        return AnnotationValueUtil.toBoolean(suppressOptimisticLockException);
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

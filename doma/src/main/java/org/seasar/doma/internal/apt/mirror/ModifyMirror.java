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

import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;

import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;

/**
 * @author taedium
 * 
 */
public abstract class ModifyMirror {

    protected final AnnotationMirror annotationMirror;

    protected AnnotationValue sqlFile;

    protected AnnotationValue queryTimeout;

    protected AnnotationValue ignoreVersion;

    protected AnnotationValue includeVersion;

    protected AnnotationValue excludeNull;

    protected AnnotationValue suppressOptimisticLockException;

    protected AnnotationValue includeUnchanged;

    protected AnnotationValue include;

    protected AnnotationValue exclude;

    protected ModifyMirror(AnnotationMirror annotationMirror) {
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

    public AnnotationValue getIgnoreVersion() {
        return ignoreVersion;
    }

    public AnnotationValue getIncludeVersion() {
        return includeVersion;
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

    public Boolean getIncludeVersionValue() {
        return AnnotationValueUtil.toBoolean(includeVersion);
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

    public boolean getSqlFileValue() {
        Boolean value = AnnotationValueUtil.toBoolean(sqlFile);
        if (value == null) {
            throw new AptIllegalStateException("sqlFile");
        }
        return value.booleanValue();
    }

}

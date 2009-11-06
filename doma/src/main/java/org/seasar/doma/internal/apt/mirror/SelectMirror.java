/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import org.seasar.doma.Select;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.internal.apt.util.ElementUtil;

/**
 * @author taedium
 * 
 */
public class SelectMirror {

    protected final AnnotationMirror annotationMirror;

    protected AnnotationValue iterate;

    protected AnnotationValue queryTimeout;

    protected AnnotationValue fetchSize;

    protected AnnotationValue maxRows;

    protected SelectMirror(AnnotationMirror annotationMirror) {
        this.annotationMirror = annotationMirror;
    }

    public AnnotationValue getIterate() {
        return iterate;
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

    public boolean isIterate() {
        return AnnotationValueUtil.isEqual(Boolean.TRUE, iterate);
    }

    public static SelectMirror newInstance(ExecutableElement method,
            ProcessingEnvironment env) {
        assertNotNull(env);
        AnnotationMirror annotationMirror = ElementUtil.getAnnotationMirror(
                method, Select.class, env);
        if (annotationMirror == null) {
            return null;
        }
        SelectMirror result = new SelectMirror(annotationMirror);
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : env
                .getElementUtils().getElementValuesWithDefaults(
                        annotationMirror).entrySet()) {
            String name = entry.getKey().getSimpleName().toString();
            AnnotationValue value = entry.getValue();
            if ("iterate".equals(name)) {
                result.iterate = value;
            } else if ("queryTimeout".equals(name)) {
                result.queryTimeout = value;
            } else if ("fetchSize".equals(name)) {
                result.fetchSize = value;
            } else if ("maxRows".equals(name)) {
                result.maxRows = value;
            }
        }
        return result;
    }

}

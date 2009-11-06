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

import org.seasar.doma.Procedure;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.internal.apt.util.ElementUtil;

/**
 * @author taedium
 * 
 */
public class ProcedureMirror {

    protected final AnnotationMirror annotationMirror;

    protected AnnotationValue catalog;

    protected AnnotationValue schema;

    protected AnnotationValue name;

    protected AnnotationValue queryTimeout;

    protected ProcedureMirror(AnnotationMirror annotationMirror) {
        this.annotationMirror = annotationMirror;
    }

    public static ProcedureMirror newInstance(ExecutableElement method,
            ProcessingEnvironment env) {
        assertNotNull(env);
        AnnotationMirror annotationMirror = ElementUtil.getAnnotationMirror(
                method, Procedure.class, env);
        if (annotationMirror == null) {
            return null;
        }
        ProcedureMirror result = new ProcedureMirror(annotationMirror);
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : env
                .getElementUtils().getElementValuesWithDefaults(
                        annotationMirror).entrySet()) {
            String name = entry.getKey().getSimpleName().toString();
            AnnotationValue value = entry.getValue();
            if ("catalog".equals(name)) {
                result.catalog = value;
            } else if ("schema".equals(name)) {
                result.schema = value;
            } else if ("name".equals(name)) {
                result.name = value;
            } else if ("queryTimeout".equals(name)) {
                result.queryTimeout = value;
            }
        }
        return result;
    }

    public String getProcedureName() {
        String catalogValue = AnnotationValueUtil.toString(this.catalog);
        String schemaValue = AnnotationValueUtil.toString(this.schema);
        String nameValue = AnnotationValueUtil.toString(this.name);

        StringBuilder buf = new StringBuilder();
        if (catalogValue != null && !catalogValue.isEmpty()) {
            buf.append(catalogValue);
            buf.append(".");
        }
        if (schemaValue != null && !schemaValue.isEmpty()) {
            buf.append(schemaValue);
            buf.append(".");
        }
        if (nameValue != null && !nameValue.isEmpty()) {
            buf.append(nameValue);
        }
        return buf.toString();
    }

    public AnnotationValue getQueryTimeout() {
        return queryTimeout;
    }

}

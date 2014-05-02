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

import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.Procedure;
import org.seasar.doma.internal.apt.AptIllegalStateException;
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

    protected AnnotationValue quote;

    protected AnnotationValue queryTimeout;

    protected String defaultName;

    protected AnnotationValue mapKeyNaming;

    protected ProcedureMirror(AnnotationMirror annotationMirror,
            String defaultName) {
        assertNotNull(annotationMirror, defaultName);
        this.annotationMirror = annotationMirror;
        this.defaultName = defaultName;
    }

    public static ProcedureMirror newInstance(ExecutableElement method,
            ProcessingEnvironment env) {
        assertNotNull(env);
        AnnotationMirror annotationMirror = ElementUtil.getAnnotationMirror(
                method, Procedure.class, env);
        if (annotationMirror == null) {
            return null;
        }
        ProcedureMirror result = new ProcedureMirror(annotationMirror, method
                .getSimpleName().toString());
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : env
                .getElementUtils()
                .getElementValuesWithDefaults(annotationMirror).entrySet()) {
            String name = entry.getKey().getSimpleName().toString();
            AnnotationValue value = entry.getValue();
            if ("catalog".equals(name)) {
                result.catalog = value;
            } else if ("schema".equals(name)) {
                result.schema = value;
            } else if ("name".equals(name)) {
                result.name = value;
            } else if ("quote".equals(name)) {
                result.quote = value;
            } else if ("queryTimeout".equals(name)) {
                result.queryTimeout = value;
            } else if ("mapKeyNaming".equals(name)) {
                result.mapKeyNaming = value;
            }
        }
        return result;
    }

    public AnnotationValue getQueryTimeout() {
        return queryTimeout;
    }

    public AnnotationValue getMapKeyNaming() {
        return mapKeyNaming;
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
        return value;
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

}

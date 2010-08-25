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
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.Dao;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.internal.apt.util.ElementUtil;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.jdbc.Config;

/**
 * @author taedium
 * 
 */
public class DaoMirror {

    protected final AnnotationMirror annotationMirror;

    protected final ProcessingEnvironment env;

    protected AnnotationValue config;

    protected TypeMirror configValue;

    protected DaoMirror(AnnotationMirror annotationMirror,
            ProcessingEnvironment env) {
        assertNotNull(annotationMirror, env);
        this.annotationMirror = annotationMirror;
        this.env = env;
    }

    public static DaoMirror newInstance(TypeElement interfase,
            ProcessingEnvironment env) {
        assertNotNull(env);
        AnnotationMirror annotationMirror = ElementUtil.getAnnotationMirror(
                interfase, Dao.class, env);
        if (annotationMirror == null) {
            return null;
        }
        DaoMirror result = new DaoMirror(annotationMirror, env);
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : env
                .getElementUtils()
                .getElementValuesWithDefaults(annotationMirror).entrySet()) {
            String name = entry.getKey().getSimpleName().toString();
            AnnotationValue value = entry.getValue();
            if ("config".equals(name)) {
                result.config = value;
                result.configValue = AnnotationValueUtil.toType(value);
                if (result.configValue == null) {
                    throw new AptIllegalStateException("config");
                }
            }
        }
        return result;
    }

    public AnnotationMirror getAnnotationMirror() {
        return annotationMirror;
    }

    public AnnotationValue getConfig() {
        return config;
    }

    public TypeMirror getConfigValue() {
        TypeMirror value = AnnotationValueUtil.toType(config);
        if (value == null) {
            throw new AptIllegalStateException("config");
        }
        return value;
    }

    public boolean hasUserDefinedConfig() {
        return !TypeMirrorUtil.isSameType(configValue, Config.class, env);
    }

}

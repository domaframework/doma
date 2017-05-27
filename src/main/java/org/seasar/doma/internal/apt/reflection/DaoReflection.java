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

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.AccessLevel;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.internal.apt.util.TypeMirrorUtil;
import org.seasar.doma.jdbc.Config;

/**
 * @author taedium
 * 
 */
public class DaoReflection {

    protected final AnnotationMirror annotationMirror;

    protected final ProcessingEnvironment env;

    protected final AnnotationValue config;

    protected final AnnotationValue accessLevel;

    protected DaoReflection(AnnotationMirror annotationMirror,
            ProcessingEnvironment env, Map<String, AnnotationValue> values) {
        assertNotNull(annotationMirror, env, values);
        this.annotationMirror = annotationMirror;
        this.env = env;
        this.config = assertNotNullValue(values, "config");
        this.accessLevel = assertNotNullValue(values, "accessLevel");
    }

    public AnnotationMirror getAnnotationMirror() {
        return annotationMirror;
    }

    public AnnotationValue getConfig() {
        return config;
    }

    public AnnotationValue getAccessLevel() {
        return accessLevel;
    }

    public TypeMirror getConfigValue() {
        return getConfigValueInternal();
    }

    protected TypeMirror getConfigValueInternal() {
        TypeMirror value = AnnotationValueUtil.toType(config);
        if (value == null) {
            throw new AptIllegalStateException("config");
        }
        return value;
    }

    public AccessLevel getAccessLevelValue() {
        VariableElement enumConstant = AnnotationValueUtil
                .toEnumConstant(accessLevel);
        if (enumConstant == null) {
            throw new AptIllegalStateException("accessLevel");
        }
        return AccessLevel.valueOf(enumConstant.getSimpleName().toString());
    }

    public boolean hasUserDefinedConfig() {
        TypeMirror configValue = getConfigValueInternal();
        return !TypeMirrorUtil.isSameType(configValue, Config.class, env);
    }

}

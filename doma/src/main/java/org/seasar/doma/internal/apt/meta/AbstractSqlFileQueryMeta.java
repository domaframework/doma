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
package org.seasar.doma.internal.apt.meta;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.lang.model.type.TypeMirror;

/**
 * @author taedium
 * 
 */
public abstract class AbstractSqlFileQueryMeta extends AbstractQueryMeta
        implements SqlFileQueryMeta {

    protected Map<String, TypeMirror> methodParameterTypes = new LinkedHashMap<String, TypeMirror>();

    public TypeMirror getMethodParameterType(String parameterName) {
        return methodParameterTypes.get(parameterName);
    }

    public void addMethodParameterType(String parameterName, TypeMirror parameterType) {
        this.methodParameterTypes.put(parameterName, parameterType);
    }

}

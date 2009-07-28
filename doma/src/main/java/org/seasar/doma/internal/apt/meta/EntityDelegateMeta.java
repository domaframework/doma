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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.lang.model.type.TypeMirror;

/**
 * @author taedium
 * 
 */
public class EntityDelegateMeta extends AbstractEntityMethodMeta {

    protected TypeMirror targetType;

    protected Map<String, String> methodParameters = new LinkedHashMap<String, String>();

    public TypeMirror getTargetType() {
        return targetType;
    }

    public void setTargetType(TypeMirror targetType) {
        this.targetType = targetType;
    }

    public Iterator<Map.Entry<String, String>> getMethodParameters() {
        return methodParameters.entrySet().iterator();
    }

    public void addMethodParameter(String parameterName,
            String parameterTypeName) {
        this.methodParameters.put(parameterName, parameterTypeName);
    }
}

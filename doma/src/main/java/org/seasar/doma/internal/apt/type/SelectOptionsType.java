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
package org.seasar.doma.internal.apt.type;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.TypeUtil;
import org.seasar.doma.jdbc.SelectOptions;

public class SelectOptionsType {

    protected TypeMirror type;

    protected String typeName;

    protected SelectOptionsType() {
    }

    public TypeMirror getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }

    public static SelectOptionsType newInstance(TypeMirror type,
            ProcessingEnvironment env) {
        assertNotNull(type, env);
        if (!TypeUtil.isAssignable(type, SelectOptions.class, env)) {
            return null;
        }
        SelectOptionsType selectOptionsType = new SelectOptionsType();
        selectOptionsType.type = type;
        selectOptionsType.typeName = TypeUtil.getTypeName(type, env);
        return selectOptionsType;
    }
}

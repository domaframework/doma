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
package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.mirror.DaoMirror;

/**
 * @author taedium
 * 
 */
public class ParentDaoMeta {

    protected final DaoMirror daoMirror;

    protected TypeMirror daoType;

    protected TypeElement daoElement;

    protected String name;

    public ParentDaoMeta(DaoMirror daoMirror) {
        assertNotNull(daoMirror);
        this.daoMirror = daoMirror;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeMirror getDaoType() {
        return daoType;
    }

    public void setDaoType(TypeMirror daoType) {
        this.daoType = daoType;
    }

    public TypeElement getDaoElement() {
        return daoElement;
    }

    public void setDaoElement(TypeElement daoElement) {
        this.daoElement = daoElement;
    }

    public boolean hasUserDefinedConfig() {
        return daoMirror.hasUserDefinedConfig();
    }

    DaoMirror getDaoMirror() {
        return daoMirror;
    }

    public TypeMirror getConfigType() {
        return daoMirror.getConfigValue();
    }

}

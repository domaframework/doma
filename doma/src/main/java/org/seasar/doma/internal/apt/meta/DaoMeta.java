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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.jdbc.Config;

/**
 * 
 * @author taedium
 * 
 */
public class DaoMeta {

    protected final Deque<Map<TypeMirror, TypeMirror>> typeParamMapStack = new LinkedList<Map<TypeMirror, TypeMirror>>();

    protected final List<QueryMeta> queryMetas = new ArrayList<QueryMeta>();

    protected TypeMirror configType;

    protected TypeMirror daoType;

    protected TypeElement daoElement;

    protected String name;

    protected Config config;

    public DaoMeta() {
        typeParamMapStack.push(Collections.<TypeMirror, TypeMirror> emptyMap());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeMirror getConfigType() {
        return configType;
    }

    public void setConfigType(TypeMirror configType) {
        this.configType = configType;
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

    public void addQueryMeta(QueryMeta queryMeta) {
        queryMetas.add(queryMeta);
    }

    public List<QueryMeta> getQueryMetas() {
        return queryMetas;
    }

    public void addTypeParameterMap(Map<TypeMirror, TypeMirror> typeParameterMap) {
        typeParamMapStack.push(typeParameterMap);
    }

    public Map<TypeMirror, TypeMirror> getTypeParameterMap() {
        return typeParamMapStack.peek();
    }

}

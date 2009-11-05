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

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.jdbc.command.Command;
import org.seasar.doma.internal.jdbc.query.Query;

/**
 * @author taedium
 * 
 */
public abstract class AbstractQueryMeta implements QueryMeta {

    protected String name;

    protected ExecutableElement executableElement;

    protected List<String> typeParameterNames = new ArrayList<String>();

    protected List<String> thrownTypeNames = new ArrayList<String>();

    protected QueryKind queryKind;

    protected AnnotationMirror annotationMirror;

    protected AnnotationValue sqlFile;

    protected AnnotationValue iterate;

    protected AnnotationValue queryTimeout;

    protected AnnotationValue maxRows;

    protected AnnotationValue fetchSize;

    protected AnnotationValue ignoreVersion;

    protected AnnotationValue includeVersion;

    protected AnnotationValue excludeNull;

    protected AnnotationValue suppressOptimisticLockException;

    protected AnnotationValue includeUnchanged;

    protected AnnotationValue include;

    protected AnnotationValue exclude;

    protected Map<String, TypeMirror> bindableParameterTypeMap = new LinkedHashMap<String, TypeMirror>();

    protected QueryReturnMeta returnMeta;

    protected List<QueryParameterMeta> parameterMetas = new ArrayList<QueryParameterMeta>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExecutableElement getExecutableElement() {
        return executableElement;
    }

    public void setExecutableElement(ExecutableElement executableElement) {
        this.executableElement = executableElement;
    }

    public void addTypeParameterName(String typeParameterName) {
        typeParameterNames.add(typeParameterName);
    }

    public List<String> getTypeParameterNames() {
        return typeParameterNames;
    }

    public void addThrownTypeName(String thrownTypeName) {
        thrownTypeNames.add(thrownTypeName);
    }

    public List<String> getThrownTypeNames() {
        return thrownTypeNames;
    }

    public Class<? extends Query> getQueryClass() {
        if (queryKind == null) {
            return null;
        }
        return queryKind.getQueryClass();
    }

    @SuppressWarnings("unchecked")
    public Class<? extends Command> getCommandClass() {
        if (queryKind == null) {
            return null;
        }
        return queryKind.getCommandClass();
    }

    public QueryKind getQueryKind() {
        return queryKind;
    }

    public void setQueryKind(QueryKind queryKind) {
        this.queryKind = queryKind;
    }

    public AnnotationValue getSqlFile() {
        return sqlFile;
    }

    public AnnotationValue getQueryTimeout() {
        return queryTimeout;
    }

    public AnnotationValue getIgnoreVersion() {
        return ignoreVersion;
    }

    public AnnotationValue getIncludeVersion() {
        return includeVersion;
    }

    public AnnotationValue getSuppressOptimisticLockException() {
        return suppressOptimisticLockException;
    }

    public AnnotationValue getExcludeNull() {
        return excludeNull;
    }

    public Map<String, TypeMirror> getParameterTypeMap() {
        return bindableParameterTypeMap;
    }

    public void addBindableParameterType(String parameterName,
            TypeMirror bindableParameterTypeMap) {
        this.bindableParameterTypeMap.put(parameterName,
                bindableParameterTypeMap);
    }

    public AnnotationValue getIncludeUnchanged() {
        return includeUnchanged;
    }

    public AnnotationValue getInclude() {
        return include;
    }

    public AnnotationValue getExclude() {
        return exclude;
    }

    public QueryReturnMeta getReturnMeta() {
        return returnMeta;
    }

    public void setReturnMeta(QueryReturnMeta returnMeta) {
        this.returnMeta = returnMeta;
    }

    public List<QueryParameterMeta> getParameterMetas() {
        return parameterMetas;
    }

    public void addParameterMeta(QueryParameterMeta queryParameterMeta) {
        this.parameterMetas.add(queryParameterMeta);
    }

    public AnnotationValue getIterate() {
        return iterate;
    }

    public AnnotationValue getMaxRows() {
        return maxRows;
    }

    public AnnotationValue getFetchSize() {
        return fetchSize;
    }

    public void setAnnotationMirror(AnnotationMirror mirror,
            ProcessingEnvironment env) {
        assertNotNull(mirror, env);
        this.annotationMirror = mirror;
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : env
                .getElementUtils().getElementValuesWithDefaults(mirror)
                .entrySet()) {
            String name = entry.getKey().getSimpleName().toString();
            if ("sqlFile".equals(name)) {
                sqlFile = entry.getValue();
            } else if ("iterate".equals(name)) {
                iterate = entry.getValue();
            } else if ("queryTimeout".equals(name)) {
                queryTimeout = entry.getValue();
            } else if ("fetchSize".equals(name)) {
                fetchSize = entry.getValue();
            } else if ("maxRows".equals(name)) {
                maxRows = entry.getValue();
            } else if ("ignoreVersion".equals(name)) {
                ignoreVersion = entry.getValue();
            } else if ("includeVersion".equals(name)) {
                includeVersion = entry.getValue();
            } else if ("excludeNull".equals(name)) {
                excludeNull = entry.getValue();
            } else if ("suppressOptimisticLockException".equals(name)) {
                suppressOptimisticLockException = entry.getValue();
            } else if ("includeUnchanged".equals(name)) {
                includeUnchanged = entry.getValue();
            } else if ("include".equals(name)) {
                include = entry.getValue();
            } else if ("exclude".equals(name)) {
                exclude = entry.getValue();
            }
        }
    }

    public AnnotationMirror getAnnotationMirror() {
        return annotationMirror;
    }

}

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

import javax.lang.model.type.TypeMirror;

import org.seasar.doma.jdbc.Options;
import org.seasar.doma.jdbc.SelectOptions;

/**
 * @author taedium
 * 
 */
public class SqlFileSelectQueryMeta extends AbstractSqlFileQueryMeta {

    protected String optionsTypeName;

    protected String optionsName;

    protected String iterationCallbackTypeName;

    protected String iterationCallbackName;

    protected TypeMirror iterationCallbackResultType;

    protected TypeMirror iterationCallbackTargetType;

    protected boolean iterated;

    protected Integer maxRows;

    protected Integer fetchSize;

    public String getIterationCallbackTypeName() {
        return iterationCallbackTypeName;
    }

    public void setIterationCallbackTypeName(String iterationCallbackTypeName) {
        this.iterationCallbackTypeName = iterationCallbackTypeName;
    }

    public String getIterationCallbackName() {
        return iterationCallbackName;
    }

    public void setIterationCallbackName(String iterationCallbackName) {
        this.iterationCallbackName = iterationCallbackName;
    }

    public TypeMirror getIterationCallbackResultType() {
        return iterationCallbackResultType;
    }

    public void setIterationCallbackResultType(
            TypeMirror iterationCallbackResultType) {
        this.iterationCallbackResultType = iterationCallbackResultType;
    }

    public TypeMirror getIterationCallbackTargetType() {
        return iterationCallbackTargetType;
    }

    public void setIterationCallbackTargetType(
            TypeMirror iterationCallbackTargetType) {
        this.iterationCallbackTargetType = iterationCallbackTargetType;
    }

    public String getOptionsTypeName() {
        return optionsTypeName;
    }

    public void setOptionsTypeName(String optionsTypeName) {
        this.optionsTypeName = optionsTypeName;
    }

    public String getOptionsName() {
        return optionsName;
    }

    public void setOptionsName(String optionsName) {
        this.optionsName = optionsName;
    }

    public Class<? extends Options> getOptionsClass() {
        return SelectOptions.class;
    }

    public boolean isIterated() {
        return iterated;
    }

    public void setIterated(boolean iterated) {
        this.iterated = iterated;
    }

    public Integer getMaxRows() {
        return maxRows;
    }

    public void setMaxRows(Integer maxRows) {
        this.maxRows = maxRows;
    }

    public Integer getFetchSize() {
        return fetchSize;
    }

    public void setFetchSize(Integer fetchSize) {
        this.fetchSize = fetchSize;
    }

    @Override
    public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
        return visitor.visistSqlFileSelectQueryMeta(this, p);
    }

}

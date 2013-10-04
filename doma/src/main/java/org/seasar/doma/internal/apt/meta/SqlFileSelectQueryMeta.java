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

import javax.lang.model.element.ExecutableElement;

import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.internal.apt.cttype.IterationCallbackCtType;
import org.seasar.doma.internal.apt.cttype.SelectOptionsCtType;
import org.seasar.doma.internal.apt.mirror.SelectMirror;

/**
 * @author taedium
 * 
 */
public class SqlFileSelectQueryMeta extends AbstractSqlFileQueryMeta {

    protected SelectMirror selectMirror;

    protected String iterationCallbackPrameterName;

    protected IterationCallbackCtType iterationCallbackCtType;

    protected String selectOptionsParameterName;

    protected SelectOptionsCtType selectOptionsCtType;

    public SqlFileSelectQueryMeta(ExecutableElement method) {
        super(method);
    }

    public String getIterationCallbackPrameterName() {
        return iterationCallbackPrameterName;
    }

    public void setIterationCallbackPrameterName(
            String iterationCallbackPrameterName) {
        this.iterationCallbackPrameterName = iterationCallbackPrameterName;
    }

    public IterationCallbackCtType getIterationCallbackCtType() {
        return iterationCallbackCtType;
    }

    public void setIterationCallbackCtType(
            IterationCallbackCtType iterationCallbackCtType) {
        this.iterationCallbackCtType = iterationCallbackCtType;
    }

    public String getSelectOptionsParameterName() {
        return selectOptionsParameterName;
    }

    public void setSelectOptionsParameterName(String selectOptionsParameterName) {
        this.selectOptionsParameterName = selectOptionsParameterName;
    }

    public SelectOptionsCtType getSelectOptionsCtType() {
        return selectOptionsCtType;
    }

    public void setSelectOptionsCtType(SelectOptionsCtType selectOptionsCtType) {
        this.selectOptionsCtType = selectOptionsCtType;
    }

    void setSelectMirror(SelectMirror selectMirror) {
        this.selectMirror = selectMirror;
    }

    SelectMirror getSelectMirror() {
        return selectMirror;
    }

    public int getFetchSize() {
        return selectMirror.getFetchSizeValue();
    }

    public int getMaxRows() {
        return selectMirror.getMaxRowsValue();
    }

    public int getQueryTimeout() {
        return selectMirror.getQueryTimeoutValue();
    }

    public boolean getIterate() {
        return selectMirror.getIterateValue();
    }

    public boolean getEnsureResult() {
        return selectMirror.getEnsureResultValue();
    }

    public boolean getEnsureResultMapping() {
        return selectMirror.getEnsureResultMappingValue();
    }

    public MapKeyNamingType getMapKeyNamingType() {
        return selectMirror.getMapKeyNamingValue();
    }

    @Override
    public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
        return visitor.visitSqlFileSelectQueryMeta(this, p);
    }

}

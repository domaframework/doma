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

import org.seasar.doma.FetchType;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.SelectType;
import org.seasar.doma.internal.apt.cttype.CollectorCtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.FunctionCtType;
import org.seasar.doma.internal.apt.cttype.SelectOptionsCtType;
import org.seasar.doma.internal.apt.mirror.SelectMirror;
import org.seasar.doma.jdbc.SqlLogType;

/**
 * @author taedium
 * 
 */
public class SqlFileSelectQueryMeta extends AbstractSqlFileQueryMeta {

    protected SelectMirror selectMirror;

    protected String functionParameterName;

    protected FunctionCtType functionCtType;

    protected String collectorParameterName;

    protected CollectorCtType collectorCtType;

    protected String selectOptionsParameterName;

    protected SelectOptionsCtType selectOptionsCtType;

    protected EntityCtType entityCtType;

    public SqlFileSelectQueryMeta(ExecutableElement method) {
        super(method);
    }

    public String getFunctionParameterName() {
        return functionParameterName;
    }

    public void setFunctionParameterName(String functionParameterName) {
        this.functionParameterName = functionParameterName;
    }

    public FunctionCtType getFunctionCtType() {
        return functionCtType;
    }

    public void setFunctionCtType(FunctionCtType functionCtType) {
        this.functionCtType = functionCtType;
    }

    public String getCollectorParameterName() {
        return collectorParameterName;
    }

    public void setCollectorParameterName(String collectorParameterName) {
        this.collectorParameterName = collectorParameterName;
    }

    public CollectorCtType getCollectorCtType() {
        return collectorCtType;
    }

    public void setCollectorCtType(CollectorCtType collectorCtType) {
        this.collectorCtType = collectorCtType;
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

    public EntityCtType getEntityCtType() {
        return entityCtType;
    }

    public void setEntityCtType(EntityCtType entityCtType) {
        this.entityCtType = entityCtType;
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

    public SelectType getSelectStrategyType() {
        return selectMirror.getStrategyValue();
    }

    public FetchType getFetchType() {
        return selectMirror.getFetchValue();
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

    public SqlLogType getSqlLogType() {
        return selectMirror.getSqlLogValue();
    }

    @Override
    public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
        return visitor.visitSqlFileSelectQueryMeta(this, p);
    }

}

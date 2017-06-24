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
import javax.lang.model.element.TypeElement;

import org.seasar.doma.FetchType;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.SelectType;
import org.seasar.doma.internal.apt.cttype.CollectorCtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.FunctionCtType;
import org.seasar.doma.internal.apt.cttype.SelectOptionsCtType;
import org.seasar.doma.internal.apt.reflection.SelectReflection;
import org.seasar.doma.jdbc.SqlLogType;

/**
 * @author taedium
 * 
 */
public class SqlFileSelectQueryMeta extends AbstractSqlFileQueryMeta {

    private SelectReflection selectReflection;

    private String functionParameterName;

    private FunctionCtType functionCtType;

    private String collectorParameterName;

    private CollectorCtType collectorCtType;

    private String selectOptionsParameterName;

    private SelectOptionsCtType selectOptionsCtType;

    private EntityCtType entityCtType;

    private boolean resultStream;

    public SqlFileSelectQueryMeta(ExecutableElement method, TypeElement dao) {
        super(method, dao);
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

    public void setSelectReflection(SelectReflection selectReflection) {
        this.selectReflection = selectReflection;
    }

    public SelectReflection getSelectReflection() {
        return selectReflection;
    }

    public int getFetchSize() {
        return selectReflection.getFetchSizeValue();
    }

    public int getMaxRows() {
        return selectReflection.getMaxRowsValue();
    }

    public int getQueryTimeout() {
        return selectReflection.getQueryTimeoutValue();
    }

    public SelectType getSelectStrategyType() {
        return selectReflection.getStrategyValue();
    }

    public FetchType getFetchType() {
        return selectReflection.getFetchValue();
    }

    public boolean getEnsureResult() {
        return selectReflection.getEnsureResultValue();
    }

    public boolean getEnsureResultMapping() {
        return selectReflection.getEnsureResultMappingValue();
    }

    public MapKeyNamingType getMapKeyNamingType() {
        return selectReflection.getMapKeyNamingValue();
    }

    public SqlLogType getSqlLogType() {
        return selectReflection.getSqlLogValue();
    }

    public boolean isExpandable() {
        return entityCtType != null;
    }

    public boolean isResultStream() {
        return resultStream;
    }

    public void setResultStream(boolean resultStream) {
        this.resultStream = resultStream;
    }

    @Override
    public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
        return visitor.visitSqlFileSelectQueryMeta(this, p);
    }

}

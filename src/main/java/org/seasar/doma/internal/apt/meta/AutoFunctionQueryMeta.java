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

import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.internal.apt.reflection.FunctionReflection;
import org.seasar.doma.jdbc.SqlLogType;

/**
 * @author taedium
 * 
 */
public class AutoFunctionQueryMeta extends AutoModuleQueryMeta {

    protected ResultParameterMeta resultParameterMeta;

    protected FunctionReflection functionReflection;

    public AutoFunctionQueryMeta(ExecutableElement method, TypeElement dao) {
        super(method, dao);
    }

    public ResultParameterMeta getResultParameterMeta() {
        return resultParameterMeta;
    }

    public void setResultParameterMeta(ResultParameterMeta resultParameterMeta) {
        this.resultParameterMeta = resultParameterMeta;
    }

    FunctionReflection getFunctionReflection() {
        return functionReflection;
    }

    void setFunctionReflection(FunctionReflection functionReflection) {
        this.functionReflection = functionReflection;
    }

    public String getCatalogName() {
        return functionReflection.getCatalogValue();
    }

    public String getSchemaName() {
        return functionReflection.getSchemaValue();
    }

    public String getFunctionName() {
        return functionReflection.getNameValue();
    }

    public boolean isQuoteRequired() {
        return functionReflection.getQuoteValue();
    }

    public int getQueryTimeout() {
        return functionReflection.getQueryTimeoutValue();
    }

    public boolean getEnsureResultMapping() {
        return functionReflection.getEnsureResultMappingValue();
    }

    @Override
    public MapKeyNamingType getMapKeyNamingType() {
        return functionReflection.getMapKeyNamingValue();
    }

    public SqlLogType getSqlLogType() {
        return functionReflection.getSqlLogValue();
    }

    @Override
    public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
        return visitor.visitAutoFunctionQueryMeta(this, p);
    }

}

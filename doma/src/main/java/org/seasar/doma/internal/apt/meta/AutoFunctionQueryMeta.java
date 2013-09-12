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
import org.seasar.doma.internal.apt.mirror.FunctionMirror;

/**
 * @author taedium
 * 
 */
public class AutoFunctionQueryMeta extends AutoModuleQueryMeta {

    protected ResultParameterMeta resultParameterMeta;

    protected FunctionMirror functionMirror;

    public AutoFunctionQueryMeta(ExecutableElement method) {
        super(method);
    }

    public ResultParameterMeta getResultParameterMeta() {
        return resultParameterMeta;
    }

    public void setResultParameterMeta(ResultParameterMeta resultParameterMeta) {
        this.resultParameterMeta = resultParameterMeta;
    }

    FunctionMirror getFunctionMirror() {
        return functionMirror;
    }

    void setFunctionMirror(FunctionMirror functionMirror) {
        this.functionMirror = functionMirror;
    }

    public String getFunctionName() {
        return functionMirror.getQualifiedName();
    }

    public int getQueryTimeout() {
        return functionMirror.getQueryTimeoutValue();
    }

    public boolean getEnsureResultMapping() {
        return functionMirror.getEnsureResultMappingValue();
    }

    @Override
    public MapKeyNamingType getMapKeyNamingType() {
        return functionMirror.getMapKeyNamingValue();
    }

    @Override
    public <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p) {
        return visitor.visitAutoFunctionQueryMeta(this, p);
    }

}

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
package org.seasar.doma.internal.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import org.seasar.doma.internal.jdbc.sql.CallableSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.ResultParameter;

/**
 * @author taedium
 * 
 */
public class AutoFunctionQuery<R> extends AutoModuleQuery implements
        FunctionQuery<R> {

    protected String functionName;

    protected ResultParameter<R> resultParameter;

    public void prepare() {
        assertNotNull(config, functionName, resultParameter, callerClassName, callerMethodName);
        prepareOptions();
        prepareSql();
        assertNotNull(sql);
    }

    protected void prepareSql() {
        CallableSqlBuilder builder = new CallableSqlBuilder(config,
                functionName, parameters, resultParameter);
        sql = builder.build();
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public void setResultParameter(ResultParameter<R> parameter) {
        this.resultParameter = parameter;
    }

    @Override
    public R getResult() {
        return resultParameter.getResult();
    }

}

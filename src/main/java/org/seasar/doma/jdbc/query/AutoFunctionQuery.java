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
package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.internal.jdbc.sql.CallableSqlBuilder;
import org.seasar.doma.jdbc.ResultParameter;
import org.seasar.doma.jdbc.SqlKind;

public class AutoFunctionQuery<RESULT> extends AutoModuleQuery implements FunctionQuery<RESULT> {

    protected ResultParameter<RESULT> resultParameter;

    @Override
    public void prepare() {
        super.prepare();
        assertNotNull(moduleName, resultParameter);
        prepareQualifiedName();
        prepareOptions();
        prepareSql();
        assertNotNull(sql);
    }

    protected void prepareSql() {
        CallableSqlBuilder builder = new CallableSqlBuilder(config, SqlKind.FUNCTION, qualifiedName,
                parameters, sqlLogType, resultParameter);
        sql = builder.build(this::comment);
    }

    public void setFunctionName(String functionName) {
        setModuleName(functionName);
    }

    public void setResultParameter(ResultParameter<RESULT> parameter) {
        this.resultParameter = parameter;
    }

    @Override
    public RESULT getResult() {
        return resultParameter.getResult();
    }

}

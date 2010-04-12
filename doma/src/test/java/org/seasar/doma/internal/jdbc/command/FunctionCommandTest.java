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
package org.seasar.doma.internal.jdbc.command;

import java.util.List;

import junit.framework.TestCase;

import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.query.AutoFunctionQuery;
import org.seasar.doma.internal.jdbc.sql.BasicInOutParameter;
import org.seasar.doma.internal.jdbc.sql.BasicInParameter;
import org.seasar.doma.internal.jdbc.sql.BasicOutParameter;
import org.seasar.doma.internal.jdbc.sql.BasicResultParameter;
import org.seasar.doma.jdbc.Reference;
import org.seasar.doma.wrapper.IntegerWrapper;

/**
 * @author taedium
 * 
 */
public class FunctionCommandTest extends TestCase {

    private final MockConfig runtimeConfig = new MockConfig();

    public void testExecute() throws Exception {
        List<Object> outParameters = runtimeConfig.dataSource.connection.callableStatement.outParameters;
        outParameters.add(10);
        outParameters.add(null);
        outParameters.add(20);
        outParameters.add(30);

        IntegerWrapper aaa = new IntegerWrapper(40);
        IntegerWrapper bbb = new IntegerWrapper(50);
        IntegerWrapper ccc = new IntegerWrapper(60);

        AutoFunctionQuery<Integer> query = new AutoFunctionQuery<Integer>();
        query.setConfig(runtimeConfig);
        query.setFunctionName("aaa");
        query.setResultParameter(new BasicResultParameter<Integer>(
                new IntegerWrapper(), false));
        query.addParameter(new BasicInParameter(aaa));
        query.addParameter(new BasicOutParameter<Integer>(bbb,
                new Reference<Integer>()));
        query.addParameter(new BasicInOutParameter<Integer>(ccc,
                new Reference<Integer>()));
        query.setCallerClassName("aaa");
        query.setCallerMethodName("bbb");
        query.prepare();
        Integer result = new FunctionCommand<Integer>(query).execute();
        query.complete();

        assertNotNull(result);
        assertEquals(new Integer(10), result);
        assertEquals(new Integer(40), aaa.get());
        assertEquals(new Integer(20), bbb.get());
        assertEquals(new Integer(30), ccc.get());

        String sql = runtimeConfig.dataSource.connection.callableStatement.sql;
        assertEquals("{? = call aaa(?, ?, ?)}", sql);
    }
}

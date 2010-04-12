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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.doma.internal.jdbc.mock.BindValue;
import org.seasar.doma.internal.jdbc.mock.MockCallableStatement;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.RegisterOutParameter;
import org.seasar.doma.internal.jdbc.query.Query;
import org.seasar.doma.internal.jdbc.sql.BasicInOutParameter;
import org.seasar.doma.internal.jdbc.sql.BasicInParameter;
import org.seasar.doma.internal.jdbc.sql.BasicOutParameter;
import org.seasar.doma.internal.jdbc.sql.BasicResultParameter;
import org.seasar.doma.internal.jdbc.sql.CallableSqlParameter;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Reference;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.wrapper.BigDecimalWrapper;
import org.seasar.doma.wrapper.IntegerWrapper;
import org.seasar.doma.wrapper.StringWrapper;

/**
 * @author taedium
 * 
 */
public class CallableSqlParameterBinderTest extends TestCase {

    private final MockConfig runtimeConfig = new MockConfig();

    public void testBind() throws Exception {
        MockCallableStatement callableStatement = new MockCallableStatement();

        List<CallableSqlParameter> parameters = new ArrayList<CallableSqlParameter>();
        parameters.add(new BasicResultParameter<Integer>(new IntegerWrapper(),
                false));
        parameters.add(new BasicInParameter(new StringWrapper("aaa")));
        parameters.add(new BasicInOutParameter<BigDecimal>(
                new BigDecimalWrapper(), new Reference<BigDecimal>(
                        new BigDecimal(10))));
        parameters.add(new BasicOutParameter<String>(new StringWrapper("bbb"),
                new Reference<String>()));
        CallableSqlParameterBinder binder = new CallableSqlParameterBinder(
                new MyQuery());
        binder.bind(callableStatement, parameters);

        List<BindValue> bindValues = callableStatement.bindValues;
        assertEquals(2, bindValues.size());
        BindValue bindValue = bindValues.get(0);
        assertEquals(2, bindValue.getIndex());
        assertEquals("aaa", bindValue.getValue());
        bindValue = bindValues.get(1);
        assertEquals(3, bindValue.getIndex());
        assertEquals(new BigDecimal(10), bindValue.getValue());
        List<RegisterOutParameter> registerOutParameters = callableStatement.registerOutParameters;
        assertEquals(3, registerOutParameters.size());
    }

    protected class MyQuery implements Query {

        @Override
        public Sql<?> getSql() {
            return null;
        }

        @Override
        public Config getConfig() {
            return runtimeConfig;
        }

        @Override
        public String getClassName() {
            return null;
        }

        @Override
        public String getMethodName() {
            return null;
        }

        @Override
        public int getQueryTimeout() {
            return 0;
        }

        @Override
        public void prepare() {
        }

        @Override
        public void complete() {
        }

    }
}

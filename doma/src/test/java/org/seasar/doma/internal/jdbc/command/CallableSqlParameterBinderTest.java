package org.seasar.doma.internal.jdbc.command;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.seasar.doma.domain.BigDecimalDomain;
import org.seasar.doma.domain.IntegerDomain;
import org.seasar.doma.domain.StringDomain;
import org.seasar.doma.internal.jdbc.command.CallableSqlParameterBinder;
import org.seasar.doma.internal.jdbc.mock.BindValue;
import org.seasar.doma.internal.jdbc.mock.MockCallableStatement;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.RegisterOutParameter;
import org.seasar.doma.internal.jdbc.query.Query;
import org.seasar.doma.internal.jdbc.sql.CallableSqlParameter;
import org.seasar.doma.internal.jdbc.sql.DomainResultParameter;
import org.seasar.doma.internal.jdbc.sql.InOutParameter;
import org.seasar.doma.internal.jdbc.sql.InParameter;
import org.seasar.doma.internal.jdbc.sql.OutParameter;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Sql;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class CallableSqlParameterBinderTest extends TestCase {

    private MockConfig runtimeConfig = new MockConfig();

    public void testBind() throws Exception {
        MockCallableStatement callableStatement = new MockCallableStatement();

        List<CallableSqlParameter> parameters = new ArrayList<CallableSqlParameter>();
        parameters.add(new DomainResultParameter<IntegerDomain>(
                IntegerDomain.class));
        parameters.add(new InParameter(new StringDomain("aaa")));
        parameters.add(new InOutParameter(new BigDecimalDomain(new BigDecimal(
                10))));
        parameters.add(new OutParameter(new StringDomain("bbb")));
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

    }
}

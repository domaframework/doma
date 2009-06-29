package org.seasar.doma.internal.jdbc.command;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.seasar.doma.domain.BigDecimalDomain;
import org.seasar.doma.domain.StringDomain;
import org.seasar.doma.internal.jdbc.command.PreparedSqlParameterBinder;
import org.seasar.doma.internal.jdbc.mock.BindValue;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockPreparedStatement;
import org.seasar.doma.internal.jdbc.query.Query;
import org.seasar.doma.internal.jdbc.sql.BindParameter;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Sql;

import junit.framework.TestCase;

public class PreparedSqlParameterBinderTest extends TestCase {

    private MockConfig runtimeConfig = new MockConfig();

    public void testBind() throws Exception {
        MockPreparedStatement preparedStatement = new MockPreparedStatement();
        List<BindParameter> parameters = new ArrayList<BindParameter>();
        parameters.add(new BindParameter(new StringDomain("aaa")));
        parameters.add(new BindParameter(new BigDecimalDomain(
                new BigDecimal(10))));
        PreparedSqlParameterBinder binder = new PreparedSqlParameterBinder(
                new MyQuery());
        binder.bind(preparedStatement, parameters);

        List<BindValue> bindValues = preparedStatement.bindValues;
        assertEquals(2, bindValues.size());
        BindValue bindValue = bindValues.get(0);
        assertEquals(1, bindValue.getIndex());
        assertEquals("aaa", bindValue.getValue());
        bindValue = bindValues.get(1);
        assertEquals(2, bindValue.getIndex());
        assertEquals(new BigDecimal(10), bindValue.getValue());
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

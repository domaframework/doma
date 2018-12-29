package org.seasar.doma.internal.jdbc.command;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.BindValue;
import org.seasar.doma.internal.jdbc.mock.MockCallableStatement;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.RegisterOutParameter;
import org.seasar.doma.internal.jdbc.sql.BasicInOutParameter;
import org.seasar.doma.internal.jdbc.sql.BasicInParameter;
import org.seasar.doma.internal.jdbc.sql.BasicOutParameter;
import org.seasar.doma.internal.jdbc.sql.BasicSingleResultParameter;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Reference;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlParameter;
import org.seasar.doma.jdbc.query.Query;

/** @author taedium */
public class CallableSqlParameterBinderTest extends TestCase {

  private final MockConfig runtimeConfig = new MockConfig();

  public void testBind() throws Exception {
    MockCallableStatement callableStatement = new MockCallableStatement();

    List<SqlParameter> parameters = new ArrayList<>();
    parameters.add(
        new BasicSingleResultParameter<Integer>(
            () -> new org.seasar.doma.wrapper.IntegerWrapper(), false));
    parameters.add(
        new BasicInParameter<String>(() -> new org.seasar.doma.wrapper.StringWrapper("aaa")));
    parameters.add(
        new BasicInOutParameter<BigDecimal>(
            () -> new org.seasar.doma.wrapper.BigDecimalWrapper(),
            new Reference<BigDecimal>(new BigDecimal(10))));
    parameters.add(
        new BasicOutParameter<String>(
            () -> new org.seasar.doma.wrapper.StringWrapper("bbb"), new Reference<String>()));
    CallableSqlParameterBinder binder = new CallableSqlParameterBinder(new MyQuery());
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
    public void prepare() {}

    @Override
    public void complete() {}

    @Override
    public Method getMethod() {
      return null;
    }

    @Override
    public String comment(String sql) {
      return sql;
    }
  }
}

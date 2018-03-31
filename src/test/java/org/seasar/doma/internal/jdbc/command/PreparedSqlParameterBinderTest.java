package org.seasar.doma.internal.jdbc.command;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.mock.MockPreparedStatement;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.internal.jdbc.sql.ScalarInParameter;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.query.Query;

public class PreparedSqlParameterBinderTest extends TestCase {

  private final MockConfig runtimeConfig = new MockConfig();

  public void testBind() throws Exception {
    var preparedStatement = new MockPreparedStatement();
    List<ScalarInParameter<?, ?>> parameters = new ArrayList<ScalarInParameter<?, ?>>();
    parameters.add(
        new ScalarInParameter<>(
            () -> new BasicScalar<>(new org.seasar.doma.wrapper.StringWrapper(), false), "aaa"));
    parameters.add(
        new ScalarInParameter<>(
            () -> new BasicScalar<>(new org.seasar.doma.wrapper.BigDecimalWrapper(), false),
            new BigDecimal(10)));
    var binder = new PreparedSqlParameterBinder(new MyQuery());
    binder.bind(preparedStatement, parameters);

    var bindValues = preparedStatement.bindValues;
    assertEquals(2, bindValues.size());
    var bindValue = bindValues.get(0);
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

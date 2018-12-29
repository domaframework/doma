package org.seasar.doma.jdbc.query;

import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.sql.BasicSingleResultParameter;
import org.seasar.doma.jdbc.SqlLogType;

/** @author taedium */
public class AutoFunctionQueryTest extends TestCase {

  private final MockConfig runtimeConfig = new MockConfig();

  public void testPrepare() throws Exception {
    AutoFunctionQuery<Integer> query = new AutoFunctionQuery<Integer>();
    query.setConfig(runtimeConfig);
    query.setFunctionName("aaa");
    query.setResultParameter(
        new BasicSingleResultParameter<Integer>(
            () -> new org.seasar.doma.wrapper.IntegerWrapper(), false));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    FunctionQuery<Integer> functionQuery = query;
    assertNotNull(functionQuery.getSql());
  }
}

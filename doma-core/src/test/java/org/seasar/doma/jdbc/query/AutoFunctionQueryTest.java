package org.seasar.doma.jdbc.query;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.sql.BasicSingleResultParameter;
import org.seasar.doma.jdbc.SqlLogType;

public class AutoFunctionQueryTest {

  private final MockConfig runtimeConfig = new MockConfig();

  @Test
  public void testPrepare() throws Exception {
    AutoFunctionQuery<Integer> query = new AutoFunctionQuery<>();
    query.setConfig(runtimeConfig);
    query.setFunctionName("aaa");
    query.setResultParameter(
        new BasicSingleResultParameter<>(() -> new org.seasar.doma.wrapper.IntegerWrapper()));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    FunctionQuery<Integer> functionQuery = query;
    assertNotNull(functionQuery.getSql());
  }
}

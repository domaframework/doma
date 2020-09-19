package org.seasar.doma.jdbc.query;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.sql.BasicSingleResultParameter;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.wrapper.IntegerWrapper;

public class AutoFunctionQueryTest {

  private final MockConfig runtimeConfig = new MockConfig();

  @Test
  public void testPrepare() {
    AutoFunctionQuery<Integer> query = new AutoFunctionQuery<>();
    query.setConfig(runtimeConfig);
    query.setFunctionName("aaa");
    query.setResultParameter(new BasicSingleResultParameter<>(IntegerWrapper::new));
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setSqlLogType(SqlLogType.FORMATTED);
    query.prepare();

    assertNotNull(((FunctionQuery<Integer>) query).getSql());
  }
}

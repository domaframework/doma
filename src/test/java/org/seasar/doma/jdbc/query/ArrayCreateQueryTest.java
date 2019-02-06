package org.seasar.doma.jdbc.query;

import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;

public class ArrayCreateQueryTest {

  private final MockConfig config = new MockConfig();

  @Test
  public void testPrepare() throws Exception {
    ArrayCreateQuery query = new ArrayCreateQuery();
    query.setConfig(config);
    query.setCallerClassName("aaa");
    query.setCallerMethodName("bbb");
    query.setTypeName("varchar");
    query.setElements(new String[] {});
    query.prepare();
  }
}

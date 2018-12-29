package org.seasar.doma.jdbc.query;

import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;

public class ArrayCreateQueryTest extends TestCase {

  private final MockConfig config = new MockConfig();

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

package org.seasar.doma.jdbc;

import junit.framework.TestCase;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.internal.jdbc.mock.MockConfig;

/** @author nakamura-to */
public class ConfigTest extends TestCase {

  public void testGet() throws Exception {
    Config config = Config.get(new Provider());
    assertNotNull(config);
  }

  public void testGet_IllegalArgument() throws Exception {
    try {
      Config.get("hoge");
      fail();
    } catch (DomaIllegalArgumentException e) {
      System.out.println(e);
    }
  }

  protected static class Provider implements ConfigProvider {

    @Override
    public Config getConfig() {
      return new MockConfig();
    }
  }
}

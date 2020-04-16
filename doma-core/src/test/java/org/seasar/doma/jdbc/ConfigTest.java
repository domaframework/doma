package org.seasar.doma.jdbc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.internal.jdbc.mock.MockConfig;

public class ConfigTest {

  @Test
  public void testGet() throws Exception {
    Config config = Config.get(new Provider());
    assertNotNull(config);
  }

  @Test
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

package org.seasar.doma.internal;

import javax.sql.DataSource;
import junit.framework.TestCase;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.ConfigException;
import org.seasar.doma.jdbc.EntityListenerProvider;
import org.seasar.doma.jdbc.SimpleDataSource;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.StandardDialect;
import org.seasar.doma.jdbc.entity.EntityListener;

/** @author backpaper0 */
public class RuntimeConfigTest extends TestCase {

  public void testGetEntityListener() throws Exception {
    Config originalConfig =
        new MockConfig() {

          @Override
          public EntityListenerProvider getEntityListenerProvider() {
            return new EntityListenerProvider() {};
          }
        };

    RuntimeConfig runtimeConfig = new RuntimeConfig(originalConfig);

    MockEntityListener entityListener =
        runtimeConfig
            .getEntityListenerProvider()
            .get(MockEntityListener.class, MockEntityListener::new);
    assertNotNull(entityListener);
  }

  public void testGetEntityListenerNullCheck() throws Exception {
    Config originalConfig =
        new MockConfig() {

          @Override
          public EntityListenerProvider getEntityListenerProvider() {
            return null;
          }
        };

    RuntimeConfig runtimeConfig = new RuntimeConfig(originalConfig);

    try {
      runtimeConfig
          .getEntityListenerProvider()
          .get(MockEntityListener.class, MockEntityListener::new);
      fail();
    } catch (ConfigException e) {
      assertEquals(originalConfig.getClass().getName(), e.getClassName());
      assertEquals("getEntityListenerProvider", e.getMethodName());
    }
  }

  private interface MockConfig extends Config {

    @Override
    default Dialect getDialect() {
      return new StandardDialect();
    }

    @Override
    default DataSource getDataSource() {
      return new SimpleDataSource();
    }
  }

  private static class MockEntityListener implements EntityListener<Object> {}
}

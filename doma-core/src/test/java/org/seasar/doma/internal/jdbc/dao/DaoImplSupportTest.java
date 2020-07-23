package org.seasar.doma.internal.jdbc.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.Config;

/** @author backpaper0 */
public class DaoImplSupportTest {

  @Test
  public void testConstructorParameter1() throws Exception {
    Config config = null;
    try {
      new DaoImplSupport(config) {};
      fail();
    } catch (DomaNullPointerException expected) {
      assertEquals("config", expected.getParameterName());
    }
  }

  @Test
  public void testConstructorParameter2() throws Exception {
    Config config = null;
    Connection connection = mock(Connection.class);
    try {
      new DaoImplSupport(config, connection) {};
      fail();
    } catch (DomaNullPointerException expected) {
      assertEquals("config", expected.getParameterName());
    }
  }

  @Test
  public void testConstructorParameter3() throws Exception {
    Config config = mock(Config.class);
    Connection connection = null;
    try {
      new DaoImplSupport(config, connection) {};
      fail();
    } catch (DomaNullPointerException expected) {
      assertEquals("connection", expected.getParameterName());
    }
  }

  @Test
  public void testConstructorParameter4() throws Exception {
    Config config = null;
    DataSource dataSource = mock(DataSource.class);
    try {
      new DaoImplSupport(config, dataSource) {};
      fail();
    } catch (DomaNullPointerException expected) {
      assertEquals("config", expected.getParameterName());
    }
  }

  @Test
  public void testConstructorParameter5() throws Exception {
    Config config = mock(Config.class);
    DataSource dataSource = null;
    try {
      new DaoImplSupport(config, dataSource) {};
      fail();
    } catch (DomaNullPointerException expected) {
      assertEquals("dataSource", expected.getParameterName());
    }
  }

  private <T> T mock(Class<T> aClass) {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    Class<?>[] interfaces = {aClass};
    InvocationHandler h =
        new InvocationHandler() {

          @Override
          public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return null;
          }
        };
    Object proxy = Proxy.newProxyInstance(loader, interfaces, h);
    return aClass.cast(proxy);
  }
}

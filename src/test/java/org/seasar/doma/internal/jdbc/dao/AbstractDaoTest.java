package org.seasar.doma.internal.jdbc.dao;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import javax.sql.DataSource;
import junit.framework.TestCase;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.Config;

/** @author backpaper0 */
public class AbstractDaoTest extends TestCase {

  public void testConstructorParameter1() throws Exception {
    Config config = null;
    try {
      new AbstractDao(config) {};
      fail();
    } catch (DomaNullPointerException expected) {
      assertEquals("config", expected.getParameterName());
    }
  }

  public void testConstructorParameter2() throws Exception {
    Config config = null;
    var connection = mock(Connection.class);
    try {
      new AbstractDao(config, connection) {};
      fail();
    } catch (DomaNullPointerException expected) {
      assertEquals("config", expected.getParameterName());
    }
  }

  public void testConstructorParameter3() throws Exception {
    var config = mock(Config.class);
    Connection connection = null;
    try {
      new AbstractDao(config, connection) {};
      fail();
    } catch (DomaNullPointerException expected) {
      assertEquals("connection", expected.getParameterName());
    }
  }

  public void testConstructorParameter4() throws Exception {
    Config config = null;
    var dataSource = mock(DataSource.class);
    try {
      new AbstractDao(config, dataSource) {};
      fail();
    } catch (DomaNullPointerException expected) {
      assertEquals("config", expected.getParameterName());
    }
  }

  public void testConstructorParameter5() throws Exception {
    var config = mock(Config.class);
    DataSource dataSource = null;
    try {
      new AbstractDao(config, dataSource) {};
      fail();
    } catch (DomaNullPointerException expected) {
      assertEquals("dataSource", expected.getParameterName());
    }
  }

  private <T> T mock(Class<T> aClass) {
    var loader = Thread.currentThread().getContextClassLoader();
    Class<?>[] interfaces = {aClass};
    InvocationHandler h =
        new InvocationHandler() {

          @Override
          public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return null;
          }
        };
    var proxy = Proxy.newProxyInstance(loader, interfaces, h);
    return aClass.cast(proxy);
  }
}

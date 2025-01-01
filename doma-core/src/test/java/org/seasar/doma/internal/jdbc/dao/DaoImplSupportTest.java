package org.seasar.doma.internal.jdbc.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.Config;

/**
 * @author backpaper0
 */
public class DaoImplSupportTest {

  @SuppressWarnings("ConstantConditions")
  @Test
  public void testConstructorParameter1() {
    Config config = null;
    try {
      new DaoImplSupport(config) {};
      fail();
    } catch (DomaNullPointerException expected) {
      assertEquals("config", expected.getParameterName());
    }
  }
}

package org.seasar.doma.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaNullPointerException;

/** @author backpaper0 */
public class SelectOptionsTest {

  @SuppressWarnings("ConstantConditions")
  @Test
  public void testForUpdateParameter() {
    String[] aliases = null;
    try {
      SelectOptions.get().forUpdate(aliases);
      fail();
    } catch (DomaNullPointerException expected) {
      assertEquals("aliases", expected.getParameterName());
    }
  }

  @SuppressWarnings("ConstantConditions")
  @Test
  public void testForUpdateNowaitParameter() {
    String[] aliases = null;
    try {
      SelectOptions.get().forUpdateNowait(aliases);
      fail();
    } catch (DomaNullPointerException expected) {
      assertEquals("aliases", expected.getParameterName());
    }
  }

  @Test
  public void testForUpdateWaitParameter1() {
    int waitSeconds = -1;
    try {
      SelectOptions.get().forUpdateWait(waitSeconds);
      fail();
    } catch (DomaIllegalArgumentException expected) {
      assertEquals("waitSeconds", expected.getParameterName());
      assertEquals("waitSeconds < 0", expected.getDescription());
    }
  }

  @Test
  public void testForUpdateWaitParameter2() {
    int waitSeconds = -1;
    String[] aliases = {"a"};
    try {
      SelectOptions.get().forUpdateWait(waitSeconds, aliases);
      fail();
    } catch (DomaIllegalArgumentException expected) {
      assertEquals("waitSeconds", expected.getParameterName());
      assertEquals("waitSeconds < 0", expected.getDescription());
    }
  }

  @SuppressWarnings("ConstantConditions")
  @Test
  public void testForUpdateWaitParameter3() {
    int waitSeconds = 1;
    String[] aliases = null;
    try {
      SelectOptions.get().forUpdateWait(waitSeconds, aliases);
      fail();
    } catch (DomaNullPointerException expected) {
      assertEquals("aliases", expected.getParameterName());
    }
  }

  @Test
  public void testOffsetParameter() {
    int offset = -1;
    try {
      SelectOptions.get().offset(offset);
      fail();
    } catch (DomaIllegalArgumentException expected) {
      assertEquals("offset", expected.getParameterName());
      assertEquals("offset < 0", expected.getDescription());
    }
  }

  @Test
  public void testLimitParameter() {
    int limit = -1;
    try {
      SelectOptions.get().limit(limit);
      fail();
    } catch (DomaIllegalArgumentException expected) {
      assertEquals("limit", expected.getParameterName());
      assertEquals("limit < 0", expected.getDescription());
    }
  }
}

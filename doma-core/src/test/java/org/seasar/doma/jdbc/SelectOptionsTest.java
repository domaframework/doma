package org.seasar.doma.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaNullPointerException;

/** @author backpaper0 */
public class SelectOptionsTest {

  @Test
  public void testForUpdateParameter() throws Exception {
    String[] aliases = null;
    try {
      SelectOptions.get().forUpdate(aliases);
      fail();
    } catch (DomaNullPointerException expected) {
      assertEquals("aliases", expected.getParameterName());
    }
  }

  @Test
  public void testForUpdateNowaitParameter() throws Exception {
    String[] aliases = null;
    try {
      SelectOptions.get().forUpdateNowait(aliases);
      fail();
    } catch (DomaNullPointerException expected) {
      assertEquals("aliases", expected.getParameterName());
    }
  }

  @Test
  public void testForUpdateWaitParameter1() throws Exception {
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
  public void testForUpdateWaitParameter2() throws Exception {
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

  @Test
  public void testForUpdateWaitParameter3() throws Exception {
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
  public void testOffsetParameter() throws Exception {
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
  public void testLimitParameter() throws Exception {
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

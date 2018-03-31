package org.seasar.doma.internal.util;

import junit.framework.TestCase;

public class ResourceUtilTest extends TestCase {

  public void testGetResourceAsStream() throws Exception {
    var path = getClass().getName().replace(".", "/") + ".txt";
    var inputStream = ResourceUtil.getResourceAsStream(path);
    assertNotNull(inputStream);
  }

  public void testGetResourceAsStream_nonexistentPath() throws Exception {
    var inputStream = ResourceUtil.getResourceAsStream("nonexistentPath");
    assertNull(inputStream);
  }

  public void testGetResourceAsString() throws Exception {
    var path = getClass().getName().replace(".", "/") + ".txt";
    var value = ResourceUtil.getResourceAsString(path);
    assertEquals("aaa", value);
  }
}

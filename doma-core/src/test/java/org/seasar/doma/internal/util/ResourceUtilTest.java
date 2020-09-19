package org.seasar.doma.internal.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.InputStream;
import org.junit.jupiter.api.Test;

public class ResourceUtilTest {

  @Test
  public void testGetResourceAsStream() {
    String path = getClass().getName().replace(".", "/") + ".txt";
    InputStream inputStream = ResourceUtil.getResourceAsStream(path);
    assertNotNull(inputStream);
  }

  @Test
  public void testGetResourceAsStream_nonexistentPath() {
    InputStream inputStream = ResourceUtil.getResourceAsStream("nonexistentPath");
    assertNull(inputStream);
  }

  @Test
  public void testGetResourceAsString() throws Exception {
    String path = getClass().getName().replace(".", "/") + ".txt";
    String value = ResourceUtil.getResourceAsString(path);
    assertEquals("aaa", value);
  }
}

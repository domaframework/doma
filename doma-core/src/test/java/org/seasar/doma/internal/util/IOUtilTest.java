package org.seasar.doma.internal.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class IOUtilTest {

  @Test
  public void test() {
    IOUtil.close(
        () -> {
          throw new IOException();
        });
  }

  @Test
  public void testEndWith_true() {
    File file = new File("/fuga/META-INF/piyo/HogeDao/selectById.sql");
    String pathname = "META-INF/piyo/HogeDao/selectById.sql";
    assertTrue(IOUtil.endsWith(file, pathname));
  }

  @Test
  public void testEndWith_false() {
    File file = new File("/fuga/META-INF/piyo/hogeDao/selectById.sql");
    String pathname = "META-INF/piyo/HogeDao/selectById.sql";
    assertFalse(IOUtil.endsWith(file, pathname));
  }
}

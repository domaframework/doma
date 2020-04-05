package org.seasar.doma.internal.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class IOUtilTest {

  @Test
  public void test() throws Exception {
    IOUtil.close(
        new Closeable() {

          @Override
          public void close() throws IOException {
            throw new IOException();
          }
        });
  }

  @Test
  public void testEndWith_true() throws Exception {
    File file = new File("/fuga/META-INF/piyo/HogeDao/selectById.sql");
    String pathname = "META-INF/piyo/HogeDao/selectById.sql";
    assertTrue(IOUtil.endsWith(file, pathname));
  }

  @Test
  public void testEndWith_false() throws Exception {
    File file = new File("/fuga/META-INF/piyo/hogeDao/selectById.sql");
    String pathname = "META-INF/piyo/HogeDao/selectById.sql";
    assertFalse(IOUtil.endsWith(file, pathname));
  }
}

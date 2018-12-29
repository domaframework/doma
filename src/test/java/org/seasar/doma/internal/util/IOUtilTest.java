package org.seasar.doma.internal.util;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import junit.framework.TestCase;

public class IOUtilTest extends TestCase {

  public void test() throws Exception {
    IOUtil.close(
        new Closeable() {

          @Override
          public void close() throws IOException {
            throw new IOException();
          }
        });
  }

  public void testEndWith_true() throws Exception {
    File file = new File("/fuga/META-INF/piyo/HogeDao/selectById.sql");
    String pathname = "META-INF/piyo/HogeDao/selectById.sql";
    assertTrue(IOUtil.endsWith(file, pathname));
  }

  public void testEndWith_false() throws Exception {
    File file = new File("/fuga/META-INF/piyo/hogeDao/selectById.sql");
    String pathname = "META-INF/piyo/HogeDao/selectById.sql";
    assertFalse(IOUtil.endsWith(file, pathname));
  }
}

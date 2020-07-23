package org.seasar.doma.internal.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.util.SqlFileUtil;

public class SqlFileUtilTest {

  @Test
  public void testBuildPath() throws Exception {
    String path = SqlFileUtil.buildPath("aaa.bbb.Ccc", "ddd");
    assertEquals("META-INF/aaa/bbb/Ccc/ddd.sql", path);
  }

  @Test
  public void testBuildPath_defaultPackage() throws Exception {
    String path = SqlFileUtil.buildPath("Ccc", "ddd");
    assertEquals("META-INF/Ccc/ddd.sql", path);
  }
}

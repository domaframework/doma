package org.seasar.doma.internal.util;

import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.util.SqlFileUtil;

/** @author taedium */
public class SqlFileUtilTest extends TestCase {

  public void testBuildPath() throws Exception {
    String path = SqlFileUtil.buildPath("aaa.bbb.Ccc", "ddd");
    assertEquals("META-INF/aaa/bbb/Ccc/ddd.sql", path);
  }

  public void testBuildPath_defaultPackage() throws Exception {
    String path = SqlFileUtil.buildPath("Ccc", "ddd");
    assertEquals("META-INF/Ccc/ddd.sql", path);
  }
}

package org.seasar.doma.internal.jdbc.util;

import junit.framework.TestCase;

public class DatabaseObjectUtilTest extends TestCase {

  public void testGetQualifiedName() throws Exception {
    var name = DatabaseObjectUtil.getQualifiedName(s -> "[" + s + "]", "aaa", "bbb", "ccc");
    assertEquals("[aaa].[bbb].[ccc]", name);
  }

  public void testGetQualifiedName_catalogIsNull() throws Exception {
    var name = DatabaseObjectUtil.getQualifiedName(s -> "[" + s + "]", null, "bbb", "ccc");
    assertEquals("[bbb].[ccc]", name);
  }

  public void testGetQualifiedName_schemaIsNull() throws Exception {
    var name = DatabaseObjectUtil.getQualifiedName(s -> "[" + s + "]", "aaa", null, "ccc");
    assertEquals("[aaa].[ccc]", name);
  }

  public void testGetQualifiedName_catalogAndSchemaIsNull() throws Exception {
    var name = DatabaseObjectUtil.getQualifiedName(s -> "[" + s + "]", null, null, "ccc");
    assertEquals("[ccc]", name);
  }
}

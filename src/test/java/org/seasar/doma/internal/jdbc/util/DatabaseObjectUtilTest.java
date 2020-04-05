package org.seasar.doma.internal.jdbc.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DatabaseObjectUtilTest {

  @Test
  public void testGetQualifiedName() throws Exception {
    String name = DatabaseObjectUtil.getQualifiedName(s -> "[" + s + "]", "aaa", "bbb", "ccc");
    assertEquals("[aaa].[bbb].[ccc]", name);
  }

  @Test
  public void testGetQualifiedName_catalogIsNull() throws Exception {
    String name = DatabaseObjectUtil.getQualifiedName(s -> "[" + s + "]", null, "bbb", "ccc");
    assertEquals("[bbb].[ccc]", name);
  }

  @Test
  public void testGetQualifiedName_schemaIsNull() throws Exception {
    String name = DatabaseObjectUtil.getQualifiedName(s -> "[" + s + "]", "aaa", null, "ccc");
    assertEquals("[aaa].[ccc]", name);
  }

  @Test
  public void testGetQualifiedName_catalogAndSchemaIsNull() throws Exception {
    String name = DatabaseObjectUtil.getQualifiedName(s -> "[" + s + "]", null, null, "ccc");
    assertEquals("[ccc]", name);
  }
}

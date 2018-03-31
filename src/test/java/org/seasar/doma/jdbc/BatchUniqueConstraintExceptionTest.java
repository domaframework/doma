package org.seasar.doma.jdbc;

import junit.framework.TestCase;

public class BatchUniqueConstraintExceptionTest extends TestCase {

  public void test() throws Exception {
    var e =
        new BatchUniqueConstraintException(
            SqlLogType.FORMATTED, SqlKind.UPDATE, "aaa", "bbb", new Exception());
    System.out.println(e.getMessage());
    assertSame(SqlKind.UPDATE, e.getKind());
    assertEquals("aaa", e.getRawSql());
    assertEquals("bbb", e.getSqlFilePath());
    assertNull(e.getFormattedSql());
  }
}

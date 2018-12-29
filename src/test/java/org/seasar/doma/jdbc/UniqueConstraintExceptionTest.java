package org.seasar.doma.jdbc;

import junit.framework.TestCase;

public class UniqueConstraintExceptionTest extends TestCase {

  public void test() throws Exception {
    Exception cause = new Exception();
    UniqueConstraintException e =
        new UniqueConstraintException(
            SqlLogType.FORMATTED, SqlKind.INSERT, "aaa", "bbb", "ccc", cause);
    System.out.println(e.getMessage());
    assertSame(SqlKind.INSERT, e.getKind());
    assertEquals("aaa", e.getRawSql());
    assertEquals("bbb", e.getFormattedSql());
    assertEquals("ccc", e.getSqlFilePath());
    assertEquals(cause, e.getCause());
  }
}

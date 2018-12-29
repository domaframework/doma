package org.seasar.doma.jdbc;

import junit.framework.TestCase;

public class SqlExecutionExceptionTest extends TestCase {

  public void test() throws Exception {
    SqlExecutionException e =
        new SqlExecutionException(
            SqlLogType.FORMATTED,
            SqlKind.UPDATE,
            "aaa",
            "bbb",
            "ccc",
            new Exception(),
            new RuntimeException());
    System.out.println(e.getMessage());
    assertSame(SqlKind.UPDATE, e.getKind());
    assertEquals("aaa", e.getRawSql());
    assertEquals("bbb", e.getFormattedSql());
    assertEquals("ccc", e.getSqlFilePath());
    assertNotNull(e.getRootCause());
  }
}

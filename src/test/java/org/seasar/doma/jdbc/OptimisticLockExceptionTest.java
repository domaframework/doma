package org.seasar.doma.jdbc;

import junit.framework.TestCase;

public class OptimisticLockExceptionTest extends TestCase {

  public void test() throws Exception {
    OptimisticLockException e =
        new OptimisticLockException(SqlLogType.FORMATTED, SqlKind.UPDATE, "aaa", "bbb", "ccc");
    System.out.println(e.getMessage());
    assertSame(SqlKind.UPDATE, e.getKind());
    assertEquals("aaa", e.getRawSql());
    assertEquals("bbb", e.getFormattedSql());
    assertEquals("ccc", e.getSqlFilePath());
  }
}

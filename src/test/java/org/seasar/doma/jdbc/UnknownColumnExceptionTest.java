package org.seasar.doma.jdbc;

import junit.framework.TestCase;

/** @author taedium */
public class UnknownColumnExceptionTest extends TestCase {

  public void test() throws Exception {
    UnknownColumnException e =
        new UnknownColumnException(
            SqlLogType.FORMATTED, "aaa", "bbb", "ccc", SqlKind.SELECT, "ddd", "eee", "fff");
    System.out.println(e.getMessage());
    assertEquals("aaa", e.getColumnName());
    assertEquals("bbb", e.getExpectedPropertyName());
    assertEquals("ccc", e.getEntityClassName());
    assertSame(SqlKind.SELECT, e.getKind());
    assertEquals("ddd", e.getRawSql());
    assertEquals("eee", e.getFormattedSql());
    assertEquals("fff", e.getSqlFilePath());
  }
}

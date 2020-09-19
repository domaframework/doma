package org.seasar.doma.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

public class UnknownColumnExceptionTest {

  @Test
  public void test() {
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

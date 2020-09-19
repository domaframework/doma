package org.seasar.doma.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

public class NonSingleColumnExceptionTest {

  @Test
  public void test() {
    NonSingleColumnException e =
        new NonSingleColumnException(SqlLogType.FORMATTED, SqlKind.SELECT, "aaa", "bbb", "ccc");
    System.out.println(e.getMessage());
    assertSame(SqlKind.SELECT, e.getKind());
    assertEquals("aaa", e.getRawSql());
    assertEquals("bbb", e.getFormattedSql());
    assertEquals("ccc", e.getSqlFilePath());
  }
}

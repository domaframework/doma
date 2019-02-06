package org.seasar.doma.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

public class UniqueConstraintExceptionTest {

  @Test
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

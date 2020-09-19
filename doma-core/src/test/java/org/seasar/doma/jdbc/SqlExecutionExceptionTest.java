package org.seasar.doma.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

public class SqlExecutionExceptionTest {

  @Test
  public void test() {
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

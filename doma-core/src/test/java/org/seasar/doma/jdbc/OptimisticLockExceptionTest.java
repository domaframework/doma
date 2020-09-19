package org.seasar.doma.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

public class OptimisticLockExceptionTest {

  @Test
  public void test() {
    OptimisticLockException e =
        new OptimisticLockException(SqlLogType.FORMATTED, SqlKind.UPDATE, "aaa", "bbb", "ccc");
    System.out.println(e.getMessage());
    assertSame(SqlKind.UPDATE, e.getKind());
    assertEquals("aaa", e.getRawSql());
    assertEquals("bbb", e.getFormattedSql());
    assertEquals("ccc", e.getSqlFilePath());
  }
}

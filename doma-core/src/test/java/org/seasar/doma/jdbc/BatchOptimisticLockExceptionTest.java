package org.seasar.doma.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

public class BatchOptimisticLockExceptionTest {

  @Test
  public void test() {
    BatchOptimisticLockException e =
        new BatchOptimisticLockException(SqlLogType.FORMATTED, SqlKind.UPDATE, "aaa", "bbb");
    System.out.println(e.getMessage());
    assertSame(SqlKind.UPDATE, e.getKind());
    assertEquals("aaa", e.getRawSql());
    assertEquals("bbb", e.getSqlFilePath());
    assertNull(e.getFormattedSql());
  }
}

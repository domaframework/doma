package org.seasar.doma.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

public class BatchSqlExecutionExceptionTest {

  @Test
  public void test() throws Exception {
    BatchSqlExecutionException e =
        new BatchSqlExecutionException(
            SqlLogType.FORMATTED,
            SqlKind.UPDATE,
            "aaa",
            "bbb",
            new Exception(),
            new RuntimeException());
    assertSame(SqlKind.UPDATE, e.getKind());
    assertEquals("aaa", e.getRawSql());
    assertEquals("bbb", e.getSqlFilePath());
    assertNull(e.getFormattedSql());
    assertNotNull(e.getRootCause());
    System.out.println(e.getMessage());
  }
}

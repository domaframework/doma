package org.seasar.doma.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

public class BatchUniqueConstraintExceptionTest {

  @Test
  public void test() throws Exception {
    BatchUniqueConstraintException e =
        new BatchUniqueConstraintException(
            SqlLogType.FORMATTED, SqlKind.UPDATE, "aaa", "bbb", new Exception());
    System.out.println(e.getMessage());
    assertSame(SqlKind.UPDATE, e.getKind());
    assertEquals("aaa", e.getRawSql());
    assertEquals("bbb", e.getSqlFilePath());
    assertNull(e.getFormattedSql());
  }
}

package org.seasar.doma.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ResultMappingExceptionTest {

  @Test
  public void test() throws Exception {
    ResultMappingException e =
        new ResultMappingException(
            SqlLogType.FORMATTED,
            "aaa",
            Arrays.asList("bbb", "bbb2"),
            Arrays.asList("ccc", "ccc2"),
            SqlKind.SELECT,
            "ddd",
            "eee",
            "fff");
    System.out.println(e.getMessage());
    assertEquals("aaa", e.getEntityClassName());
    List<String> unmappedPropertyNames = e.getUnmappedPropertyNames();
    assertEquals("bbb", unmappedPropertyNames.get(0));
    assertEquals("bbb2", unmappedPropertyNames.get(1));
    List<String> expectedColumnNames = e.getExpectedColumnNames();
    assertEquals("ccc", expectedColumnNames.get(0));
    assertEquals("ccc2", expectedColumnNames.get(1));
    assertSame(SqlKind.SELECT, e.getKind());
    assertEquals("ddd", e.getRawSql());
    assertEquals("eee", e.getFormattedSql());
    assertEquals("fff", e.getSqlFilePath());
  }
}

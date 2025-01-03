/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ResultMappingExceptionTest {

  @Test
  public void test() {
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

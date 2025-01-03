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

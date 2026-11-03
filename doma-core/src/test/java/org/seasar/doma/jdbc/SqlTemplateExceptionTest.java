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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.function.Function;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.message.Message;

public class SqlTemplateExceptionTest {

  private final MockConfig config = new MockConfig();

  @Test
  public void testParseError() {
    String sql = "select * from aaa\n/*%end*/";
    SqlParseException e = assertThrows(SqlParseException.class, () -> new SqlParser(sql).parse());
    assertEquals(Message.DOMA2104, e.getMessageResource());
    assertEquals(sql, e.getSql());
    assertEquals(2, e.getLineNumber());
    assertEquals(8, e.getColumnNumber());
    // The message arguments keep the same layout as before
    assertArrayEquals(new Object[] {sql, 2, 8}, (Object[]) e.getArgs());
  }

  @Test
  public void testParseError_tokenizer() {
    String sql = "select 1\nfrom T /* abc\nwhere A = 1";
    SqlParseException e = assertThrows(SqlParseException.class, () -> new SqlParser(sql).parse());
    assertEquals(Message.DOMA2102, e.getMessageResource());
    assertEquals(2, e.getLineNumber());
  }

  @Test
  public void testBuildError() {
    SqlParser parser = new SqlParser("select * from aaa where bbb = \n/*bbb*/'ccc'");
    SqlNode sqlNode = parser.parse();
    NodePreparedSqlBuilder builder =
        new NodePreparedSqlBuilder(config, SqlKind.SELECT, "dummyPath");
    SqlBuildException e =
        assertThrows(SqlBuildException.class, () -> builder.build(sqlNode, Function.identity()));
    assertEquals(Message.DOMA2111, e.getMessageResource());
    assertEquals(2, e.getLineNumber());
    assertEquals(7, e.getColumnNumber());
  }

  @Test
  public void testArgumentsFollowTheLocation() {
    Throwable cause = new RuntimeException();
    SqlBuildException e =
        new SqlBuildException(Message.DOMA2112, cause, "select 1", 1, 8, "name", String.class);
    assertArrayEquals(
        new Object[] {"select 1", 1, 8, "name", String.class}, (Object[]) e.getArgs());
    assertEquals(
        Message.DOMA2112.getMessage("select 1", 1, 8, "name", String.class), e.getMessage());
    assertSame(cause, e.getCause());
  }
}

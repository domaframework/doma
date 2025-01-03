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
package org.seasar.doma.internal.jdbc.dialect;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.Function;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlNode;

public class Db2PagingTransformerTest {

  @Test
  public void testOffsetLimit() {
    String expected =
        "select * from ( select temp_.*, row_number() over( order by temp_.id ) as doma_rownumber_ from ( select emp.id from emp ) as temp_ ) as temp2_ where doma_rownumber_ > 5 and doma_rownumber_ <= 15";
    Db2PagingTransformer transformer = new Db2PagingTransformer(5, 10);
    SqlParser parser = new SqlParser("select emp.id from emp order by emp.id");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  @Test
  public void testOffsetOnly() {
    String expected =
        "select * from ( select temp_.*, row_number() over( order by temp_.id ) as doma_rownumber_ from ( select emp.id from emp ) as temp_ ) as temp2_ where doma_rownumber_ > 5";
    Db2PagingTransformer transformer = new Db2PagingTransformer(5, -1);
    SqlParser parser = new SqlParser("select emp.id from emp order by emp.id");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  @Test
  public void testLimitOnly() {
    String expected = "select emp.id from emp order by emp.id fetch first 10 rows only";
    Db2PagingTransformer transformer = new Db2PagingTransformer(-1, 10);
    SqlParser parser = new SqlParser("select emp.id from emp order by emp.id");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }
}

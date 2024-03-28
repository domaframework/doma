package org.seasar.doma.internal.jdbc.dialect;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.Function;
import org.junit.jupiter.api.Test;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.dialect.MysqlDialect;

public class MysqlForUpdateTransformerTest {

  @Test
  public void testForUpdateNormal() {
    String expected = "select * from emp order by emp.id for update";
    MysqlForUpdateTransformer transformer =
        new MysqlForUpdateTransformer(SelectForUpdateType.NORMAL, 0);
    SqlParser parser = new SqlParser("select * from emp order by emp.id");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  @Test
  public void testForUpdateNowait_v8() {
    String expected = "select * from emp order by emp.id for update nowait";
    MysqlForUpdateTransformer transformer =
        new MysqlForUpdateTransformer(
            SelectForUpdateType.NOWAIT, 0, new String[] {}, MysqlDialect.MySqlVersion.V8);
    SqlParser parser = new SqlParser("select * from emp order by emp.id");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }

  @Test
  public void testForUpdateNowait_alias_v8() {
    String expected = "select * from emp order by emp.id for update of emp nowait";
    MysqlForUpdateTransformer transformer =
        new MysqlForUpdateTransformer(
            SelectForUpdateType.NOWAIT, 0, new String[] {"emp"}, MysqlDialect.MySqlVersion.V8);
    SqlParser parser = new SqlParser("select * from emp order by emp.id");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }
}

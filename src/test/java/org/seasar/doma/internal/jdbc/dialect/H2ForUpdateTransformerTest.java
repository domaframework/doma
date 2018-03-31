package org.seasar.doma.internal.jdbc.dialect;

import java.util.function.Function;
import junit.framework.TestCase;
import org.seasar.doma.internal.jdbc.mock.MockConfig;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlNode;

/** @author taedium */
public class H2ForUpdateTransformerTest extends TestCase {

  public void testForUpdateNormal() throws Exception {
    String expected = "select * from emp order by emp.id for update";
    H2ForUpdateTransformer transformer = new H2ForUpdateTransformer(SelectForUpdateType.NORMAL, 0);
    SqlParser parser = new SqlParser("select * from emp order by emp.id");
    SqlNode sqlNode = transformer.transform(parser.parse());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(new MockConfig(), SqlKind.SELECT, "dummyPath");
    PreparedSql sql = sqlBuilder.build(sqlNode, Function.identity());
    assertEquals(expected, sql.getRawSql());
  }
}

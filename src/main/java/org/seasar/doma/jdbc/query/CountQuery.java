package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.FetchType;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlNode;

/** @author taedium */
public class CountQuery extends AbstractSelectQuery {

  protected SqlNode sqlNode;

  @Override
  public boolean isResultEnsured() {
    return true;
  }

  @Override
  public boolean isResultMappingEnsured() {
    return false;
  }

  @Override
  public FetchType getFetchType() {
    return FetchType.LAZY;
  }

  @Override
  public void prepare() {
    super.prepare();
    assertNotNull(sqlNode);
  }

  @Override
  protected void prepareSql() {
    SqlNode transformedSqlNode = config.getDialect().transformSelectSqlNodeForGettingCount(sqlNode);
    buildSql(
        (evaluator, expander) -> {
          NodePreparedSqlBuilder sqlBuilder =
              new NodePreparedSqlBuilder(
                  config, SqlKind.SELECT, null, evaluator, sqlLogType, expander);
          return sqlBuilder.build(transformedSqlNode, this::comment);
        });
  }

  @Override
  public void complete() {
    // do nothing
  }

  public void setSqlNode(SqlNode sqlNode) {
    this.sqlNode = sqlNode;
  }
}

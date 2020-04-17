package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.jdbc.SelectOptionsAccessor;
import org.seasar.doma.jdbc.SqlFile;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlNode;

public class SqlFileSelectQuery extends AbstractSelectQuery {

  protected String sqlFilePath;

  protected SqlFile sqlFile;

  @Override
  public void prepare() {
    super.prepare();
    assertNotNull(sqlFilePath);
  }

  protected void prepareSql() {
    sqlFile = config.getSqlFileRepository().getSqlFile(method, sqlFilePath, config.getDialect());
    SqlNode transformedSqlNode =
        config.getDialect().transformSelectSqlNode(sqlFile.getSqlNode(), options);
    buildSql(
        (evaluator, expander) -> {
          NodePreparedSqlBuilder sqlBuilder =
              new NodePreparedSqlBuilder(
                  config, SqlKind.SELECT, sqlFilePath, evaluator, sqlLogType, expander);
          return sqlBuilder.build(transformedSqlNode, this::comment);
        });
  }

  @Override
  public void complete() {
    if (SelectOptionsAccessor.isCount(options)) {
      executeCount(sqlFile.getSqlNode());
    }
  }

  public void setSqlFilePath(String sqlFilePath) {
    this.sqlFilePath = sqlFilePath;
  }
}

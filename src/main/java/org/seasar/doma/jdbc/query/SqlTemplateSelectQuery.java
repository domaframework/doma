package org.seasar.doma.jdbc.query;

import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.jdbc.SelectOptionsAccessor;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlTemplate;

public class SqlTemplateSelectQuery extends AbstractSelectQuery {

  protected SqlTemplate sqlTemplate;

  @Override
  public void prepare() {
    super.prepare();
  }

  protected void prepareSql() {
    sqlTemplate = config.getSqlTemplateRepository().getSqlTemplate(method, config.getDialect());
    var transformedSqlNode =
        config.getDialect().transformSelectSqlNode(sqlTemplate.getSqlNode(), options);
    buildSql(
        (evaluator, expander) -> {
          var sqlBuilder =
              new NodePreparedSqlBuilder(
                  config, SqlKind.SELECT, sqlTemplate.getPath(), evaluator, sqlLogType, expander);
          return sqlBuilder.build(transformedSqlNode, this::comment);
        });
  }

  @Override
  public void complete() {
    if (SelectOptionsAccessor.isCount(options)) {
      executeCount(sqlTemplate.getSqlNode());
    }
  }
}

package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.HashMap;
import java.util.Map;
import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.expr.Value;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.SqlTemplate;

public class SqlProcessorQuery extends AbstractQuery {

  protected final Map<String, Value> parameters = new HashMap<>();

  protected SqlTemplate sqlFile;

  protected PreparedSql sql;

  public void addParameter(String name, Class<?> type, Object value) {
    assertNotNull(name, type);
    parameters.put(name, new Value(type, value));
  }

  @Override
  public void prepare() {
    super.prepare();
    prepareSql();
  }

  protected void prepareSql() {
    sqlFile = config.getSqlTemplateRepository().getSqlTemplate(method, config.getDialect());
    var evaluator =
        new ExpressionEvaluator(
            parameters, config.getDialect().getExpressionFunctions(), config.getClassHelper());
    var sqlBuilder =
        new NodePreparedSqlBuilder(
            config, SqlKind.SQL_PROCESSOR, sqlFile.getPath(), evaluator, SqlLogType.FORMATTED);
    sql = sqlBuilder.build(sqlFile.getSqlNode(), this::comment);
  }

  @Override
  public PreparedSql getSql() {
    return sql;
  }

  @Override
  public void complete() {}
}

package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.HashMap;
import java.util.Map;
import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.expr.Value;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlFile;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;

public class SqlProcessorQuery extends AbstractQuery {

  protected final Map<String, Value> parameters = new HashMap<String, Value>();

  protected String sqlFilePath;

  protected SqlFile sqlFile;

  protected PreparedSql sql;

  public void setSqlFilePath(String sqlFilePath) {
    this.sqlFilePath = sqlFilePath;
  }

  public void addParameter(String name, Class<?> type, Object value) {
    assertNotNull(name, type);
    parameters.put(name, new Value(type, value));
  }

  @Override
  public void prepare() {
    super.prepare();
    assertNotNull(sqlFilePath);
    prepareSql();
  }

  protected void prepareSql() {
    sqlFile = config.getSqlFileRepository().getSqlFile(method, sqlFilePath, config.getDialect());
    ExpressionEvaluator evaluator =
        new ExpressionEvaluator(
            parameters, config.getDialect().getExpressionFunctions(), config.getClassHelper());
    NodePreparedSqlBuilder sqlBuilder =
        new NodePreparedSqlBuilder(
            config, SqlKind.SQL_PROCESSOR, sqlFilePath, evaluator, SqlLogType.FORMATTED);
    sql = sqlBuilder.build(sqlFile.getSqlNode(), this::comment);
  }

  @Override
  public PreparedSql getSql() {
    return sql;
  }

  @Override
  public void complete() {}
}

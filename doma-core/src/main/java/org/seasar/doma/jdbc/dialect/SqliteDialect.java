package org.seasar.doma.jdbc.dialect;

import java.sql.SQLException;
import java.util.Collections;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.expr.ExpressionFunctions;
import org.seasar.doma.internal.jdbc.dialect.SqlitePagingTransformer;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogFormattingVisitor;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.SqlNode;

/** A dialect for SQLite. */
public class SqliteDialect extends StandardDialect {

  public SqliteDialect() {
    this(
        new SqliteJdbcMappingVisitor(),
        new SqliteSqlLogFormattingVisitor(),
        new SqliteExpressionFunctions());
  }

  public SqliteDialect(JdbcMappingVisitor jdbcMappingVisitor) {
    this(jdbcMappingVisitor, new SqliteSqlLogFormattingVisitor(), new SqliteExpressionFunctions());
  }

  public SqliteDialect(SqlLogFormattingVisitor sqlLogFormattingVisitor) {
    this(new SqliteJdbcMappingVisitor(), sqlLogFormattingVisitor, new SqliteExpressionFunctions());
  }

  public SqliteDialect(ExpressionFunctions expressionFunctions) {
    this(new SqliteJdbcMappingVisitor(), new SqliteSqlLogFormattingVisitor(), expressionFunctions);
  }

  public SqliteDialect(
      JdbcMappingVisitor jdbcMappingVisitor, SqlLogFormattingVisitor sqlLogFormattingVisitor) {
    this(jdbcMappingVisitor, sqlLogFormattingVisitor, new SqliteExpressionFunctions());
  }

  public SqliteDialect(
      JdbcMappingVisitor jdbcMappingVisitor,
      SqlLogFormattingVisitor sqlLogFormattingVisitor,
      ExpressionFunctions expressionFunctions) {
    super(jdbcMappingVisitor, sqlLogFormattingVisitor, expressionFunctions);
  }

  @Override
  public String getName() {
    return "sqlite";
  }

  @Override
  public boolean includesIdentityColumn() {
    return true;
  }

  @Override
  public PreparedSql getIdentitySelectSql(
      String catalogName,
      String schemaName,
      String tableName,
      String columnName,
      boolean isQuoteRequired,
      boolean isIdColumnQuoteRequired) {
    if (tableName == null) {
      throw new DomaNullPointerException("tableName");
    }
    if (columnName == null) {
      throw new DomaNullPointerException("columnName");
    }
    String rawSql = "select last_insert_rowid()";
    return new PreparedSql(
        SqlKind.SELECT, rawSql, rawSql, null, Collections.emptyList(), SqlLogType.FORMATTED);
  }

  @Override
  protected SqlNode toPagingSqlNode(SqlNode sqlNode, long offset, long limit) {
    SqlitePagingTransformer transformer = new SqlitePagingTransformer(offset, limit);
    return transformer.transform(sqlNode);
  }

  @Override
  protected SqlNode toForUpdateSqlNode(
      SqlNode sqlNode, SelectForUpdateType forUpdateType, int waitSeconds, String... aliases) {
    return sqlNode;
  }

  @Override
  public boolean supportsIdentity() {
    return true;
  }

  @Override
  public boolean isUniqueConstraintViolated(SQLException sqlException) {
    if (sqlException == null) {
      throw new DomaNullPointerException("sqlException");
    }
    SQLException cause = getCauseSQLException(sqlException);
    String message = cause.getMessage();
    return message != null
        && message.startsWith("[SQLITE_CONSTRAINT]")
        && message.contains(" unique)");
  }

  public static class SqliteJdbcMappingVisitor extends StandardJdbcMappingVisitor {}

  public static class SqliteSqlLogFormattingVisitor extends StandardSqlLogFormattingVisitor {}

  public static class SqliteExpressionFunctions extends StandardExpressionFunctions {

    public SqliteExpressionFunctions() {
      super();
    }

    public SqliteExpressionFunctions(char[] wildcards) {
      super(wildcards);
    }

    protected SqliteExpressionFunctions(char escapeChar, char[] wildcards) {
      super(escapeChar, wildcards);
    }
  }
}

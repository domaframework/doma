package org.seasar.doma.jdbc.dialect;

import java.util.Collections;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.expr.ExpressionFunctions;
import org.seasar.doma.internal.jdbc.dialect.MssqlForUpdateTransformer;
import org.seasar.doma.internal.jdbc.dialect.MssqlPagingTransformer;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.ScriptBlockContext;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogFormattingVisitor;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.SqlNode;

/** A dialect for Microsoft SQL Server. */
public class MssqlDialect extends Mssql2008Dialect {

  /** whether this dialect forces to use the OFFSET FETCH Clause for a paging */
  private final boolean pagingForceOffsetFetch;

  public MssqlDialect() {
    this(
        new MssqlJdbcMappingVisitor(),
        new MssqlSqlLogFormattingVisitor(),
        new MssqlExpressionFunctions(),
        false);
  }

  public MssqlDialect(JdbcMappingVisitor jdbcMappingVisitor) {
    this(
        jdbcMappingVisitor,
        new MssqlSqlLogFormattingVisitor(),
        new MssqlExpressionFunctions(),
        false);
  }

  public MssqlDialect(SqlLogFormattingVisitor sqlLogFormattingVisitor) {
    this(
        new MssqlJdbcMappingVisitor(),
        sqlLogFormattingVisitor,
        new MssqlExpressionFunctions(),
        false);
  }

  public MssqlDialect(ExpressionFunctions expressionFunctions) {
    this(
        new MssqlJdbcMappingVisitor(),
        new MssqlSqlLogFormattingVisitor(),
        expressionFunctions,
        false);
  }

  public MssqlDialect(
      JdbcMappingVisitor jdbcMappingVisitor, SqlLogFormattingVisitor sqlLogFormattingVisitor) {
    this(jdbcMappingVisitor, sqlLogFormattingVisitor, new MssqlExpressionFunctions(), false);
  }

  public MssqlDialect(
      JdbcMappingVisitor jdbcMappingVisitor,
      SqlLogFormattingVisitor sqlLogFormattingVisitor,
      ExpressionFunctions expressionFunctions) {
    this(jdbcMappingVisitor, sqlLogFormattingVisitor, expressionFunctions, false);
  }

  public MssqlDialect(
      JdbcMappingVisitor jdbcMappingVisitor,
      SqlLogFormattingVisitor sqlLogFormattingVisitor,
      ExpressionFunctions expressionFunctions,
      boolean pagingForceOffsetFetch) {
    super(jdbcMappingVisitor, sqlLogFormattingVisitor, expressionFunctions);
    this.pagingForceOffsetFetch = pagingForceOffsetFetch;
  }

  @Override
  public String getName() {
    return "mssql";
  }

  @Override
  protected SqlNode toForUpdateSqlNode(
      SqlNode sqlNode, SelectForUpdateType forUpdateType, int waitSeconds, String... aliases) {
    MssqlForUpdateTransformer transformer =
        new MssqlForUpdateTransformer(forUpdateType, waitSeconds, aliases);
    return transformer.transform(sqlNode);
  }

  @Override
  protected SqlNode toPagingSqlNode(SqlNode sqlNode, long offset, long limit) {
    MssqlPagingTransformer transformer =
        new MssqlPagingTransformer(offset, limit, this.pagingForceOffsetFetch);
    return transformer.transform(sqlNode);
  }

  @Override
  public PreparedSql getSequenceNextValSql(String qualifiedSequenceName, long allocationSize) {
    if (qualifiedSequenceName == null) {
      throw new DomaNullPointerException("qualifiedSequenceName");
    }
    String rawSql = "select next value for " + qualifiedSequenceName;
    return new PreparedSql(
        SqlKind.SELECT, rawSql, rawSql, null, Collections.emptyList(), SqlLogType.FORMATTED);
  }

  @Override
  public boolean supportsSequence() {
    return true;
  }

  @Override
  public ScriptBlockContext createScriptBlockContext() {
    return new MssqlScriptBlockContext();
  }

  public static class MssqlJdbcMappingVisitor extends Mssql2008JdbcMappingVisitor {}

  public static class MssqlSqlLogFormattingVisitor extends Mssql2008SqlLogFormattingVisitor {}

  public static class MssqlExpressionFunctions extends Mssql2008ExpressionFunctions {

    public MssqlExpressionFunctions() {
      super();
    }

    public MssqlExpressionFunctions(char[] wildcards) {
      super(wildcards);
    }

    protected MssqlExpressionFunctions(char escapeChar, char[] wildcards) {
      super(escapeChar, wildcards);
    }
  }

  public static class MssqlScriptBlockContext extends Mssql2008ScriptBlockContext {}
}

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
package org.seasar.doma.jdbc.dialect;

import java.sql.SQLException;
import java.util.Collections;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.expr.ExpressionFunctions;
import org.seasar.doma.internal.jdbc.dialect.HsqldbPagingTransformer;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogFormattingVisitor;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.SqlNode;

/** A dialect for HSQLDB. */
public class HsqldbDialect extends StandardDialect {

  /** the error code that represents unique violation */
  protected static final int UNIQUE_CONSTRAINT_VIOLATION_ERROR_CODE = -104;

  public HsqldbDialect() {
    this(
        new HsqldbJdbcMappingVisitor(),
        new HsqldbSqlLogFormattingVisitor(),
        new HsqldbExpressionFunctions());
  }

  public HsqldbDialect(JdbcMappingVisitor jdbcMappingVisitor) {
    this(jdbcMappingVisitor, new HsqldbSqlLogFormattingVisitor(), new HsqldbExpressionFunctions());
  }

  public HsqldbDialect(SqlLogFormattingVisitor sqlLogFormattingVisitor) {
    this(new HsqldbJdbcMappingVisitor(), sqlLogFormattingVisitor, new HsqldbExpressionFunctions());
  }

  public HsqldbDialect(ExpressionFunctions expressionFunctions) {
    this(new HsqldbJdbcMappingVisitor(), new HsqldbSqlLogFormattingVisitor(), expressionFunctions);
  }

  public HsqldbDialect(
      JdbcMappingVisitor jdbcMappingVisitor, SqlLogFormattingVisitor sqlLogFormattingVisitor) {
    this(jdbcMappingVisitor, sqlLogFormattingVisitor, new HsqldbExpressionFunctions());
  }

  public HsqldbDialect(
      JdbcMappingVisitor jdbcMappingVisitor,
      SqlLogFormattingVisitor sqlLogFormattingVisitor,
      ExpressionFunctions expressionFunctions) {
    super(jdbcMappingVisitor, sqlLogFormattingVisitor, expressionFunctions);
  }

  @Override
  public String getName() {
    return "hsqldb";
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
    String rawSql = "call identity()";
    return new PreparedSql(
        SqlKind.SELECT, rawSql, rawSql, null, Collections.emptyList(), SqlLogType.FORMATTED);
  }

  @Override
  public PreparedSql getSequenceNextValSql(String qualifiedSequenceName, long allocationSize) {
    if (qualifiedSequenceName == null) {
      throw new DomaNullPointerException("qualifiedSequenceName");
    }
    String rawSql =
        "select next value for "
            + qualifiedSequenceName
            + " from information_schema.system_tables where table_name = 'SYSTEM_TABLES'";
    return new PreparedSql(
        SqlKind.SELECT, rawSql, rawSql, null, Collections.emptyList(), SqlLogType.FORMATTED);
  }

  @Override
  public boolean isUniqueConstraintViolated(SQLException sqlException) {
    if (sqlException == null) {
      throw new DomaNullPointerException("sqlException");
    }
    int code = getErrorCode(sqlException);
    return UNIQUE_CONSTRAINT_VIOLATION_ERROR_CODE == code;
  }

  @Override
  protected SqlNode toPagingSqlNode(SqlNode sqlNode, long offset, long limit) {
    HsqldbPagingTransformer transformer = new HsqldbPagingTransformer(offset, limit);
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
  public boolean supportsSequence() {
    return true;
  }

  public static class HsqldbJdbcMappingVisitor extends StandardJdbcMappingVisitor {}

  public static class HsqldbSqlLogFormattingVisitor extends StandardSqlLogFormattingVisitor {}

  public static class HsqldbExpressionFunctions extends StandardExpressionFunctions {

    public HsqldbExpressionFunctions() {
      super();
    }

    public HsqldbExpressionFunctions(char[] wildcards) {
      super(wildcards);
    }

    protected HsqldbExpressionFunctions(char escapeChar, char[] wildcards) {
      super(escapeChar, wildcards);
    }
  }
}

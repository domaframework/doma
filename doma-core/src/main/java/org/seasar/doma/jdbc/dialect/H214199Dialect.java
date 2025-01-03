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
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.expr.ExpressionFunctions;
import org.seasar.doma.internal.jdbc.dialect.H2ForUpdateTransformer;
import org.seasar.doma.internal.jdbc.dialect.H2PagingTransformer;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SqlLogFormattingVisitor;
import org.seasar.doma.jdbc.SqlNode;

/** A dialect for H2 version 1.4.199 and below. */
public class H214199Dialect extends H212126Dialect {

  /** the error code that represents unique violation */
  protected static final int UNIQUE_CONSTRAINT_VIOLATION_ERROR_CODE = 23505;

  public H214199Dialect() {
    this(
        new H214199JdbcMappingVisitor(),
        new H214199SqlLogFormattingVisitor(),
        new H214199ExpressionFunctions());
  }

  public H214199Dialect(JdbcMappingVisitor jdbcMappingVisitor) {
    this(
        jdbcMappingVisitor, new H214199SqlLogFormattingVisitor(), new H214199ExpressionFunctions());
  }

  public H214199Dialect(SqlLogFormattingVisitor sqlLogFormattingVisitor) {
    this(
        new H214199JdbcMappingVisitor(), sqlLogFormattingVisitor, new H214199ExpressionFunctions());
  }

  public H214199Dialect(ExpressionFunctions expressionFunctions) {
    this(
        new H214199JdbcMappingVisitor(), new H214199SqlLogFormattingVisitor(), expressionFunctions);
  }

  public H214199Dialect(
      JdbcMappingVisitor jdbcMappingVisitor, SqlLogFormattingVisitor sqlLogFormattingVisitor) {
    this(jdbcMappingVisitor, sqlLogFormattingVisitor, new H214199ExpressionFunctions());
  }

  public H214199Dialect(
      JdbcMappingVisitor jdbcMappingVisitor,
      SqlLogFormattingVisitor sqlLogFormattingVisitor,
      ExpressionFunctions expressionFunctions) {
    super(jdbcMappingVisitor, sqlLogFormattingVisitor, expressionFunctions);
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
    H2PagingTransformer transformer = new H2PagingTransformer(offset, limit);
    return transformer.transform(sqlNode);
  }

  @Override
  protected SqlNode toForUpdateSqlNode(
      SqlNode sqlNode, SelectForUpdateType forUpdateType, int waitSeconds, String... aliases) {
    H2ForUpdateTransformer transformer =
        new H2ForUpdateTransformer(forUpdateType, waitSeconds, aliases);
    return transformer.transform(sqlNode);
  }

  public static class H214199JdbcMappingVisitor extends H212126JdbcMappingVisitor {}

  public static class H214199SqlLogFormattingVisitor extends H212126SqlLogFormattingVisitor {}

  public static class H214199ExpressionFunctions extends H212126ExpressionFunctions {

    public H214199ExpressionFunctions() {
      super();
    }

    public H214199ExpressionFunctions(char[] wildcards) {
      super(wildcards);
    }

    protected H214199ExpressionFunctions(char escapeChar, char[] wildcards) {
      super(escapeChar, wildcards);
    }
  }
}

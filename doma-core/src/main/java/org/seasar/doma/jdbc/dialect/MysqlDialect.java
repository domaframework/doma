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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.expr.ExpressionFunctions;
import org.seasar.doma.internal.jdbc.dialect.MysqlCountCalculatingTransformer;
import org.seasar.doma.internal.jdbc.dialect.MysqlCountGettingTransformer;
import org.seasar.doma.internal.jdbc.dialect.MysqlForUpdateTransformer;
import org.seasar.doma.internal.jdbc.dialect.MysqlPagingTransformer;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.ScriptBlockContext;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SqlLogFormattingVisitor;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.criteria.query.CriteriaBuilder;
import org.seasar.doma.jdbc.query.UpsertAssembler;
import org.seasar.doma.jdbc.query.UpsertAssemblerContext;

/** A dialect for MySQL. */
public class MysqlDialect extends StandardDialect {

  /** the set of {@literal SQLState} code that represents unique violation */
  protected static final Set<Integer> UNIQUE_CONSTRAINT_VIOLATION_ERROR_CODES =
      new HashSet<>(Arrays.asList(1022, 1062));

  /** the quotation mark of the start */
  protected static final char OPEN_QUOTE = '`';

  /** the quotation mark of the end */
  protected static final char CLOSE_QUOTE = '`';

  public static final MySqlVersion DEFAULT_VERSION = MySqlVersion.V5;

  protected final MySqlVersion version;

  public MysqlDialect() {
    this(DEFAULT_VERSION);
  }

  public MysqlDialect(MySqlVersion version) {
    this(
        new MysqlJdbcMappingVisitor(),
        new MysqlSqlLogFormattingVisitor(),
        new MysqlExpressionFunctions(),
        version);
  }

  public MysqlDialect(JdbcMappingVisitor jdbcMappingVisitor) {
    this(jdbcMappingVisitor, DEFAULT_VERSION);
  }

  public MysqlDialect(JdbcMappingVisitor jdbcMappingVisitor, MySqlVersion version) {
    this(
        jdbcMappingVisitor,
        new MysqlSqlLogFormattingVisitor(),
        new MysqlExpressionFunctions(),
        version);
  }

  public MysqlDialect(SqlLogFormattingVisitor sqlLogFormattingVisitor) {
    this(sqlLogFormattingVisitor, DEFAULT_VERSION);
  }

  public MysqlDialect(SqlLogFormattingVisitor sqlLogFormattingVisitor, MySqlVersion version) {
    this(
        new MysqlJdbcMappingVisitor(),
        sqlLogFormattingVisitor,
        new MysqlExpressionFunctions(),
        version);
  }

  public MysqlDialect(ExpressionFunctions expressionFunctions) {
    this(expressionFunctions, DEFAULT_VERSION);
  }

  public MysqlDialect(ExpressionFunctions expressionFunctions, MySqlVersion version) {
    this(
        new MysqlJdbcMappingVisitor(),
        new MysqlSqlLogFormattingVisitor(),
        expressionFunctions,
        version);
  }

  public MysqlDialect(
      JdbcMappingVisitor jdbcMappingVisitor, SqlLogFormattingVisitor sqlLogFormattingVisitor) {
    this(jdbcMappingVisitor, sqlLogFormattingVisitor, DEFAULT_VERSION);
  }

  public MysqlDialect(
      JdbcMappingVisitor jdbcMappingVisitor,
      SqlLogFormattingVisitor sqlLogFormattingVisitor,
      MySqlVersion version) {
    this(jdbcMappingVisitor, sqlLogFormattingVisitor, new MysqlExpressionFunctions(), version);
  }

  public MysqlDialect(
      JdbcMappingVisitor jdbcMappingVisitor,
      SqlLogFormattingVisitor sqlLogFormattingVisitor,
      ExpressionFunctions expressionFunctions) {
    this(jdbcMappingVisitor, sqlLogFormattingVisitor, expressionFunctions, DEFAULT_VERSION);
  }

  public MysqlDialect(
      JdbcMappingVisitor jdbcMappingVisitor,
      SqlLogFormattingVisitor sqlLogFormattingVisitor,
      ExpressionFunctions expressionFunctions,
      MySqlVersion version) {
    super(jdbcMappingVisitor, sqlLogFormattingVisitor, expressionFunctions);
    this.version = Objects.requireNonNull(version);
  }

  @Override
  public String getName() {
    return "mysql";
  }

  @Override
  public boolean isUniqueConstraintViolated(SQLException sqlException) {
    if (sqlException == null) {
      throw new DomaNullPointerException("sqlException");
    }
    int code = getErrorCode(sqlException);
    return UNIQUE_CONSTRAINT_VIOLATION_ERROR_CODES.contains(code);
  }

  @Override
  public boolean supportsAutoGeneratedKeys() {
    return true;
  }

  @Override
  public boolean supportsIdentity() {
    return true;
  }

  @Override
  public boolean supportsSelectForUpdate(SelectForUpdateType type, boolean withTargets) {
    switch (version) {
      case V5:
        return type == SelectForUpdateType.NORMAL && !withTargets;
      case V8:
        return type == SelectForUpdateType.NORMAL || type == SelectForUpdateType.NOWAIT;
      default:
        throw new IllegalStateException(version.toString());
    }
  }

  @Override
  public boolean supportsAliasInDeleteClause() {
    return true;
  }

  @Override
  public boolean supportsBatchExecutionReturningGeneratedValues() {
    return true;
  }

  @Override
  protected SqlNode toCountCalculatingSqlNode(SqlNode sqlNode) {
    switch (version) {
      case V5:
        MysqlCountCalculatingTransformer transformer = new MysqlCountCalculatingTransformer();
        return transformer.transform(sqlNode);
      case V8:
        return super.toCountCalculatingSqlNode(sqlNode);
      default:
        throw new IllegalStateException(version.toString());
    }
  }

  @Override
  protected SqlNode toPagingSqlNode(SqlNode sqlNode, long offset, long limit) {
    MysqlPagingTransformer transformer = new MysqlPagingTransformer(offset, limit);
    return transformer.transform(sqlNode);
  }

  @Override
  protected SqlNode toForUpdateSqlNode(
      SqlNode sqlNode, SelectForUpdateType forUpdateType, int waitSeconds, String... aliases) {
    MysqlForUpdateTransformer transformer =
        new MysqlForUpdateTransformer(forUpdateType, waitSeconds, aliases);
    return transformer.transform(sqlNode);
  }

  @Override
  protected SqlNode toCountGettingSqlNode(SqlNode sqlNode) {
    switch (version) {
      case V5:
        MysqlCountGettingTransformer transformer = new MysqlCountGettingTransformer();
        return transformer.transform(sqlNode);
      case V8:
        return super.toCountGettingSqlNode(sqlNode);
      default:
        throw new IllegalStateException(version.toString());
    }
  }

  @Override
  public String getScriptBlockDelimiter() {
    return "/";
  }

  @Override
  public ScriptBlockContext createScriptBlockContext() {
    return new MysqlScriptBlockContext();
  }

  @Override
  public String applyQuote(String name) {
    return OPEN_QUOTE + name + CLOSE_QUOTE;
  }

  @Override
  public CriteriaBuilder getCriteriaBuilder() {
    return new MysqlCriteriaBuilder();
  }

  public static class MysqlJdbcMappingVisitor extends StandardJdbcMappingVisitor {}

  public static class MysqlSqlLogFormattingVisitor extends StandardSqlLogFormattingVisitor {}

  public static class MysqlExpressionFunctions extends StandardExpressionFunctions {

    public MysqlExpressionFunctions() {
      super();
    }

    public MysqlExpressionFunctions(char[] wildcards) {
      super(wildcards);
    }

    protected MysqlExpressionFunctions(char escapeChar, char[] wildcards) {
      super(escapeChar, wildcards);
    }
  }

  public static class MysqlScriptBlockContext extends StandardScriptBlockContext {

    protected MysqlScriptBlockContext() {
      sqlBlockStartKeywordsList.add(Arrays.asList("create", "procedure"));
      sqlBlockStartKeywordsList.add(Arrays.asList("create", "function"));
      sqlBlockStartKeywordsList.add(Arrays.asList("create", "trigger"));
      sqlBlockStartKeywordsList.add(Arrays.asList("alter", "procedure"));
      sqlBlockStartKeywordsList.add(Arrays.asList("alter", "function"));
      sqlBlockStartKeywordsList.add(Arrays.asList("alter", "trigger"));
      sqlBlockStartKeywordsList.add(Collections.singletonList("declare"));
      sqlBlockStartKeywordsList.add(Collections.singletonList("begin"));
    }
  }

  public static class MysqlCriteriaBuilder extends StandardCriteriaBuilder {

    @Override
    public void offsetAndFetch(PreparedSqlBuilder buf, int offset, int limit) {
      buf.appendSql(" limit ");
      if (limit > 0) {
        buf.appendSql(Integer.toString(limit));
      } else {
        buf.appendSql(MysqlPagingTransformer.MAXIMUM_LIMIT);
      }
      buf.appendSql(" offset ");
      buf.appendSql(Integer.toString(offset));
    }
  }

  @Override
  public UpsertAssembler getUpsertAssembler(UpsertAssemblerContext context) {
    return new MysqlUpsertAssembler(context, version);
  }

  @Override
  public boolean supportsReturning() {
    return false;
  }

  public enum MySqlVersion {
    V5,
    V8
  }
}

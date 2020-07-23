package org.seasar.doma.jdbc.dialect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.expr.ExpressionFunctions;
import org.seasar.doma.internal.jdbc.dialect.PostgresForUpdateTransformer;
import org.seasar.doma.internal.jdbc.dialect.PostgresPagingTransformer;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.internal.jdbc.util.DatabaseObjectUtil;
import org.seasar.doma.jdbc.InParameter;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.ScriptBlockContext;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogFormattingVisitor;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.criteria.option.ForUpdateOption;
import org.seasar.doma.jdbc.criteria.query.AliasManager;
import org.seasar.doma.jdbc.criteria.query.CriteriaBuilder;
import org.seasar.doma.jdbc.type.AbstractResultSetType;
import org.seasar.doma.jdbc.type.JdbcType;

/** A dialect for PostgreSQL. */
public class PostgresDialect extends StandardDialect {

  /** the {@literal SQLState} that represents unique violation */
  protected static final String UNIQUE_CONSTRAINT_VIOLATION_STATE_CODE = "23505";

  /** the JDBC type for {@link ResultSet} */
  protected static final JdbcType<ResultSet> RESULT_SET = new PostgresResultSetType();

  public PostgresDialect() {
    this(
        new PostgresJdbcMappingVisitor(),
        new PostgresSqlLogFormattingVisitor(),
        new PostgresExpressionFunctions());
  }

  public PostgresDialect(JdbcMappingVisitor jdbcMappingVisitor) {
    this(
        jdbcMappingVisitor,
        new PostgresSqlLogFormattingVisitor(),
        new PostgresExpressionFunctions());
  }

  public PostgresDialect(SqlLogFormattingVisitor sqlLogFormattingVisitor) {
    this(
        new PostgresJdbcMappingVisitor(),
        sqlLogFormattingVisitor,
        new PostgresExpressionFunctions());
  }

  public PostgresDialect(ExpressionFunctions expressionFunctions) {
    this(
        new PostgresJdbcMappingVisitor(),
        new PostgresSqlLogFormattingVisitor(),
        expressionFunctions);
  }

  public PostgresDialect(
      JdbcMappingVisitor jdbcMappingVisitor, SqlLogFormattingVisitor sqlLogFormattingVisitor) {
    this(jdbcMappingVisitor, sqlLogFormattingVisitor, new PostgresExpressionFunctions());
  }

  public PostgresDialect(
      JdbcMappingVisitor jdbcMappingVisitor,
      SqlLogFormattingVisitor sqlLogFormattingVisitor,
      ExpressionFunctions expressionFunctions) {
    super(jdbcMappingVisitor, sqlLogFormattingVisitor, expressionFunctions);
  }

  @Override
  public String getName() {
    return "postgres";
  }

  @Override
  protected SqlNode toForUpdateSqlNode(
      SqlNode sqlNode, SelectForUpdateType forUpdateType, int waitSeconds, String... aliases) {
    PostgresForUpdateTransformer transformer =
        new PostgresForUpdateTransformer(forUpdateType, waitSeconds, aliases);
    return transformer.transform(sqlNode);
  }

  @Override
  protected SqlNode toPagingSqlNode(SqlNode sqlNode, long offset, long limit) {
    PostgresPagingTransformer transformer = new PostgresPagingTransformer(offset, limit);
    return transformer.transform(sqlNode);
  }

  @Override
  public boolean isUniqueConstraintViolated(SQLException sqlException) {
    if (sqlException == null) {
      throw new DomaNullPointerException("sqlException");
    }
    String state = getSQLState(sqlException);
    return UNIQUE_CONSTRAINT_VIOLATION_STATE_CODE.equals(state);
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
    String identitySeqFuncExpr =
        createIdentitySequenceFunctionExpression(
            catalogName,
            schemaName,
            tableName,
            columnName,
            isQuoteRequired,
            isIdColumnQuoteRequired);
    StringBuilder buf = new StringBuilder(64);
    buf.append("select currval(");
    buf.append(identitySeqFuncExpr);
    buf.append(")");
    String rawSql = buf.toString();
    return new PreparedSql(
        SqlKind.SELECT,
        rawSql,
        rawSql,
        null,
        Collections.<InParameter<?>>emptyList(),
        SqlLogType.FORMATTED);
  }

  @Override
  public Sql<?> getIdentityReservationSql(
      String catalogName,
      String schemaName,
      String tableName,
      String columnName,
      boolean isQuoteRequired,
      boolean isIdColumnQuoteRequired,
      int reservationSize) {
    if (tableName == null) {
      throw new DomaNullPointerException("tableName");
    }
    if (columnName == null) {
      throw new DomaNullPointerException("columnName");
    }
    String identitySeqFuncExpr =
        createIdentitySequenceFunctionExpression(
            catalogName,
            schemaName,
            tableName,
            columnName,
            isQuoteRequired,
            isIdColumnQuoteRequired);
    StringBuilder buf = new StringBuilder(64);
    buf.append("select nextval(");
    buf.append(identitySeqFuncExpr);
    buf.append(") from generate_series(1, ");
    buf.append(reservationSize);
    buf.append(")");
    String rawSql = buf.toString();
    return new PreparedSql(
        SqlKind.SELECT,
        rawSql,
        rawSql,
        null,
        Collections.<InParameter<?>>emptyList(),
        SqlLogType.FORMATTED);
  }

  protected String createIdentitySequenceFunctionExpression(
      String catalogName,
      String schemaName,
      String tableName,
      String columnName,
      boolean isQuoteRequired,
      boolean isIdColumnQuoteRequired) {
    String qualifiedTableName =
        DatabaseObjectUtil.getQualifiedName(
            isQuoteRequired ? this::applyQuote : Function.identity(),
            catalogName,
            schemaName,
            tableName);
    String colName = isIdColumnQuoteRequired ? columnName : columnName.toLowerCase();
    return "pg_catalog.pg_get_serial_sequence('" + qualifiedTableName + "', '" + colName + "')";
  }

  @Override
  public PreparedSql getSequenceNextValSql(String qualifiedSequenceName, long allocationSize) {
    if (qualifiedSequenceName == null) {
      throw new DomaNullPointerException("qualifiedSequenceName");
    }
    String rawSql = "select nextval('" + qualifiedSequenceName + "')";
    return new PreparedSql(
        SqlKind.SELECT,
        rawSql,
        rawSql,
        null,
        Collections.<InParameter<?>>emptyList(),
        SqlLogType.FORMATTED);
  }

  @Override
  public boolean supportsIdentity() {
    return true;
  }

  @Override
  public boolean supportsSequence() {
    return true;
  }

  @Override
  public boolean supportsIdentityReservation() {
    return true;
  }

  @Override
  public boolean supportsSelectForUpdate(SelectForUpdateType type, boolean withTargets) {
    return type == SelectForUpdateType.NORMAL || type == SelectForUpdateType.NOWAIT;
  }

  @Override
  public boolean supportsResultSetReturningAsOutParameter() {
    return true;
  }

  @Override
  public JdbcType<ResultSet> getResultSetType() {
    return RESULT_SET;
  }

  @Override
  public ScriptBlockContext createScriptBlockContext() {
    return new PostgresScriptBlockContext();
  }

  @Override
  public CriteriaBuilder getCriteriaBuilder() {
    return new PostgresCriteriaBuilder();
  }

  public static class PostgresResultSetType extends AbstractResultSetType {

    public PostgresResultSetType() {
      super(Types.OTHER);
    }
  }

  public static class PostgresJdbcMappingVisitor extends StandardJdbcMappingVisitor {}

  public static class PostgresSqlLogFormattingVisitor extends StandardSqlLogFormattingVisitor {}

  public static class PostgresExpressionFunctions extends StandardExpressionFunctions {

    public PostgresExpressionFunctions() {
      super();
    }

    public PostgresExpressionFunctions(char[] wildcards) {
      super(wildcards);
    }

    protected PostgresExpressionFunctions(char escapeChar, char[] wildcards) {
      super(escapeChar, wildcards);
    }
  }

  public static class PostgresScriptBlockContext implements ScriptBlockContext {

    protected boolean inBlock;

    @Override
    public void addKeyword(String keyword) {
      if ("$$".equals(keyword)) {
        inBlock = !inBlock;
      }
    }

    @Override
    public boolean isInBlock() {
      return inBlock;
    }
  }

  public static class PostgresCriteriaBuilder extends StandardCriteriaBuilder {
    @Override
    public void forUpdate(
        PreparedSqlBuilder buf,
        ForUpdateOption option,
        Consumer<PropertyMetamodel<?>> column,
        AliasManager aliasManager) {
      option.accept(
          new ForUpdateOption.Visitor() {

            @Override
            public void visit(ForUpdateOption.Basic basic) {
              buf.appendSql(" for update");
              of(basic.propertyMetamodels);
            }

            @Override
            public void visit(ForUpdateOption.NoWait noWait) {
              buf.appendSql(" for update");
              of(noWait.propertyMetamodels);
              buf.appendSql(" nowait");
            }

            @Override
            public void visit(ForUpdateOption.Wait wait) {
              buf.appendSql(" for update");
              of(wait.propertyMetamodels);
            }

            private void of(List<PropertyMetamodel<?>> propertyMetamodels) {
              Set<String> aliases = new LinkedHashSet<>();
              for (PropertyMetamodel<?> p : propertyMetamodels) {
                String alias = aliasManager.getAlias(p);
                aliases.add(alias);
              }
              if (!aliases.isEmpty()) {
                buf.appendSql(" of ");
                for (String alias : aliases) {
                  buf.appendSql(alias);
                  buf.appendSql(", ");
                }
                buf.cutBackSql(2);
              }
            }
          });
    }
  }
}

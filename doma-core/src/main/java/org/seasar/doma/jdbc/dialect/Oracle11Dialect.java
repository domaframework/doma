package org.seasar.doma.jdbc.dialect;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.expr.ExpressionFunctions;
import org.seasar.doma.internal.jdbc.dialect.OracleForUpdateTransformer;
import org.seasar.doma.internal.jdbc.dialect.OraclePagingTransformer;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.internal.util.AssertionUtil;
import org.seasar.doma.jdbc.JdbcMappingFunction;
import org.seasar.doma.jdbc.JdbcMappingHint;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.ScriptBlockContext;
import org.seasar.doma.jdbc.SelectForUpdateType;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogFormatter;
import org.seasar.doma.jdbc.SqlLogFormattingFunction;
import org.seasar.doma.jdbc.SqlLogFormattingVisitor;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.criteria.option.ForUpdateOption;
import org.seasar.doma.jdbc.criteria.query.AliasManager;
import org.seasar.doma.jdbc.criteria.query.CriteriaBuilder;
import org.seasar.doma.jdbc.type.AbstractResultSetType;
import org.seasar.doma.jdbc.type.JdbcType;
import org.seasar.doma.jdbc.type.JdbcTypes;
import org.seasar.doma.wrapper.BooleanWrapper;
import org.seasar.doma.wrapper.DateWrapper;
import org.seasar.doma.wrapper.LocalDateTimeWrapper;
import org.seasar.doma.wrapper.LocalDateWrapper;
import org.seasar.doma.wrapper.LocalTimeWrapper;
import org.seasar.doma.wrapper.TimeWrapper;
import org.seasar.doma.wrapper.TimestampWrapper;
import org.seasar.doma.wrapper.UtilDateWrapper;

/** A dialect for Oracle Database 11g and below. */
public class Oracle11Dialect extends StandardDialect {

  /** the error code that represents unique violation */
  protected static final int UNIQUE_CONSTRAINT_VIOLATION_ERROR_CODE = 1;

  /** the JDBC type for {@link ResultSet} */
  protected static final JdbcType<ResultSet> RESULT_SET = new OracleResultSetType();

  public Oracle11Dialect() {
    this(
        new Oracle11JdbcMappingVisitor(),
        new Oracle11SqlLogFormattingVisitor(),
        new Oracle11ExpressionFunctions());
  }

  public Oracle11Dialect(JdbcMappingVisitor jdbcMappingVisitor) {
    this(
        jdbcMappingVisitor,
        new Oracle11SqlLogFormattingVisitor(),
        new Oracle11ExpressionFunctions());
  }

  public Oracle11Dialect(SqlLogFormattingVisitor sqlLogFormattingVisitor) {
    this(
        new Oracle11JdbcMappingVisitor(),
        sqlLogFormattingVisitor,
        new Oracle11ExpressionFunctions());
  }

  public Oracle11Dialect(ExpressionFunctions expressionFunctions) {
    this(
        new Oracle11JdbcMappingVisitor(),
        new Oracle11SqlLogFormattingVisitor(),
        expressionFunctions);
  }

  public Oracle11Dialect(
      JdbcMappingVisitor jdbcMappingVisitor, SqlLogFormattingVisitor sqlLogFormattingVisitor) {
    this(jdbcMappingVisitor, sqlLogFormattingVisitor, new Oracle11ExpressionFunctions());
  }

  public Oracle11Dialect(
      JdbcMappingVisitor jdbcMappingVisitor,
      SqlLogFormattingVisitor sqlLogFormattingVisitor,
      ExpressionFunctions expressionFunctions) {
    super(jdbcMappingVisitor, sqlLogFormattingVisitor, expressionFunctions);
  }

  @Override
  public String getName() {
    return "oracle";
  }

  @Override
  public boolean supportsBatchUpdateResults() {
    return false;
  }

  @Override
  protected SqlNode toForUpdateSqlNode(
      SqlNode sqlNode, SelectForUpdateType forUpdateType, int waitSeconds, String... aliases) {
    OracleForUpdateTransformer transformer =
        new OracleForUpdateTransformer(forUpdateType, waitSeconds, aliases);
    return transformer.transform(sqlNode);
  }

  @Override
  protected SqlNode toPagingSqlNode(SqlNode sqlNode, long offset, long limit) {
    OraclePagingTransformer transformer = new OraclePagingTransformer(offset, limit);
    return transformer.transform(sqlNode);
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
  public PreparedSql getSequenceNextValSql(String qualifiedSequenceName, long allocationSize) {
    if (qualifiedSequenceName == null) {
      throw new DomaNullPointerException("qualifiedSequenceName");
    }
    String rawSql = "select " + qualifiedSequenceName + ".nextval from dual";
    return new PreparedSql(
        SqlKind.SELECT, rawSql, rawSql, null, Collections.emptyList(), SqlLogType.FORMATTED);
  }

  @Override
  public boolean supportsIdentity() {
    return false;
  }

  @Override
  public boolean supportsSequence() {
    return true;
  }

  @Override
  public boolean supportsSelectForUpdate(SelectForUpdateType type, boolean withTargets) {
    return true;
  }

  @Override
  public boolean supportsResultSetReturningAsOutParameter() {
    return true;
  }

  @Override
  public boolean supportsModOperator() {
    return false;
  }

  @Override
  public JdbcType<ResultSet> getResultSetType() {
    return RESULT_SET;
  }

  @Override
  public String getScriptBlockDelimiter() {
    return "/";
  }

  @Override
  public ScriptBlockContext createScriptBlockContext() {
    return new Oracle11ScriptBlockContext();
  }

  @Override
  public CriteriaBuilder getCriteriaBuilder() {
    return new Oracle11CriteriaBuilder();
  }

  public static class OracleResultSetType extends AbstractResultSetType {

    protected static final int CURSOR = -10;

    public OracleResultSetType() {
      super(CURSOR);
    }
  }

  public static class Oracle11JdbcMappingVisitor extends StandardJdbcMappingVisitor {

    @Override
    public Void visitBooleanWrapper(
        BooleanWrapper wrapper, JdbcMappingFunction p, JdbcMappingHint q) throws SQLException {
      return p.apply(wrapper, JdbcTypes.INTEGER_ADAPTIVE_BOOLEAN);
    }
  }

  public static class Oracle11SqlLogFormattingVisitor extends StandardSqlLogFormattingVisitor {

    /** the formatter for {@link Date} */
    protected final DateFormatter dateFormatter = new DateFormatter();

    /** the formatter for {@link Time} */
    protected final TimeFormatter timeFormatter = new TimeFormatter();

    /** the formatter for {@link Timestamp} */
    protected final TimestampFormatter timestampFormatter = new TimestampFormatter();

    /** the formatter for {@link java.util.Date} */
    protected final UtilDateFormatter utilDateFormatter = new UtilDateFormatter();

    /** the formatter for {@link LocalDate} */
    protected final LocalDateFormatter localDateFormatter = new LocalDateFormatter(dateFormatter);

    /** the formatter for {@link LocalDateTime} */
    protected final LocalDateTimeFormatter localDateTimeFormatter =
        new LocalDateTimeFormatter(timestampFormatter);

    /** the formatter for {@link LocalTime} */
    protected final LocalTimeFormatter localTimeFormatter = new LocalTimeFormatter(timeFormatter);

    @Override
    public String visitBooleanWrapper(BooleanWrapper wrapper, SqlLogFormattingFunction p, Void q)
        throws RuntimeException {
      return p.apply(wrapper, JdbcTypes.INTEGER_ADAPTIVE_BOOLEAN);
    }

    @Override
    public String visitDateWrapper(DateWrapper wrapper, SqlLogFormattingFunction p, Void q) {
      return p.apply(wrapper, dateFormatter);
    }

    @Override
    public String visitLocalDateWrapper(
        LocalDateWrapper wrapper, SqlLogFormattingFunction p, Void q) throws RuntimeException {
      return p.apply(wrapper, localDateFormatter);
    }

    @Override
    public String visitLocalDateTimeWrapper(
        LocalDateTimeWrapper wrapper, SqlLogFormattingFunction p, Void q) throws RuntimeException {
      return p.apply(wrapper, localDateTimeFormatter);
    }

    @Override
    public String visitLocalTimeWrapper(
        LocalTimeWrapper wrapper, SqlLogFormattingFunction p, Void q) throws RuntimeException {
      return p.apply(wrapper, localTimeFormatter);
    }

    @Override
    public String visitTimeWrapper(TimeWrapper wrapper, SqlLogFormattingFunction p, Void q) {
      return p.apply(wrapper, timeFormatter);
    }

    @Override
    public String visitTimestampWrapper(
        TimestampWrapper wrapper, SqlLogFormattingFunction p, Void q) {
      return p.apply(wrapper, timestampFormatter);
    }

    @Override
    public String visitUtilDateWrapper(
        UtilDateWrapper wrapper, SqlLogFormattingFunction p, Void q) {
      return p.apply(wrapper, utilDateFormatter);
    }

    /** A formatter that converts a {@link Date} object to a date literal. */
    protected static class DateFormatter implements SqlLogFormatter<Date> {

      @Override
      public String convertToLogFormat(Date value) {
        if (value == null) {
          return "null";
        }
        return "date'" + value + "'";
      }
    }

    /** A formatter that converts a {@link Time} object to a time literal. */
    protected static class TimeFormatter implements SqlLogFormatter<Time> {

      @Override
      public String convertToLogFormat(Time value) {
        if (value == null) {
          return "null";
        }
        return "time'" + value + "'";
      }
    }

    /** A formatter that converts a {@link Timestamp} object to a timestamp literal. */
    protected static class TimestampFormatter implements SqlLogFormatter<Timestamp> {

      @Override
      public String convertToLogFormat(Timestamp value) {
        if (value == null) {
          return "null";
        }
        return "timestamp'" + value + "'";
      }
    }

    /** A formatter that converts a {@link java.util.Date} object to a date literal. */
    protected static class UtilDateFormatter implements SqlLogFormatter<java.util.Date> {

      @Override
      public String convertToLogFormat(java.util.Date value) {
        if (value == null) {
          return "null";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return "timestamp'" + dateFormat.format(value) + "'";
      }
    }

    /** A formatter that converts a {@link LocalDate} object to a date literal. */
    protected static class LocalDateFormatter implements SqlLogFormatter<LocalDate> {

      protected final DateFormatter delegate;

      protected LocalDateFormatter(DateFormatter delegate) {
        AssertionUtil.assertNotNull(delegate);
        this.delegate = delegate;
      }

      @Override
      public String convertToLogFormat(LocalDate value) {
        if (value == null) {
          return "null";
        }
        return delegate.convertToLogFormat(Date.valueOf(value));
      }
    }

    /** A formatter that converts a {@link LocalDateTime} object to a timestamp literal. */
    protected static class LocalDateTimeFormatter implements SqlLogFormatter<LocalDateTime> {

      protected final TimestampFormatter delegate;

      protected LocalDateTimeFormatter(TimestampFormatter delegate) {
        AssertionUtil.assertNotNull(delegate);
        this.delegate = delegate;
      }

      @Override
      public String convertToLogFormat(LocalDateTime value) {
        if (value == null) {
          return "null";
        }
        return delegate.convertToLogFormat(Timestamp.valueOf(value));
      }
    }

    /** A formatter that converts a {@link LocalTime} object to a time literal. */
    protected static class LocalTimeFormatter implements SqlLogFormatter<LocalTime> {

      protected final TimeFormatter delegate;

      protected LocalTimeFormatter(TimeFormatter delegate) {
        AssertionUtil.assertNotNull(delegate);
        this.delegate = delegate;
      }

      @Override
      public String convertToLogFormat(LocalTime value) {
        if (value == null) {
          return "null";
        }
        return delegate.convertToLogFormat(Time.valueOf(value));
      }
    }
  }

  public static class Oracle11ExpressionFunctions extends StandardExpressionFunctions {

    private static final char[] DEFAULT_WILDCARDS = {'%', '_', '％', '＿'};

    public Oracle11ExpressionFunctions() {
      super(DEFAULT_WILDCARDS);
    }

    public Oracle11ExpressionFunctions(char[] wildcards) {
      super(wildcards);
    }

    protected Oracle11ExpressionFunctions(char escapeChar, char[] wildcards) {
      super(escapeChar, wildcards);
    }
  }

  public static class Oracle11ScriptBlockContext extends StandardScriptBlockContext {

    protected Oracle11ScriptBlockContext() {
      sqlBlockStartKeywordsList.add(Arrays.asList("create", "or", "replace", "procedure"));
      sqlBlockStartKeywordsList.add(Arrays.asList("create", "or", "replace", "function"));
      sqlBlockStartKeywordsList.add(Arrays.asList("create", "or", "replace", "trigger"));
      sqlBlockStartKeywordsList.add(Arrays.asList("create", "procedure"));
      sqlBlockStartKeywordsList.add(Arrays.asList("create", "function"));
      sqlBlockStartKeywordsList.add(Arrays.asList("create", "trigger"));
      sqlBlockStartKeywordsList.add(Collections.singletonList("declare"));
      sqlBlockStartKeywordsList.add(Collections.singletonList("begin"));
    }
  }

  public static class Oracle11CriteriaBuilder extends StandardCriteriaBuilder {
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
              buf.appendSql(" wait ");
              buf.appendSql(String.valueOf(wait.second));
            }

            private void of(List<PropertyMetamodel<?>> propertyMetamodels) {
              if (!propertyMetamodels.isEmpty()) {
                buf.appendSql(" of ");
                for (PropertyMetamodel<?> p : propertyMetamodels) {
                  column.accept(p);
                  buf.appendSql(", ");
                }
                buf.cutBackSql(2);
              }
            }
          });
    }
  }
}

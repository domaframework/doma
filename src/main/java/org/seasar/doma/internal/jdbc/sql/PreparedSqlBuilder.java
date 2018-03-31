package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.seasar.doma.jdbc.*;
import org.seasar.doma.wrapper.Wrapper;

public class PreparedSqlBuilder implements SqlContext {

  protected final List<InParameter<?>> parameters = new ArrayList<>();

  protected final StringBuilder rawSql = new StringBuilder(200);

  protected final StringBuilder formattedSql = new StringBuilder(200);

  protected final Config config;

  protected final SqlKind kind;

  protected final SqlLogFormattingFunction formattingFunction;

  protected final SqlLogType sqlLogType;

  public PreparedSqlBuilder(Config config, SqlKind kind, SqlLogType sqlLogType) {
    assertNotNull(config, kind, sqlLogType);
    this.config = config;
    this.kind = kind;
    this.sqlLogType = sqlLogType;
    this.formattingFunction = new ConvertToLogFormatFunction();
  }

  public void appendSql(String sql) {
    rawSql.append(sql);
    formattedSql.append(sql);
  }

  public void cutBackSql(int length) {
    rawSql.setLength(rawSql.length() - length);
    formattedSql.setLength(formattedSql.length() - length);
  }

  public <BASIC> void appendParameter(InParameter<BASIC> parameter) {
    rawSql.append("?");
    Wrapper<BASIC> wrapper = parameter.getWrapper();
    formattedSql.append(
        wrapper.accept(config.getDialect().getSqlLogFormattingVisitor(), formattingFunction, null));
    parameters.add(parameter);
  }

  public PreparedSql build(Function<String, String> commenter) {
    assertNotNull(commenter);
    return new PreparedSql(kind, rawSql, formattedSql, null, parameters, sqlLogType, commenter);
  }
}

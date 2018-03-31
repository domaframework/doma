package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import org.seasar.doma.jdbc.*;
import org.seasar.doma.wrapper.Wrapper;

public class CallableSqlBuilder
    implements SqlParameterVisitor<CallableSqlBuilder.Context, RuntimeException> {

  protected final Config config;

  protected final SqlKind kind;

  protected final ResultParameter<?> resultParameter;

  protected final List<SqlParameter> parameters;

  protected final String moduleName;

  protected final SqlLogType sqlLogType;

  protected final SqlLogFormattingFunction formattingFunction;

  protected boolean began;

  public CallableSqlBuilder(
      Config config,
      SqlKind kind,
      String moduleName,
      List<SqlParameter> parameters,
      SqlLogType sqlLogType) {
    this(config, kind, moduleName, parameters, sqlLogType, null);
  }

  public CallableSqlBuilder(
      Config config,
      SqlKind kind,
      String moduleName,
      List<SqlParameter> parameters,
      SqlLogType sqlLogType,
      ResultParameter<?> resultParameter) {
    assertNotNull(config, kind, parameters, moduleName, sqlLogType);
    this.config = config;
    this.kind = kind;
    this.resultParameter = resultParameter;
    this.parameters = parameters;
    this.moduleName = moduleName;
    this.sqlLogType = sqlLogType;
    this.formattingFunction = new ConvertToLogFormatFunction();
  }

  public CallableSql build(Function<String, String> commenter) {
    assertNotNull(commenter);
    Context context = new Context();
    context.append("{");
    if (resultParameter != null) {
      resultParameter.accept(this, context);
      context.append("= ");
    }
    context.append("call ");
    context.append(moduleName);
    context.append("(");
    for (SqlParameter parameter : parameters) {
      parameter.accept(this, context);
    }
    context.cutBackIfNecessary();
    context.append(")}");
    LinkedList<SqlParameter> allParameters = new LinkedList<>(parameters);
    if (resultParameter != null) {
      allParameters.addFirst(resultParameter);
    }
    return new CallableSql(
        kind,
        context.getSqlBuf(),
        context.getFormattedSqlBuf(),
        allParameters,
        sqlLogType,
        commenter);
  }

  @Override
  public <BASIC> void visitInParameter(InParameter<BASIC> parameter, Context p)
      throws RuntimeException {
    Wrapper<BASIC> wrapper = parameter.getWrapper();
    p.appendRawSql("?, ");
    p.appendFormattedSql(
        wrapper.accept(config.getDialect().getSqlLogFormattingVisitor(), formattingFunction, null));
    p.appendFormattedSql(", ");
    p.addParameter(parameter);
  }

  @Override
  public <BASIC> void visitOutParameter(OutParameter<BASIC> parameter, Context p)
      throws RuntimeException {
    p.appendRawSql("?, ");
    p.appendFormattedSql("?, ");
    p.addParameter(parameter);
  }

  @Override
  public <BASIC, INOUT extends InParameter<BASIC> & OutParameter<BASIC>> void visitInOutParameter(
      INOUT parameter, Context p) throws RuntimeException {
    visitInParameter(parameter, p);
  }

  @Override
  public <ELEMENT> void visitListParameter(ListParameter<ELEMENT> parameter, Context p)
      throws RuntimeException {
    if (config.getDialect().supportsResultSetReturningAsOutParameter()) {
      p.appendRawSql("?, ");
      p.appendFormattedSql("?, ");
      p.addParameter(parameter);
    }
  }

  @Override
  public <BASIC, RESULT> void visitSingleResultParameter(
      SingleResultParameter<BASIC, RESULT> parameter, Context p) throws RuntimeException {
    p.appendRawSql("? ");
    p.appendFormattedSql("? ");
  }

  @Override
  public <ELEMENT> void visitResultListParameter(ResultListParameter<ELEMENT> parameter, Context p)
      throws RuntimeException {
    p.appendRawSql("? ");
    p.appendFormattedSql("? ");
  }

  protected class Context {

    private final StringBuilder rawSqlBuf = new StringBuilder(200);

    private final StringBuilder formattedSqlBuf = new StringBuilder(200);

    private final List<SqlParameter> contextParameters = new ArrayList<>();

    protected void append(CharSequence sql) {
      appendRawSql(sql);
      appendFormattedSql(sql);
    }

    protected void cutBackIfNecessary() {
      if (!contextParameters.isEmpty()) {
        rawSqlBuf.setLength(rawSqlBuf.length() - 2);
        formattedSqlBuf.setLength(formattedSqlBuf.length() - 2);
      }
    }

    protected void appendRawSql(CharSequence sql) {
      rawSqlBuf.append(sql);
    }

    protected void appendFormattedSql(CharSequence sql) {
      formattedSqlBuf.append(sql);
    }

    protected CharSequence getSqlBuf() {
      return rawSqlBuf;
    }

    protected CharSequence getFormattedSqlBuf() {
      return formattedSqlBuf;
    }

    protected void addParameter(SqlParameter parameter) {
      contextParameters.add(parameter);
    }

    @Override
    public String toString() {
      return rawSqlBuf.toString();
    }
  }
}

package org.seasar.doma.jdbc.builder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.command.BatchModifyCommand;
import org.seasar.doma.jdbc.query.SqlBatchModifyQuery;
import org.seasar.doma.message.Message;

/**
 * A builder that builds an SQL statement for a batch execution.
 *
 * @author bakenezumi
 */
public abstract class BatchBuilder {

  final BatchBuildingHelper helper;

  final SqlBatchModifyQuery query;

  final ParamIndex paramIndex;

  final Map<Integer, String> paramNameMap;

  BatchBuilder(SqlBatchModifyQuery query) {
    this.helper = new BatchBuildingHelper();
    this.query = query;
    this.paramIndex = new ParamIndex();
    paramNameMap = new HashMap<>();
  }

  BatchBuilder(
      BatchBuildingHelper builder,
      SqlBatchModifyQuery query,
      ParamIndex paramIndex,
      Map<Integer, String> paramNameMap) {
    this.helper = builder;
    this.query = query;
    this.paramIndex = paramIndex;
    this.paramNameMap = paramNameMap;
  }

  static BatchBuilder newInstance(SqlBatchModifyQuery query) {
    if (query == null) {
      throw new DomaNullPointerException("query");
    }
    if (query.getClassName() == null) {
      query.setCallerClassName(BatchBuilder.class.getName());
    }
    return new InitialBatchBuilder(query);
  }

  /**
   * Appends an SQL fragment.
   *
   * @param sql the SQL fragment
   * @return a builder
   * @throws DomaNullPointerException if {@code sql} is {@code null}
   */
  public abstract BatchBuilder sql(String sql);

  /**
   * Removes the last SQL fragment or parameter.
   *
   * @return a builder
   */
  public abstract BatchBuilder removeLast();

  /**
   * Appends a parameter.
   *
   * <p>The parameter type must be one of basic types or holder types.
   *
   * @param <P> the parameter type
   * @param paramClass the parameter class
   * @param param the parameter
   * @return a builder
   * @throws DomaNullPointerException if {@code paramClass} is {@code null}
   */
  public <P> BatchBuilder param(Class<P> paramClass, P param) {
    if (paramClass == null) {
      throw new DomaNullPointerException("paramClass");
    }
    return appendParam(paramClass, param, false);
  }

  /**
   * Appends a parameter as literal.
   *
   * <p>The parameter type must be one of basic types or holder types.
   *
   * @param <P> the parameter type
   * @param paramClass the parameter class
   * @param param the parameter
   * @return a builder
   * @throws DomaNullPointerException if {@code paramClass} is {@code null}
   */
  public <P> BatchBuilder literal(Class<P> paramClass, P param) {
    if (paramClass == null) {
      throw new DomaNullPointerException("paramClass");
    }
    return appendParam(paramClass, param, true);
  }

  abstract <P> BatchBuilder appendParam(Class<P> paramClass, P param, boolean literal);

  BatchBuilder fixSql() {
    return new FixedBatchBuilder(helper, query, paramNameMap);
  }

  private void prepare() {
    query.clearParameters();
    for (var p : helper.getParams()) {
      query.addParameter(p.name, p.paramClass, p.params);
    }
    query.setSqlNode(helper.getSqlNode());
    query.prepare();
  }

  int[] execute(Supplier<BatchModifyCommand<?>> commandFactory) {
    if (query.getMethodName() == null) {
      query.setCallerMethodName("execute");
    }
    prepare();
    var command = commandFactory.get();
    var result = command.execute();
    query.complete();
    return result;
  }

  List<? extends Sql<?>> getSqls() {
    if (query.getMethodName() == null) {
      query.setCallerMethodName("getSqls");
    }
    prepare();
    return query.getSqls();
  }

  private static class InitialBatchBuilder extends BatchBuilder {

    private InitialBatchBuilder(SqlBatchModifyQuery query) {
      super(query);
    }

    private InitialBatchBuilder(
        BatchBuildingHelper builder,
        SqlBatchModifyQuery query,
        ParamIndex paramIndex,
        Map<Integer, String> paramNameMap) {
      super(builder, query, paramIndex, paramNameMap);
    }

    @Override
    public BatchBuilder sql(String sql) {
      if (sql == null) {
        throw new DomaNullPointerException("sql");
      }
      helper.appendSqlWithLineSeparator(sql);
      return new SubsequentBatchBuilder(helper, query, paramIndex, paramNameMap);
    }

    @Override
    public BatchBuilder removeLast() {
      helper.removeLast();
      return new SubsequentBatchBuilder(helper, query, paramIndex, paramNameMap);
    }

    @Override
    <P> BatchBuilder appendParam(Class<P> paramClass, P param, boolean literal) {
      var batchParam = new BatchParam<>(paramClass, paramIndex, literal);
      batchParam.add(param);
      helper.appendParam(batchParam);
      paramNameMap.put(paramIndex.getValue(), batchParam.name);
      paramIndex.increment();
      return new SubsequentBatchBuilder(helper, query, paramIndex, paramNameMap);
    }
  }

  private static class SubsequentBatchBuilder extends InitialBatchBuilder {

    SubsequentBatchBuilder(
        BatchBuildingHelper builder,
        SqlBatchModifyQuery query,
        ParamIndex paramIndex,
        Map<Integer, String> paramNameMap) {
      super(builder, query, paramIndex, paramNameMap);
    }

    @Override
    public BatchBuilder sql(String sql) {
      if (sql == null) {
        throw new DomaNullPointerException("sql");
      }
      super.helper.appendSql(sql);
      return this;
    }
  }

  private static class FixedBatchBuilder extends BatchBuilder {

    private FixedBatchBuilder(
        BatchBuildingHelper builder, SqlBatchModifyQuery query, Map<Integer, String> paramNameMap) {
      super(builder, query, new ParamIndex(), paramNameMap);
    }

    @Override
    public BatchBuilder sql(String sql) {
      return this;
    }

    @Override
    public BatchBuilder removeLast() {
      return this;
    }

    @Override
    <P> BatchBuilder appendParam(Class<P> paramClass, P param, boolean literal) {
      final var paramName = paramNameMap.get(paramIndex.getValue());
      if (paramName == null) {
        throw new JdbcException(Message.DOMA2231);
      }
      final var batchParam = helper.getParam(paramName);

      if (literal != batchParam.literal) {
        throw new JdbcException(Message.DOMA2230);
      }
      if (paramClass != batchParam.paramClass) {
        if (batchParam.paramClass == Object.class) {
          final var newBatchParam = new BatchParam<>(batchParam, paramClass);
          newBatchParam.add(param);
          helper.modifyParam(newBatchParam);
        } else if (param == null && paramClass == Object.class) {
          batchParam.add(null);
        } else {
          throw new JdbcException(Message.DOMA2229);
        }
      } else {
        @SuppressWarnings("unchecked")
        final var castedBatchParam = (BatchParam<P>) batchParam;
        castedBatchParam.add(param);
      }
      paramIndex.increment();
      return this;
    }
  }
}

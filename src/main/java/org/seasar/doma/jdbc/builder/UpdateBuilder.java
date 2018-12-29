package org.seasar.doma.jdbc.builder;

import java.sql.Statement;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.UniqueConstraintException;
import org.seasar.doma.jdbc.command.UpdateCommand;
import org.seasar.doma.jdbc.query.SqlUpdateQuery;

/**
 * UPDATE文を組み立て実行するクラスです。
 *
 * <p>このクラスはスレッドセーフではありません。
 *
 * <h3>例</h3>
 *
 * <h4>Java</h4>
 *
 * <pre>
 * UpdateBuilder builder = UpdateBuilder.newInstance(config);
 * builder.sql(&quot;update Emp&quot;);
 * builder.sql(&quot;set&quot;);
 * builder.sql(&quot;name = &quot;).param(String.class, &quot;SMIHT&quot;).sql(&quot;,&quot;);
 * builder.sql(&quot;salary = &quot;).param(BigDecimal.class, new BigDecimal(&quot;1000&quot;));
 * builder.sql(&quot;where&quot;);
 * builder.sql(&quot;id = &quot;).param(int.class, 1000);
 * builder.execute();
 * </pre>
 *
 * <h4>実行されるSQL</h4>
 *
 * <pre>
 * update Emp
 * set
 * name = 'SMIHT',
 * salary = 1000
 * where
 * id = 10
 * </pre>
 *
 * @author taedium
 * @since 1.8.0
 */
public class UpdateBuilder {

  private final BuildingHelper helper;

  private final SqlUpdateQuery query;

  private final ParamIndex paramIndex;

  private UpdateBuilder(Config config) {
    this.helper = new BuildingHelper();
    this.query = new SqlUpdateQuery();
    this.query.setConfig(config);
    this.query.setCallerClassName(getClass().getName());
    this.query.setSqlLogType(SqlLogType.FORMATTED);
    this.paramIndex = new ParamIndex();
  }

  private UpdateBuilder(BuildingHelper builder, SqlUpdateQuery query, ParamIndex parameterIndex) {
    this.helper = builder;
    this.query = query;
    this.paramIndex = parameterIndex;
  }

  /**
   * ファクトリメソッドです。
   *
   * @param config 設定
   * @return UPDATE文を組み立てるビルダー
   * @throws DomaNullPointerException 引数が{@code null} の場合
   */
  public static UpdateBuilder newInstance(Config config) {
    if (config == null) {
      throw new DomaNullPointerException("config");
    }
    return new UpdateBuilder(config);
  }

  /**
   * SQLの断片を追加します。
   *
   * @param sql SQLの断片
   * @return このインスタンス
   * @throws DomaNullPointerException 引数が {@code null} の場合
   */
  public UpdateBuilder sql(String sql) {
    if (sql == null) {
      throw new DomaNullPointerException("sql");
    }
    helper.appendSqlWithLineSeparator(sql);
    return new SubsequentUpdateBuilder(helper, query, paramIndex);
  }

  /**
   * 最後に追加したSQLもしくはパラメータを削除します。
   *
   * @return このインスタンス
   */
  public UpdateBuilder removeLast() {
    helper.removeLast();
    return new SubsequentUpdateBuilder(helper, query, paramIndex);
  }

  /**
   * パラメータを追加します。
   *
   * <p>パラメータの型には、基本型とドメインクラスを指定できます。
   *
   * @param <P> パラメータの型
   * @param paramClass パラメータのクラス
   * @param param パラメータ
   * @return このインスタンス
   * @throws DomaNullPointerException {@code parameterClass} が {@code null} の場合
   */
  public <P> UpdateBuilder param(Class<P> paramClass, P param) {
    if (paramClass == null) {
      throw new DomaNullPointerException("paramClass");
    }
    return appendParam(paramClass, param, false);
  }

  /**
   * リテラルとしてパラメータを追加します。
   *
   * <p>パラメータの型には、基本型とドメインクラスを指定できます。
   *
   * @param <P> パラメータの型
   * @param paramClass パラメータのクラス
   * @param param パラメータ
   * @return このインスタンス
   * @throws DomaNullPointerException {@code parameterClass} が {@code null} の場合
   */
  public <P> UpdateBuilder literal(Class<P> paramClass, P param) {
    if (paramClass == null) {
      throw new DomaNullPointerException("paramClass");
    }
    return appendParam(paramClass, param, true);
  }

  private <P> UpdateBuilder appendParam(Class<P> paramClass, P param, boolean literal) {
    helper.appendParam(new Param(paramClass, param, paramIndex, literal));
    paramIndex.increment();
    return new SubsequentUpdateBuilder(helper, query, paramIndex);
  }

  /**
   * SQLを実行します。
   *
   * @return 更新件数
   * @throws UniqueConstraintException 一意制約違反が発生した場合
   * @throws JdbcException 上記以外でJDBCに関する例外が発生した場合
   */
  public int execute() {
    if (query.getMethodName() == null) {
      query.setCallerMethodName("execute");
    }
    prepare();
    UpdateCommand command = new UpdateCommand(query);
    int result = command.execute();
    query.complete();
    return result;
  }

  private void prepare() {
    query.clearParameters();
    for (Param p : helper.getParams()) {
      query.addParameter(p.name, p.paramClass, p.param);
    }
    query.setSqlNode(helper.getSqlNode());
    query.prepare();
  }

  /**
   * クエリタイムアウト（秒）を設定します。
   *
   * <p>指定しない場合、 {@link Config#getQueryTimeout()} が使用されます。
   *
   * @param queryTimeout クエリタイムアウト（秒）
   * @see Statement#setQueryTimeout(int)
   */
  public void queryTimeout(int queryTimeout) {
    query.setQueryTimeout(queryTimeout);
  }

  /**
   * 呼び出し元のクラス名です。
   *
   * <p>指定しない場合このクラスの名前が使用されます。
   *
   * @param className 呼び出し元のクラス名
   * @throws DomaNullPointerException 引数が {@code null} の場合
   */
  public void callerClassName(String className) {
    if (className == null) {
      throw new DomaNullPointerException("className");
    }
    query.setCallerClassName(className);
  }

  /**
   * SQLのログの出力形式を設定します。
   *
   * @param sqlLogType SQLのログの出力形式
   */
  public void sqlLogType(SqlLogType sqlLogType) {
    if (sqlLogType == null) {
      throw new DomaNullPointerException("sqlLogType");
    }
    query.setSqlLogType(sqlLogType);
  }

  /**
   * 呼び出し元のメソッド名です。
   *
   * <p>指定しない場合このSQLを生成するメソッド（{@link #execute()})）の名前が使用されます。
   *
   * @param methodName 呼び出し元のメソッド名
   * @throws DomaNullPointerException 引数が {@code null} の場合
   */
  public void callerMethodName(String methodName) {
    if (methodName == null) {
      throw new DomaNullPointerException("methodName");
    }
    query.setCallerMethodName(methodName);
  }

  /**
   * 組み立てられたSQLを返します。
   *
   * @return 組み立てられたSQL
   */
  public Sql<?> getSql() {
    if (query.getMethodName() == null) {
      query.setCallerMethodName("getSql");
    }
    prepare();
    return query.getSql();
  }

  private static class SubsequentUpdateBuilder extends UpdateBuilder {

    private SubsequentUpdateBuilder(
        BuildingHelper builder, SqlUpdateQuery query, ParamIndex parameterIndex) {
      super(builder, query, parameterIndex);
    }

    @Override
    public UpdateBuilder sql(String sql) {
      if (sql == null) {
        throw new DomaNullPointerException("sql");
      }
      super.helper.appendSql(sql);
      return this;
    }
  }
}

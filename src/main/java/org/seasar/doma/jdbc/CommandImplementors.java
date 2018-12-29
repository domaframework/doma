package org.seasar.doma.jdbc;

import java.lang.reflect.Method;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.seasar.doma.ArrayFactory;
import org.seasar.doma.BatchDelete;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.BatchUpdate;
import org.seasar.doma.BlobFactory;
import org.seasar.doma.ClobFactory;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.NClobFactory;
import org.seasar.doma.Procedure;
import org.seasar.doma.SQLXMLFactory;
import org.seasar.doma.Script;
import org.seasar.doma.Select;
import org.seasar.doma.SqlProcessor;
import org.seasar.doma.Update;
import org.seasar.doma.jdbc.command.BatchDeleteCommand;
import org.seasar.doma.jdbc.command.BatchInsertCommand;
import org.seasar.doma.jdbc.command.BatchUpdateCommand;
import org.seasar.doma.jdbc.command.Command;
import org.seasar.doma.jdbc.command.CreateCommand;
import org.seasar.doma.jdbc.command.DeleteCommand;
import org.seasar.doma.jdbc.command.FunctionCommand;
import org.seasar.doma.jdbc.command.InsertCommand;
import org.seasar.doma.jdbc.command.ProcedureCommand;
import org.seasar.doma.jdbc.command.ResultSetHandler;
import org.seasar.doma.jdbc.command.ScriptCommand;
import org.seasar.doma.jdbc.command.SelectCommand;
import org.seasar.doma.jdbc.command.SqlProcessorCommand;
import org.seasar.doma.jdbc.command.UpdateCommand;
import org.seasar.doma.jdbc.query.BatchDeleteQuery;
import org.seasar.doma.jdbc.query.BatchInsertQuery;
import org.seasar.doma.jdbc.query.BatchUpdateQuery;
import org.seasar.doma.jdbc.query.CreateQuery;
import org.seasar.doma.jdbc.query.DeleteQuery;
import org.seasar.doma.jdbc.query.FunctionQuery;
import org.seasar.doma.jdbc.query.InsertQuery;
import org.seasar.doma.jdbc.query.ProcedureQuery;
import org.seasar.doma.jdbc.query.ScriptQuery;
import org.seasar.doma.jdbc.query.SelectQuery;
import org.seasar.doma.jdbc.query.SqlProcessorQuery;
import org.seasar.doma.jdbc.query.UpdateQuery;

/**
 * {@link Command} の実装クラスのファクトリです。
 *
 * @author nakamura-to
 * @since 2.0.0
 */
public interface CommandImplementors {

  /**
   * {@link Select} に対応したコマンドを作成します。
   *
   * @param <RESULT> 検索結果の型
   * @param method Dao メソッド
   * @param query クエリ
   * @param resultSetHandler 結果セットのハンドラ
   * @return コマンド
   */
  default <RESULT> SelectCommand<RESULT> createSelectCommand(
      Method method, SelectQuery query, ResultSetHandler<RESULT> resultSetHandler) {
    return new SelectCommand<>(query, resultSetHandler);
  }

  /**
   * {@link Delete} に対応したコマンドを作成します。
   *
   * @param method Dao メソッド
   * @param query クエリ
   * @return コマンド
   */
  default DeleteCommand createDeleteCommand(Method method, DeleteQuery query) {
    return new DeleteCommand(query);
  }

  /**
   * {@link Insert} に対応したコマンドを作成します。
   *
   * @param method Dao メソッド
   * @param query クエリ
   * @return コマンド
   */
  default InsertCommand createInsertCommand(Method method, InsertQuery query) {
    return new InsertCommand(query);
  }

  /**
   * {@link Update} に対応したコマンドを作成します。
   *
   * @param method Dao メソッド
   * @param query クエリ
   * @return コマンド
   */
  default UpdateCommand createUpdateCommand(Method method, UpdateQuery query) {
    return new UpdateCommand(query);
  }

  /**
   * {@link BatchDelete} に対応したコマンドを作成します。
   *
   * @param method Dao メソッド
   * @param query クエリ
   * @return コマンド
   */
  default BatchDeleteCommand createBatchDeleteCommand(Method method, BatchDeleteQuery query) {
    return new BatchDeleteCommand(query);
  }

  /**
   * {@link BatchInsert} に対応したコマンドを作成します。
   *
   * @param method Dao メソッド
   * @param query クエリ
   * @return コマンド
   */
  default BatchInsertCommand createBatchInsertCommand(Method method, BatchInsertQuery query) {
    return new BatchInsertCommand(query);
  }

  /**
   * {@link BatchUpdate} に対応したコマンドを作成します。
   *
   * @param method Dao メソッド
   * @param query クエリ
   * @return コマンド
   */
  default BatchUpdateCommand createBatchUpdateCommand(Method method, BatchUpdateQuery query) {
    return new BatchUpdateCommand(query);
  }

  /**
   * {@link Function} に対応したコマンドを作成します。
   *
   * @param <RESULT> 戻り値
   * @param method Dao メソッド
   * @param query クエリ
   * @return コマンド
   */
  default <RESULT> FunctionCommand<RESULT> createFunctionCommand(
      Method method, FunctionQuery<RESULT> query) {
    return new FunctionCommand<>(query);
  }

  /**
   * {@link Procedure} に対応したコマンドを作成します。
   *
   * @param method Dao メソッド
   * @param query クエリ
   * @return コマンド
   */
  default ProcedureCommand createProcedureCommand(Method method, ProcedureQuery query) {
    return new ProcedureCommand(query);
  }

  /**
   * {@link ArrayFactory}、{@link BlobFactory}、{@link ClobFactory}、 {@link NClobFactory}、{@link
   * SQLXMLFactory} に対応したコマンドを作成します。
   *
   * @param <RESULT> 戻り値
   * @param method Dao メソッド
   * @param query クエリ
   * @return コマンド
   */
  default <RESULT> CreateCommand<RESULT> createCreateCommand(
      Method method, CreateQuery<RESULT> query) {
    return new CreateCommand<>(query);
  }

  /**
   * {@link Script} に対応したコマンドを作成します。
   *
   * @param method Dao メソッド
   * @param query クエリ
   * @return コマンド
   */
  default ScriptCommand createScriptCommand(Method method, ScriptQuery query) {
    return new ScriptCommand(query);
  }

  /**
   * {@link SqlProcessor} に対応したコマンドを作成します。
   *
   * @param <RESULT> ハンドラで処理された結果
   * @param method Dao メソッド
   * @param query クエリ
   * @param handler SQLのハンドラ
   * @return コマンド
   * @since 2.14.0
   */
  default <RESULT> SqlProcessorCommand<RESULT> createSqlProcessorCommand(
      Method method, SqlProcessorQuery query, BiFunction<Config, PreparedSql, RESULT> handler) {
    return new SqlProcessorCommand<>(query, handler);
  }
}

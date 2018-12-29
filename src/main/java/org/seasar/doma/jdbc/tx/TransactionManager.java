package org.seasar.doma.jdbc.tx;

import java.util.function.Supplier;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.JdbcException;

/**
 * トランザクションのマネージャーです。
 *
 * <p>トランザクションを使った操作を簡易化するAPIを提供します。
 *
 * <p>このインタフェースの実装クラスはスレッドセーフでなければいけません。
 *
 * @author nakamura-to
 * @since 2.0.0
 */
public interface TransactionManager {

  /**
   * トランザクション属性がREQUIREDであるトランザクションを実行します。
   *
   * @param block トランザクション内で実行する処理
   */
  public abstract void required(Runnable block);

  /**
   * トランザクション属性がREQUIREDであるトランザクションを実行します。
   *
   * @param isolationLevel トランザクション分離レベル
   * @param block トランザクション内で実行する処理
   */
  public abstract void required(TransactionIsolationLevel isolationLevel, Runnable block);

  /**
   * トランザクション属性がREQUIREDであるトランザクションを実行します。
   *
   * @param supplier トランザクション内で実行する処理
   * @param <RESULT> 結果の型
   * @return 処理の結果
   */
  public abstract <RESULT> RESULT required(Supplier<RESULT> supplier);

  /**
   * トランザクション属性がREQUIREDであるトランザクションを実行します。
   *
   * @param isolationLevel トランザクション分離レベル
   * @param supplier トランザクション内で実行する処理
   * @param <RESULT> 結果の型
   * @return 処理の結果
   */
  public abstract <RESULT> RESULT required(
      TransactionIsolationLevel isolationLevel, Supplier<RESULT> supplier);

  /**
   * トランザクション属性がREQUIRES_NEWであるトランザクションを実行します。
   *
   * @param block トランザクション内で実行する処理
   */
  public abstract void requiresNew(Runnable block);

  /**
   * トランザクション属性がREQUIRES_NEWであるトランザクションを実行します。
   *
   * @param isolationLevel トランザクション分離レベル
   * @param block トランザクション内で実行する処理
   */
  public abstract void requiresNew(TransactionIsolationLevel isolationLevel, Runnable block);

  /**
   * トランザクション属性がREQUIRES_NEWであるトランザクションを実行します。
   *
   * @param supplier トランザクション内で実行する処理
   * @param <RESULT> 結果の型
   * @return 処理の結果
   */
  public abstract <RESULT> RESULT requiresNew(Supplier<RESULT> supplier);

  /**
   * トランザクション属性がREQUIRES_NEWであるトランザクションを実行します。
   *
   * @param isolationLevel トランザクション分離レベル
   * @param supplier トランザクション内で実行する処理
   * @param <RESULT> 結果の型
   * @return 処理の結果
   */
  public abstract <RESULT> RESULT requiresNew(
      TransactionIsolationLevel isolationLevel, Supplier<RESULT> supplier);

  /**
   * トランザクション属性がNOT_SUPPORTEDであるトランザクションを実行します。
   *
   * @param block トランザクション内で実行する処理
   */
  public abstract void notSupported(Runnable block);

  /**
   * トランザクション属性がNOT_SUPPORTEDであるトランザクションを実行します。
   *
   * @param isolationLevel トランザクション分離レベル
   * @param block トランザクション内で実行する処理
   */
  public abstract void notSupported(TransactionIsolationLevel isolationLevel, Runnable block);

  /**
   * トランザクション属性がNOT_SUPPORTEDであるトランザクションを実行します。
   *
   * @param supplier トランザクション内で実行する処理
   * @param <RESULT> 結果の型
   * @return 処理の結果
   */
  public abstract <RESULT> RESULT notSupported(Supplier<RESULT> supplier);

  /**
   * トランザクション属性がNOT_SUPPORTEDであるトランザクションを実行します。
   *
   * @param isolationLevel トランザクション分離レベル
   * @param supplier トランザクション内で実行する処理
   * @param <RESULT> 結果の型
   * @return 処理の結果
   */
  public abstract <RESULT> RESULT notSupported(
      TransactionIsolationLevel isolationLevel, Supplier<RESULT> supplier);

  /** 現在のトランザクションをロールバックすることを予約します。 */
  public abstract void setRollbackOnly();

  /**
   * 現在のトランザクションがロールバックされるように予約されているかどうかを返します。
   *
   * @return ロールバックされる場合 {@code true}
   */
  public abstract boolean isRollbackOnly();

  /**
   * トランザクションのセーブポイントを作成します。
   *
   * <p>このメソッドを呼び出す前にトランザクションを開始しておく必要があります。
   *
   * @param savepointName セーブポイントの名前
   * @throws DomaNullPointerException 引数が {@code null} の場合
   * @throws TransactionNotYetBegunException トランザクションがまだ開始されていない場合
   * @throws SavepointAlreadyExistsException セーブポイントがすでに存在する場合
   * @throws JdbcException セーブポイントの作成に失敗した場合
   */
  public abstract void setSavepoint(String savepointName);

  /**
   * このトランザクションでセーブポイントを保持しているかどうかを返します。
   *
   * <p>このメソッドを呼び出す前にトランザクションを開始しておく必要があります。
   *
   * @param savepointName セーブポイントの名前
   * @throws DomaNullPointerException 引数が {@code null} の場合
   * @throws TransactionNotYetBegunException トランザクションがまだ開始されていない場合
   * @return セーブポイントを保持している場合 {@code true}
   */
  public abstract boolean hasSavepoint(String savepointName);

  /**
   * トランザクションから指定されたセーブポイントと以降のセーブポイントを削除します。
   *
   * <p>このメソッドを呼び出す前にトランザクションを開始しておく必要があります。
   *
   * @param savepointName セーブポイントの名前
   * @throws DomaNullPointerException 引数が {@code null} の場合
   * @throws TransactionNotYetBegunException トランザクションがまだ開始されていない場合
   * @throws JdbcException セーブポイントの削除に失敗した場合
   */
  public abstract void releaseSavepoint(String savepointName);

  /**
   * 指定されたセーブポイントが設定されたあとに行われたすべての変更をロールバックします。
   *
   * <p>このメソッドを呼び出す前にトランザクションを開始しておく必要があります。
   *
   * @param savepointName セーブポイントの名前
   * @throws DomaNullPointerException 引数が {@code null} の場合
   * @throws SavepointNotFoundException セーブポイントが見つからない場合
   * @throws TransactionNotYetBegunException トランザクションがまだ開始されていない場合
   * @throws JdbcException セーブポイントへのロールバックに失敗した場合
   */
  public abstract void rollback(String savepointName);
}

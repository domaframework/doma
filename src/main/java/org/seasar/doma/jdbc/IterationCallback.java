package org.seasar.doma.jdbc;

import java.sql.ResultSet;

/**
 * {@link ResultSet} にマッピングされるオブジェクト群を1件ずつ処理するコールバックです。
 *
 * <p>{@link #iterate(Object, IterationContext)} は、 {@code ResultSet}
 * のループ中にオブジェクトがインスタンス化された直後に呼び出されます。
 *
 * <p>このインタフェースの実装はスレッドセーフであることを要求されません。
 *
 * @author taedium
 * @param <TARGET> 処理対象の型。すなわち、基本型、ドメインクラス、エンティティクラス、もしくは {@code Map<String, Object>}
 * @param <RESULT> 戻り値の型
 */
public interface IterationCallback<TARGET, RESULT> {

  /**
   * デフォルトの戻り値を返します。
   *
   * @return デフォルトの戻り値
   * @since 2.0.0
   */
  default RESULT defaultResult() {
    return null;
  }

  /**
   * 処理対象のオブジェクト群を順に1件ずつ処理します。
   *
   * <p>全件を処理する前に処理を中断するには、 {@link IterationContext#exit()}を呼び出します。
   *
   * @param target {@link ResultSet} の1行にマッピングされたオブジェクト
   * @param context 実行コンテキスト
   * @return 任意の実行結果
   */
  RESULT iterate(TARGET target, IterationContext context);

  /**
   * {@code iterate} の実行後に呼び出され、任意の処理を実行します。
   *
   * @param result {@code iterate} の実行結果
   * @param context 実行コンテキスト
   * @return 実行結果
   * @since 2.0.0
   */
  default RESULT postIterate(RESULT result, IterationContext context) {
    return result;
  }
}

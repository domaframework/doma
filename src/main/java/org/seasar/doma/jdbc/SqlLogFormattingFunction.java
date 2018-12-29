package org.seasar.doma.jdbc;

import org.seasar.doma.wrapper.Wrapper;

/**
 * SQLのバインド変数の値をSQLのログ用文字列に変換する処理を表します。
 *
 * <p>このインタフェースの実装はスレッドセーフであることを要求されません。
 *
 * @author taedium
 */
public interface SqlLogFormattingFunction {

  /**
   * この処理を適用します。
   *
   * @param <V> 値の型
   * @param wrapper SQLのバインド変数にマッピングされるラッパー
   * @param formatter ログ用のフォーマッタ
   * @return フォーマットされた文字列
   */
  <V> String apply(Wrapper<V> wrapper, SqlLogFormatter<V> formatter);
}

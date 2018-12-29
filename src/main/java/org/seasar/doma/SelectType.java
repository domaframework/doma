package org.seasar.doma;

import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * 検索結果を扱う戦略です。
 *
 * @author nakamura-to
 * @since 2.0.0
 */
public enum SelectType {

  /** 結果を戻り値で取得します。 */
  RETURN,

  /** {@link Stream} を使って処理します。 */
  STREAM,

  /** {@link Collector} を使って処理します。 */
  COLLECT;
}

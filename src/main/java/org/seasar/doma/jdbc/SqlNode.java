package org.seasar.doma.jdbc;

import java.util.List;
import org.seasar.doma.DomaNullPointerException;

/**
 * SQLの文字列の解析結果です。
 *
 * <p>このインタフェースの実装はスレッドセーフであることは要求されません。
 *
 * <p>このインスタンスのライフサイクルを制御できない場合は参照専用として扱わなければいけません。
 *
 * @author taedium
 */
public interface SqlNode {

  /**
   * 子ノードのリストを返します。
   *
   * @return 子ノードのリスト
   */
  List<SqlNode> getChildren();

  /**
   * ビジターを受け入れ、ビジターの適切なメソッドにディスパッチします。
   *
   * @param <R> 戻り値の型
   * @param <P> パラメータの型
   * @param visitor ビジター
   * @param p パラメータ
   * @return ビジターで処理された値
   * @throws DomaNullPointerException ビジターが {@code null} の場合
   */
  <R, P> R accept(SqlNodeVisitor<R, P> visitor, P p);
}

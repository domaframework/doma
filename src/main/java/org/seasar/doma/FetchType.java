package org.seasar.doma;

/**
 * 検索の結果セットを即時に全て取得するか、遅延で少しづつ取得するかを示します。
 *
 * @author nakamura-to
 * @since 2.0.0
 */
public enum FetchType {

  /**
   * 即時に全て取得します。
   *
   * <p>メモリの使用量が増え、DBへの接続時間は短くなります。
   */
  EAGER,

  /**
   * 遅延で少しづつ必要なだけを取得します。
   *
   * <p>メモリの使用量は減り、DBへの接続時間は長くなります。
   */
  LAZY;
}

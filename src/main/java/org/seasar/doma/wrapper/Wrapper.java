package org.seasar.doma.wrapper;

import org.seasar.doma.DomaNullPointerException;

/**
 * 基本型の値のラッパーです。
 *
 * <p>このインタフェースの実装はスレッドセーフであることを要求されません。
 *
 * @author taedium
 * @param <BASIC> 基本型
 */
public interface Wrapper<BASIC> {

  /**
   * 値を返します。
   *
   * @return 値、{@code null} でありうる
   */
  BASIC get();

  /**
   * 値を設定します。
   *
   * @param value 値
   */
  void set(BASIC value);

  /**
   * 値のコピーを返します。
   *
   * @return 値のコピー
   */
  BASIC getCopy();

  /**
   * 値の型がプリミティブ型のボックス型であればプリミティブ型のデフォルト値をボックスした値を返します。
   *
   * @return 値の型がプリミティブ型のボックス型のであればプリミティブ型のデフォルト値をボックスした値、値の型がプリミティブ型のボックス型でない場合 {@code null}
   * @since 1.5.0
   */
  BASIC getDefault();

  /**
   * 等しい値を持っている場合 {@code true} を返します。
   *
   * @param other 値
   * @return 等しい値を持っている場合 {@code true}
   */
  boolean hasEqualValue(Object other);

  /**
   * 基本型のクラスを返します。
   *
   * @return 基本型のクラス
   * @since 2.0.0
   */
  Class<BASIC> getBasicClass();

  /**
   * ビジターを受け入れます。
   *
   * @param <R> 戻り値の型
   * @param <P> 1番目のパラメータの型
   * @param <Q> 2番目のパラメータの型
   * @param <TH> 例外の型
   * @param visitor ビジター
   * @param p 1番目のパラメータ
   * @param q 2番目のパラメータ
   * @return 戻り値
   * @throws TH 例外
   * @throws DomaNullPointerException ビジターが {@code null} の場合
   */
  <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q) throws TH;
}

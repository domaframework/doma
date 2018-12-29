package org.seasar.doma.wrapper;

import org.seasar.doma.DomaNullPointerException;

/**
 * {@link Enum} のラッパーです。
 *
 * @author taedium
 * @param <E> {@link Enum} の型
 */
public class EnumWrapper<E extends Enum<E>> extends AbstractWrapper<E> {

  /**
   * インスタンスを構築します。
   *
   * @param enumClass {@link Enum} のクラス
   * @throws DomaNullPointerException {@link Enum} のクラスが {@code null} の場合
   */
  public EnumWrapper(Class<E> enumClass) {
    this(enumClass, null);
  }

  /**
   * 値を指定してインスタンスを構築します。
   *
   * @param enumClass {@link Enum} のクラス
   * @param value 値
   * @throws DomaNullPointerException {@link Enum} のクラスが {@code null} の場合
   */
  public EnumWrapper(Class<E> enumClass, E value) {
    super(enumClass, value);
    if (enumClass == null) {
      throw new DomaNullPointerException("enumClass");
    }
  }

  @Override
  public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
      throws TH {
    if (visitor == null) {
      throw new DomaNullPointerException("visitor");
    }
    return visitor.visitEnumWrapper(this, p, q);
  }
}

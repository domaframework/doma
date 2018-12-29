package org.seasar.doma.wrapper;

/**
 * {@link Number} のラッパーです。
 *
 * @author taedium
 * @param <BASIC> 値の型
 */
public interface NumberWrapper<BASIC extends Number> extends Wrapper<BASIC> {

  @Override
  void set(Number value);

  /** 値をインクリメントします。 */
  void increment();

  /** 値をデクリメントします。 */
  void decrement();
}

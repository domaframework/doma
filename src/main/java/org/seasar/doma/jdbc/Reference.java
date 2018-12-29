package org.seasar.doma.jdbc;

/**
 * 値への参照を表します。
 *
 * <p>ストアドプロシージャーやストアドファンクションのOUTパラメーターとIN_OUTパラメーターとして使用されます。
 *
 * @author taedium
 * @param <V> 値の型
 */
public class Reference<V> {

  private V value;

  public Reference() {}

  public Reference(V value) {
    this.value = value;
  }

  public V get() {
    return value;
  }

  public void set(V value) {
    this.value = value;
  }
}

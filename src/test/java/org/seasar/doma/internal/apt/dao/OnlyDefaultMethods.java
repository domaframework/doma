package org.seasar.doma.internal.apt.dao;

public interface OnlyDefaultMethods<T> {

  default void aaa() {}

  default T bbb(T value) {
    return value;
  }
}

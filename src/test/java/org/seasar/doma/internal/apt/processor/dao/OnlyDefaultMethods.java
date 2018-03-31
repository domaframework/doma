package org.seasar.doma.internal.apt.processor.dao;

public interface OnlyDefaultMethods<T> {

  default void aaa() {}

  default T bbb(T value) {
    return value;
  }
}

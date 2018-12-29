package org.seasar.doma.internal.apt.dao;

/** @author nakamura */
public interface OnlyDefaultMethods<T> {

  default void aaa() {}

  default T bbb(T value) {
    return value;
  }
}

package org.seasar.doma.internal.apt.processor.dao;

/** @author nakamura */
public interface OnlyDefaultMethods<T> {

  default void aaa() {}

  default T bbb(T value) {
    return value;
  }
}

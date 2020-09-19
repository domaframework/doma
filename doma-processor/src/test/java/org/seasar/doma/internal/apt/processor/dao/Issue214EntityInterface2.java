package org.seasar.doma.internal.apt.processor.dao;

public interface Issue214EntityInterface2 extends Issue214EntityInterface {

  String STATIC_FIELD = "";

  static String staticMethod() {
    return null;
  }

  @Override
  String instanceMethod();
}

package org.seasar.doma.jdbc.builder;

/** @author taedium */
class Param {

  final String name;

  final Class<?> paramClass;

  final Object param;

  final boolean literal;

  Param(Class<?> paramClass, Object param, ParamIndex index, boolean literal) {
    this.paramClass = paramClass;
    this.param = param;
    this.name = "p" + index.getValue();
    this.literal = literal;
  }
}

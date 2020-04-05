package org.seasar.doma.internal.apt.validator;

import org.seasar.doma.jdbc.dialect.H2Dialect.H2ExpressionFunctions;

public class MyExpressionFunctions extends H2ExpressionFunctions {

  public String hello(String name) {
    return name;
  }
}

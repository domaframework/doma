package org.seasar.doma.internal.jdbc.mock;

class AssertionUtil {

  public static void assertTrue(boolean evalResult) {
    if (!evalResult) {
      throw new AssertionError();
    }
  }
}

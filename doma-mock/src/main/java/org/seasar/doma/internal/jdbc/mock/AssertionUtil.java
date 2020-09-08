package org.seasar.doma.internal.jdbc.mock;

class AssertionUtil {

  public static void assertTrue(boolean evalResult, Object... messages) {
    if (!evalResult) {
      StringBuilder buf = new StringBuilder();
      for (Object message : messages) {
        buf.append(message.toString());
        buf.append(", ");
      }
      if (buf.length() > 0) {
        buf.setLength(buf.length() - 2);
      }
      throw new AssertionError("Unfulfilled. " + buf.toString());
    }
  }

  public static void notYetImplemented() {
    throw new AssertionError("Not yet implemented.");
  }
}

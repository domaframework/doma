package org.seasar.doma.internal.util;

import java.util.Map;

public final class AssertionUtil {

  public static void assertNull(Object o) {
    if (o != null) {
      throw new AssertionError("Not null.");
    }
  }

  public static void assertNotNull(Object o) {
    if (o == null) {
      throw new AssertionError("Null.");
    }
  }

  public static void assertNotNull(Object o, Object o1) {
    if (o == null) {
      throw new AssertionError("Null. index = 0");
    }
    if (o1 == null) {
      throw new AssertionError("Null. index = 1");
    }
  }

  public static void assertNotNull(Object o, Object o1, Object o2) {
    if (o == null) {
      throw new AssertionError("Null. index = 0");
    }
    if (o1 == null) {
      throw new AssertionError("Null. index = 1");
    }
    if (o2 == null) {
      throw new AssertionError("Null. index = 2");
    }
  }

  public static void assertNotNull(Object o, Object o1, Object o2, Object o3) {
    if (o == null) {
      throw new AssertionError("Null. index = 0");
    }
    if (o1 == null) {
      throw new AssertionError("Null. index = 1");
    }
    if (o2 == null) {
      throw new AssertionError("Null. index = 2");
    }
    if (o3 == null) {
      throw new AssertionError("Null. index = 3");
    }
  }

  public static void assertNotNull(Object o, Object o1, Object o2, Object o3, Object o4) {
    if (o == null) {
      throw new AssertionError("Null. index = 0");
    }
    if (o1 == null) {
      throw new AssertionError("Null. index = 1");
    }
    if (o2 == null) {
      throw new AssertionError("Null. index = 2");
    }
    if (o3 == null) {
      throw new AssertionError("Null. index = 3");
    }
    if (o4 == null) {
      throw new AssertionError("Null. index = 4");
    }
  }

  public static void assertNotNull(Object o, Object... objects) {
    if (o == null) {
      throw new AssertionError("Null. index = 0");
    }
    for (var i = 0; i < objects.length; i++) {
      var object = objects[i];
      if (object == null) {
        throw new AssertionError("Null. index = " + (i + 1));
      }
    }
  }

  public static <K, V> V assertNotNullValue(Map<K, V> map, K key) {
    if (map == null) {
      throw new AssertionError("The map is null");
    }
    var value = map.get(key);
    if (value == null) {
      throw new AssertionError("The value is null. key = [" + key + "].");
    }
    return value;
  }

  public static void assertTrue(boolean evalResult, Object... messages) {
    if (!evalResult) {
      var buf = new StringBuilder();
      for (var message : messages) {
        buf.append(message.toString());
        buf.append(", ");
      }
      if (buf.length() > 0) {
        buf.setLength(buf.length() - 2);
      }
      throw new AssertionError("Unfulfilled. " + buf.toString());
    }
  }

  public static <T> void assertEquals(T expected, T actual) {
    if (expected == null) {
      if (actual == null) {
        return;
      } else {
        throw new AssertionError("Not equals. expected is null, but actual is [" + actual + "]");
      }
    }
    if (!expected.equals(actual)) {
      throw new AssertionError(
          "Not equals. expected is [" + expected + "], but actual is [" + actual + "]");
    }
  }

  public static <T> T assertUnreachable() {
    throw new AssertionError("Unreachable.");
  }

  public static <T> void assertUnreachable(String message) {
    throw new AssertionError("Unreachable. " + message);
  }

  public static <T> T assertUnreachable(Throwable throwable) {
    throw new AssertionError(throwable);
  }

  public static void notYetImplemented() {
    throw new AssertionError("Not yet implemented.");
  }
}

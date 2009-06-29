package org.seasar.doma.internal.util;

public final class Assertions {

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

    public static void assertNotNull(Object o, Object o1, Object o2, Object o3,
            Object o4) {
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
        for (int i = 0; i < objects.length; i++) {
            Object object = objects[i];
            if (object == null) {
                throw new AssertionError("Null. index = " + i + 1);
            }
        }
    }

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

    public static <T> void assertEquals(T expected, T actual) {
        if (expected == null) {
            if (actual != null) {
                throw new AssertionError(
                        "Not equals. expected is null, but actual is ["
                                + actual + "]");
            }
        }
        if (!expected.equals(actual)) {
            throw new AssertionError("Not equals. expected is [" + expected
                    + "], but actual is [" + actual + "]");
        }
    }

    public static <T> T assertUnreachable() {
        throw new AssertionError("Unreachable.");
    }

    public static void notYetImplemented() {
        throw new AssertionError("Not yet implemented.");
    }

}

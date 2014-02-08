/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.util;

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
                throw new AssertionError("Null. index = " + (i + 1));
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

    public static <T> T assertUnreachable(String message) {
        throw new AssertionError("Unreachable. " + message);
    }

    public static <T> T assertUnreachable(Throwable throwable) {
        throw new AssertionError(throwable);
    }

    public static void notYetImplemented() {
        throw new AssertionError("Not yet implemented.");
    }

}

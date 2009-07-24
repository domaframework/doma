/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.doma.util;

import java.nio.CharBuffer;

/**
 * @author taedium
 * 
 */
public final class StringUtil {

    public static String capitalize(String text) {
        if (isNullOrEmpty(text)) {
            return text;
        }
        char chars[] = text.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    public static String decapitalize(String text) {
        if (isNullOrEmpty(text)) {
            return text;
        }
        char chars[] = text.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    public static String camelize(String text) {
        if (isNullOrEmpty(text)) {
            return text;
        }
        String[] array = text.split("_");
        if (array.length == 0) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        result.append(array[0].toLowerCase());
        for (int i = 1; i < array.length; i++) {
            String s = capitalize(array[i].toLowerCase());
            result.append(s);
        }
        return result.toString();
    }

    public static String decamelize(String text) {
        if (isNullOrEmpty(text)) {
            return text;
        }
        StringBuilder result = new StringBuilder();
        CharBuffer buf = CharBuffer.wrap(text);
        while (buf.hasRemaining()) {
            char c = buf.get();
            result.append(Character.toUpperCase(c));
            buf.mark();
            if (buf.hasRemaining()) {
                char c2 = buf.get();
                if (Character.isLowerCase(c) && Character.isUpperCase(c2)) {
                    result.append("_");
                }
                buf.reset();
            }
        }
        return result.toString();
    }

    public static boolean isNullOrEmpty(String text) {
        return text == null || text.isEmpty();
    }
}

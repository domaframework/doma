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

import java.nio.CharBuffer;

/**
 * {@link String} のユーティリティクラスです。
 * 
 * @author taedium
 * 
 */
public final class StringUtil {

    /**
     * 先頭の文字を大文字に変換します。
     * 
     * @param text
     *            文字列
     * @return 変換された文字列。 ただし、{@code text} が {@code null} の場合は {@code null}、
     *         {@code text} が空文字の場合は空文字を返します。
     */
    public static String capitalize(String text) {
        if (isNullOrEmpty(text)) {
            return text;
        }
        char chars[] = text.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    /**
     * 先頭の文字を小文字に変換します。
     * 
     * @param text
     *            文字列
     * @return 変換された文字列。 ただし、{@code text} が {@code null} の場合は {@code null}、
     *         {@code text} が空文字の場合は空文字を返します。
     */
    public static String decapitalize(String text) {
        if (isNullOrEmpty(text)) {
            return text;
        }
        char chars[] = text.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    /**
     * アンダースコア区切りの文字列をキャメルケースの文字列に変換します。
     * 
     * @param text
     *            文字列
     * @return 変換された文字列。 ただし、{@code text} が {@code null} の場合は {@code null}、
     *         {@code text} が空文字の場合は空文字を返します。
     */
    public static String fromSnakeCaseToCamelCase(String text) {
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

    /**
     * キャメルケースをアンダースコア区切りの大文字に変換します。
     * 
     * @param text
     *            文字列
     * @return 変換された文字列。 ただし、{@code text} が {@code null} の場合は {@code null}、
     *         {@code text} が空文字の場合は空文字を返します。
     */
    public static String fromCamelCaseToSnakeCase(String text) {
        if (isNullOrEmpty(text)) {
            return text;
        }
        StringBuilder result = new StringBuilder();
        CharBuffer buf = CharBuffer.wrap(text);
        while (buf.hasRemaining()) {
            char c = buf.get();
            result.append(Character.toLowerCase(c));
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

    /**
     * 文字列が空白文字だけからなるかどうかを返します。
     * 
     * @param text
     *            文字列
     * @return 文字列が空白文字のみを含む場合 {@code true}
     */
    public static boolean isWhitespace(String text) {
        if (isNullOrEmpty(text)) {
            return false;
        }
        for (char ch : text.toCharArray()) {
            if (!Character.isWhitespace(ch)) {
                return false;
            }
        }
        return true;
    }

    public static String trimWhitespace(String text) {
        if (isNullOrEmpty(text)) {
            return text;
        }
        char[] chars = text.toCharArray();
        int start = 0;
        int end = chars.length;

        while ((start < end) && (Character.isWhitespace(chars[start]))) {
            start++;
        }
        while ((start < end) && (Character.isWhitespace(chars[end - 1]))) {
            end--;
        }
        if (start < end) {
            return ((start > 0) || (end < chars.length)) ? new String(chars,
                    start, end - start) : text;
        }
        return "";
    }

    private static boolean isNullOrEmpty(String text) {
        return text == null || text.isEmpty();
    }

}

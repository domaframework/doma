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
package org.seasar.aptina.commons.util;

import java.util.Collection;

/**
 * 表明を扱うユーティリティです．
 * 
 * @author koichik
 */
public class AssertionUtils {

    private AssertionUtils() {
    }

    /**
     * パラメータが {@code null} ではいけないことを表明します．
     * 
     * @param name
     *            パラメータの名前
     * @param param
     *            パラメータ
     * @throws AssertionError
     *             パラメータが {@code null} の場合
     */
    public static void assertNotNull(final String name, final Object param)
            throws AssertionError {
        if (param == null) {
            throw new AssertionError(name + "must not be null");
        }
    }

    /**
     * 文字列が {@code null} でも空でもいけないことを表明します．
     * 
     * @param name
     *            配列の名前
     * @param string
     *            文字列
     * @throws AssertionError
     *             文字列が {@code null} または場合
     */
    public static void assertNotEmpty(final String name,
            final CharSequence string) throws AssertionError {
        if (string == null) {
            throw new AssertionError(name + "must not be null");
        }
        if (string.length() == 0) {
            throw new AssertionError(name + " must not be empty");
        }
    }

    /**
     * 配列が {@code null} でも空でもいけないことを表明します．
     * 
     * @param name
     *            配列の名前
     * @param array
     *            配列
     * @throws AssertionError
     *             配列が {@code null} または空の場合
     */
    public static void assertNotEmpty(final String name, final Object[] array)
            throws AssertionError {
        if (array == null) {
            throw new AssertionError(name + " must not be null");
        }
        if (array.length == 0) {
            throw new AssertionError(name + " must not be empty");
        }
        for (int i = 0; i < array.length; ++i) {
            if (array[i] == null) {
                throw new AssertionError(name + "[" + i + "] must not be null");
            }
        }
    }

    /**
     * コレクションが {@code null} でも空でもいけないことを表明します．
     * 
     * @param name
     *            配列の名前
     * @param collection
     *            コレクション
     * @throws AssertionError
     *             配列が {@code null} または空の場合
     */
    public static void assertNotEmpty(final String name,
            final Collection<?> collection) throws AssertionError {
        if (collection == null) {
            throw new AssertionError(name + " must not be null");
        }
        if (collection.isEmpty()) {
            throw new AssertionError(name + " must not be empty");
        }
        int i = 0;
        for (final Object element : collection) {
            if (element == null) {
                throw new AssertionError(name + "[" + i + "] must not be null");
            }
            ++i;
        }
    }

    /**
     * 期待する値と実際の値が等しくなければならないことを表明します．
     * 
     * @param expected
     *            期待する値
     * @param actual
     *            実際の値
     * @throws AssertionError
     *             期待する値が実際の値と異なった場合
     */
    public static void assertEquals(final int expected, final int actual)
            throws AssertionError {
        if (expected != actual) {
            throw new AssertionError("expected <" + expected + ">, but was <"
                    + actual + ">");
        }
    }

    /**
     * 期待する値と実際の値が等しくなければならないことを表明します．
     * 
     * @param expected
     *            期待する値
     * @param actual
     *            実際の値
     * @throws AssertionError
     *             期待する値が実際の値と異なった場合
     */
    public static void assertEquals(final long expected, final long actual)
            throws AssertionError {
        if (expected != actual) {
            throw new AssertionError("expected <" + expected + ">, but was <"
                    + actual + ">");
        }
    }

}

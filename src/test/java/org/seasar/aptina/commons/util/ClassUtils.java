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

import static org.seasar.aptina.commons.util.AssertionUtils.*;
import static org.seasar.aptina.commons.util.StringUtils.*;

/**
 * クラスを扱うユーティリティです．
 * 
 * @author koichik
 */
public class ClassUtils {

    private ClassUtils() {
    }

    /**
     * パッケージ名と単純名から完全限定名を返します．
     * 
     * @param packageName
     *            パッケージ名
     * @param simpleName
     *            単純名
     * @return 完全限定名
     */
    public static String getQualifiedName(final String packageName,
            final String simpleName) {
        if (isEmpty(packageName)) {
            return simpleName;
        }
        return packageName + "." + simpleName;
    }

    /**
     * クラスの完全限定名からパッケージ名を返します．
     * <p>
     * 完全限定名がパッケージ名を含んでいない場合 (デフォルトパッケージ) は {@code null} を返します．
     * </p>
     * 
     * @param qualifiedName
     *            完全限定名
     * @return パッケージ名または {@code null}
     */
    public static String getPackageName(final String qualifiedName) {
        final int pos = qualifiedName.lastIndexOf('.');
        if (pos == -1) {
            return null;
        }
        return qualifiedName.substring(0, pos);
    }

    /**
     * クラスの完全限定名を返します．
     * <p>
     * 配列型はバイナリ名 ({@code [[;intL} 形式) ではなく正規名 ({@code int[]} 形式) となります．
     * </p>
     * 
     * @param clazz
     *            クラス
     * @return クラスの完全限定名または {@code null}
     */
    public static String getQualifiedName(final Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        if (clazz.isArray()) {
            return getQualifiedName(clazz.getComponentType()) + "[]";
        }
        return clazz.getName();
    }

    /**
     * クラスの完全限定名の配列を返します．
     * <p>
     * 配列型はバイナリ名 ({@code [[;intL} 形式) ではなく正規名 ({@code int[]} 形式) となります．
     * </p>
     * 
     * @param classes
     *            クラスの配列
     * @return 完全限定名の配列
     */
    public static String[] getQualifiedNameArray(final Class<?>... classes) {
        assertNotNull("classes", classes);
        final String[] qualifiedNames = new String[classes.length];
        for (int i = 0; i < classes.length; ++i) {
            qualifiedNames[i] = getQualifiedName(classes[i]);
        }
        return qualifiedNames;
    }

    /**
     * クラスの単純名を返します．
     * <p>
     * 配列型はバイナリ名 ({@code [[;intL} 形式) ではなく正規名 ({@code int[]} 形式) となります．
     * </p>
     * 
     * @param clazz
     *            クラス
     * @return クラスの単純名または {@code null}
     */
    public static String getSimpleName(final Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        if (clazz.isArray()) {
            return getSimpleName(clazz.getComponentType()) + "[]";
        }
        return clazz.getSimpleName();
    }

    /**
     * クラスの単純名の配列を返します．
     * <p>
     * 配列型はバイナリ名 ({@code [[;intL} 形式) ではなく正規名 ({@code int[]} 形式) となります．
     * </p>
     * 
     * @param classes
     *            クラスの配列
     * @return 単純名の配列
     */
    public static String[] getSimpleNameArray(final Class<?>... classes) {
        assertNotNull("clsses", classes);
        final String[] simpleNames = new String[classes.length];
        for (int i = 0; i < classes.length; ++i) {
            simpleNames[i] = getSimpleName(classes[i]);
        }
        return simpleNames;
    }

}

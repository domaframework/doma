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

import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import static org.seasar.aptina.commons.util.AssertionUtils.*;
import static org.seasar.aptina.commons.util.CollectionUtils.*;

/**
 * {@link Diagnostic} を扱うユーティリティです．
 * 
 * @author koichik
 */
public class DiagnosticUtils {

    private DiagnosticUtils() {
    }

    /**
     * {@link Diagnostic} のリストから， 指定されたクラスに対する {@link Diagnostic} のリストを返します．
     * 
     * @param diagnostics
     *            {@link Diagnostic} のリスト
     * @param clazz
     *            取得するクラス
     * @return 指定されたクラスに対する {@link Diagnostic} のリスト
     */
    public static List<Diagnostic<? extends JavaFileObject>> getDiagnostics(
            final List<Diagnostic<? extends JavaFileObject>> diagnostics,
            final Class<?> clazz) {
        assertNotNull("clazz", clazz);
        return getDiagnostics(diagnostics, clazz.getName());
    }

    /**
     * {@link Diagnostic} のリストから， 指定されたクラスに対する {@link Diagnostic} のリストを返します．
     * 
     * @param diagnostics
     *            {@link Diagnostic} のリスト
     * @param className
     *            取得するクラス名
     * @return 指定されたクラスに対する {@link Diagnostic} のリスト
     */
    public static List<Diagnostic<? extends JavaFileObject>> getDiagnostics(
            final List<Diagnostic<? extends JavaFileObject>> diagnostics,
            final String className) {
        assertNotNull("className", className);
        final String name = className.replace('.', '/') + ".java";
        final List<Diagnostic<? extends JavaFileObject>> result = newArrayList();
        for (final Diagnostic<? extends JavaFileObject> diagnositc : diagnostics) {
            final JavaFileObject source = diagnositc.getSource();
            if (source != null && source.toUri().toString().endsWith(name)) {
                result.add(diagnositc);
            }
        }
        return result;
    }

    /**
     * {@link Diagnostic} のリストから， 指定された {@link javax.tools.Diagnostic.Kind} を持つ
     * {@link Diagnostic} のリストを返します．
     * 
     * @param diagnostics
     *            {@link Diagnostic} のリスト
     * @param kind
     *            取得する {@link javax.tools.Diagnostic.Kind}
     * @return 指定された{@link javax.tools.Diagnostic.Kind} を持つ {@link Diagnostic}
     *         のリスト
     */
    public static List<Diagnostic<? extends JavaFileObject>> getDiagnostics(
            final List<Diagnostic<? extends JavaFileObject>> diagnostics,
            final javax.tools.Diagnostic.Kind kind) {
        assertNotNull("kind", kind);
        final List<Diagnostic<? extends JavaFileObject>> result = newArrayList();
        for (final Diagnostic<? extends JavaFileObject> diagnositc : diagnostics) {
            if (diagnositc.getKind().equals(kind)) {
                result.add(diagnositc);
            }
        }
        return result;
    }

    /**
     * {@link Diagnostic} のリストから， 指定されたクラスに対する指定された
     * {@link javax.tools.Diagnostic.Kind} を持つ {@link Diagnostic} のリストを返します．
     * 
     * @param diagnostics
     *            {@link Diagnostic} のリスト
     * @param clazz
     *            取得するクラス
     * @param kind
     *            取得する {@link javax.tools.Diagnostic.Kind}
     * @return 指定されたクラスに対する指定された {@link javax.tools.Diagnostic.Kind} を持つ
     *         {@link Diagnostic} のリスト
     */
    public static List<Diagnostic<? extends JavaFileObject>> getDiagnostics(
            final List<Diagnostic<? extends JavaFileObject>> diagnostics,
            final Class<?> clazz, final javax.tools.Diagnostic.Kind kind) {
        assertNotNull("clazz", clazz);
        assertNotNull("kind", kind);
        return getDiagnostics(diagnostics, clazz.getName(), kind);
    }

    /**
     * {@link Diagnostic} のリストから， 指定されたクラスに対する指定された
     * {@link javax.tools.Diagnostic.Kind} を持つ {@link Diagnostic} のリストを返します．
     * 
     * @param diagnostics
     *            {@link Diagnostic} のリスト
     * @param className
     *            取得するクラス名
     * @param kind
     *            取得する {@link javax.tools.Diagnostic.Kind}
     * @return 指定されたクラスに対する指定された {@link javax.tools.Diagnostic.Kind} を持つ
     *         {@link Diagnostic} のリスト
     */
    public static List<Diagnostic<? extends JavaFileObject>> getDiagnostics(
            final List<Diagnostic<? extends JavaFileObject>> diagnostics,
            final String className, final javax.tools.Diagnostic.Kind kind) {
        assertNotNull("className", className);
        assertNotNull("kind", kind);
        final String name = className.replace('.', '/') + ".java";
        final List<Diagnostic<? extends JavaFileObject>> result = newArrayList();
        for (final Diagnostic<? extends JavaFileObject> diagnositc : diagnostics) {
            final JavaFileObject source = diagnositc.getSource();
            if (source != null && source.toUri().toString().endsWith(name)
                    && diagnositc.getKind().equals(kind)) {
                result.add(diagnositc);
            }
        }
        return result;
    }

}

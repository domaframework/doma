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

import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.Modifier;

import static java.util.Collections.*;

/**
 * {@link Modifier} を扱うためのユーティリティです．
 * 
 * @author koichik
 */
public class ModifierUtils {

    /** 可視性を指定する修飾子の {@link Set} です． */
    public static final Set<Modifier> ACCESS_MODIFIERS;
    static {
        final HashSet<Modifier> set = new HashSet<Modifier>();
        set.add(Modifier.PUBLIC);
        set.add(Modifier.PROTECTED);
        set.add(Modifier.PRIVATE);
        ACCESS_MODIFIERS = unmodifiableSet(set);
    }

    private ModifierUtils() {
    }

    /**
     * 修飾子の可視性がデフォルト (パッケージ) の場合は {@code true} を返します．
     * 
     * @param modifiers
     *            修飾子
     * @return 修飾子の可視性がデフォルト (パッケージ) の場合は {@code true}
     */
    public static boolean isDefault(final Set<Modifier> modifiers) {
        return disjoint(modifiers, ACCESS_MODIFIERS);
    }

    /**
     * {@link Modifier} の {@link Set} を文字列化します．
     * <p>
     * {@link Set} が空の場合は空文字列が返されます． {@link Set} が空でない場合は，修飾子を Java
     * 言語仕様の順で並べた文字列を返します． 文字列の最後には空白が一つ付きます． これはソースを生成する際， 修飾子の後に {@literal
     * class} (クラス宣言の場合) や型名 (フィールド宣言やメソッドの場合) を続ける場合を考慮しています． 空白が不要な場合は
     * {@link String#trim()} してください．
     * </p>
     * 
     * @param modifiers
     *            {@link Modifier} の {@link Set}
     * @return {@link Modifier} の {@link Set} の文字列表現
     */
    public static String toStringOfModifiers(final Set<Modifier> modifiers) {
        if (modifiers.isEmpty()) {
            return "";
        }

        final StringBuilder buf = new StringBuilder(32);
        if (modifiers.contains(Modifier.PUBLIC)) {
            buf.append("public ");
        }
        if (modifiers.contains(Modifier.PROTECTED)) {
            buf.append("protected ");
        }
        if (modifiers.contains(Modifier.PRIVATE)) {
            buf.append("private ");
        }
        if (modifiers.contains(Modifier.ABSTRACT)) {
            buf.append("abstract ");
        }
        if (modifiers.contains(Modifier.STATIC)) {
            buf.append("static ");
        }
        if (modifiers.contains(Modifier.FINAL)) {
            buf.append("final ");
        }
        if (modifiers.contains(Modifier.TRANSIENT)) {
            buf.append("transient ");
        }
        if (modifiers.contains(Modifier.VOLATILE)) {
            buf.append("volatile ");
        }
        if (modifiers.contains(Modifier.SYNCHRONIZED)) {
            buf.append("synchronized ");
        }
        if (modifiers.contains(Modifier.NATIVE)) {
            buf.append("native ");
        }
        if (modifiers.contains(Modifier.STRICTFP)) {
            buf.append("strictfp ");
        }
        return new String(buf);
    }

}

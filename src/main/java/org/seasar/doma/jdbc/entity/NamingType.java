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
package org.seasar.doma.jdbc.entity;

import org.seasar.doma.Column;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.Entity;
import org.seasar.doma.Table;
import org.seasar.doma.Transient;
import org.seasar.doma.internal.util.StringUtil;

/**
 * ネーミング規約を表します。
 * <p>
 * エンティティ名とプロパティ名に規約を適用し、テーブル名とカラム名を求めます。
 * <ul>
 * <li>エンティティ名とは {@link Entity} が注釈されたクラスの単純名です。
 * <li>プロパティ名とは {@code Entity} が注釈されたクラスに属するフィールド（ただし、 {@link Transient}
 * が注釈されたものは除く ）の名前です。
 * </ul>
 * <p>
 * エンティティクラスに {@link Table#name()} が指定されていない場合や、フィールドに {@link Column#name()}
 * が指定されていない場合にこの規約が適用されます。
 * <p>
 * 
 * @author taedium
 * 
 */
public enum NamingType {

    /**
     * 何も行いません。
     */
    NONE {

        @Override
        public String apply(String text) {
            return text;
        }

        @Override
        public String revert(String text) {
            return text;
        }

    },

    /**
     * スネークケースの大文字に変換します。
     * <p>
     * たとえば、<code>aaaBbb</code> を <code>AAA_BBB</code> に変換します。
     */
    SNAKE_UPPER_CASE {

        @Override
        public String apply(String text) {
            if (text == null) {
                throw new DomaNullPointerException("text");
            }
            String s = StringUtil.fromCamelCaseToSnakeCase(text);
            return s.toUpperCase();
        }

        @Override
        public String revert(String text) {
            if (text == null) {
                throw new DomaNullPointerException("text");
            }
            return StringUtil.fromSnakeCaseToCamelCase(text);
        }

    },

    /**
     * スネークケースの小文字に変換します。
     * <p>
     * たとえば、<code>aaaBbb</code> を <code>aaa_bbb</code> に変換します。
     */
    SNAKE_LOWER_CASE {

        @Override
        public String apply(String text) {
            if (text == null) {
                throw new DomaNullPointerException("text");
            }
            String s = StringUtil.fromCamelCaseToSnakeCase(text);
            return s.toLowerCase();
        }

        @Override
        public String revert(String text) {
            if (text == null) {
                throw new DomaNullPointerException("text");
            }
            return StringUtil.fromSnakeCaseToCamelCase(text);
        }

    },

    /**
     * 大文字に変換します。
     * <p>
     * たとえば、<code>aaaBbb</code> を <code>AAABBB</code> に変換します。
     */
    UPPER_CASE {

        @Override
        public String apply(String text) {
            if (text == null) {
                throw new DomaNullPointerException("text");
            }
            return text.toUpperCase();
        }

        @Override
        public String revert(String text) {
            if (text == null) {
                throw new DomaNullPointerException("text");
            }
            return text.toLowerCase();
        }

    },

    /**
     * 小文字に変換します。
     * <p>
     * たとえば、<code>aaaBbb</code> を <code>aaabbb</code> に変換します。
     */
    LOWER_CASE {

        @Override
        public String apply(String text) {
            if (text == null) {
                throw new DomaNullPointerException("text");
            }
            return text.toLowerCase();
        }

        @Override
        public String revert(String text) {
            if (text == null) {
                throw new DomaNullPointerException("text");
            }
            return text;
        }

    };

    /**
     * ネーミング規約を適用します。
     * 
     * @param text
     *            規約が適用される文字列
     * @return 規約が適用された文字列
     */
    public abstract String apply(String text);

    /**
     * ネーミング規約が適用された文字列を適用前の文字列に戻します。
     * <p>
     * 正確に元に戻せないことがあります。
     * 
     * @param text
     *            ネーミング規約適用後の文字列
     * @return ネーミング規約適用前の文字列
     */
    public abstract String revert(String text);
}
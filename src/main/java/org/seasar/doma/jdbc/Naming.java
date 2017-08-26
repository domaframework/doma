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
package org.seasar.doma.jdbc;

import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.Entity;
import org.seasar.doma.jdbc.entity.NamingType;

/**
 * ネーミング規約の適用を制御します。
 * 
 * @author nakamura-to
 *
 * @since 2.2.0
 */
public interface Naming {

    /** {@link NamingType#NONE} へのアダプタ */
    static Naming NONE = new Adapter(NamingType.NONE);

    /** {@link NamingType#LOWER_CASE} へのアダプタ */
    static Naming LOWER_CASE = new Adapter(NamingType.LOWER_CASE);

    /** {@link NamingType#UPPER_CASE} へのアダプタ */
    static Naming UPPER_CASE = new Adapter(NamingType.UPPER_CASE);

    /** {@link NamingType#SNAKE_LOWER_CASE} へのアダプタ */
    static Naming SNAKE_LOWER_CASE = new Adapter(NamingType.SNAKE_LOWER_CASE);

    /** {@link NamingType#SNAKE_UPPER_CASE} へのアダプタ */
    static Naming SNAKE_UPPER_CASE = new Adapter(NamingType.SNAKE_UPPER_CASE);

    /** {@link NamingType#LENIENT_SNAKE_LOWER_CASE} へのアダプタ */
    static Naming LENIENT_SNAKE_LOWER_CASE = new Adapter(NamingType.LENIENT_SNAKE_LOWER_CASE);

    /** {@link NamingType#LENIENT_SNAKE_UPPER_CASE} へのアダプタ */
    static Naming LENIENT_SNAKE_UPPER_CASE = new Adapter(NamingType.LENIENT_SNAKE_UPPER_CASE);

    /** デフォルトの規約 */
    static Naming DEFAULT = NONE;

    /**
     * ネーミング規約を適用します。
     * 
     * @param namingType
     *            ネーミング規約。{@literal null} の場合、 {@link Entity#naming()}
     *            に明示的な指定が存在しないことを意味します。
     * @param text
     *            規約が適用される文字列
     * @return 規約が適用された文字列
     * @throws DomaNullPointerException
     *             {@code text} が {@literal null} の場合
     */
    String apply(NamingType namingType, String text);

    /**
     * ネーミング規約が適用された文字列を適用前の文字列に戻します。
     * <p>
     * 正確に元に戻せないことがあります。
     * 
     * @param namingType
     *            ネーミング規約。{@literal null} の場合、 {@link Entity#naming()}
     *            に明示的な指定が存在しないことを意味します。
     * @param text
     *            ネーミング規約適用後の文字列
     * @return ネーミング規約適用前の文字列
     * @throws DomaNullPointerException
     *             {@code text} が {@literal null} の場合
     */
    String revert(NamingType namingType, String text);

    /**
     * {@link NamingType} へのアダプタです。
     * 
     * @author nakamura-to
     */
    static class Adapter implements Naming {

        protected final NamingType namingType;

        private Adapter(NamingType namingType) {
            if (namingType == null) {
                throw new DomaNullPointerException("namingType");
            }
            this.namingType = namingType;
        }

        @Override
        public String apply(NamingType namingType, String text) {
            if (text == null) {
                throw new DomaNullPointerException("text");
            }
            if (namingType == null) {
                return this.namingType.apply(text);
            }
            return namingType.apply(text);
        }

        @Override
        public String revert(NamingType namingType, String text) {
            if (text == null) {
                throw new DomaNullPointerException("text");
            }
            if (namingType == null) {
                return this.namingType.revert(text);
            }
            return namingType.revert(text);
        }
    }

}

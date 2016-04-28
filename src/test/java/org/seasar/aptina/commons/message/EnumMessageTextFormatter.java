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
package org.seasar.aptina.commons.message;

import java.text.MessageFormat;
import java.util.Formatter;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 列挙に定義された {@link MessageFormat} のパターンを使用してメッセージを組み立てるクラスです．
 * <p>
 * {@link Formatter} のパターンを使用する場合は {@link EnumMessageFormatter} を使用してください．
 * </p>
 * <p>
 * パターンを定義した列挙は {@link EnumMessageCode} を実装していなければなりません．
 * </p>
 * 
 * @author koichik
 * @param <T>
 *            パターンを定義した列挙の型
 */
public class EnumMessageTextFormatter<T extends Enum<T> & EnumMessageCode> {

    /** リソースバンドル */
    protected final ResourceBundle bundle;

    /**
     * デフォルトロケールでインスタンスを構築します．
     * 
     * @param enumClass
     *            パターンを定義した列挙の型
     */
    public EnumMessageTextFormatter(final Class<T> enumClass) {
        bundle = EnumMessageResourceBundle.getBundle(enumClass);
    }

    /**
     * ロケールを指定してインスタンスを構築します．
     * 
     * @param enumClass
     *            パターンを定義した列挙の型
     * @param locale
     *            ロケール
     */
    public EnumMessageTextFormatter(final Class<T> enumClass,
            final Locale locale) {
        bundle = EnumMessageResourceBundle.getBundle(enumClass, locale);
    }

    /**
     * 列挙に定義されたパターンを返します．
     * 
     * @param messageCode
     *            列挙
     * @return 列挙に定義されたパターン
     */
    public String getPattern(final T messageCode) {
        return bundle.getString(messageCode.name());
    }

    /**
     * 列挙に定義されたパターンを使用してメッセージを作成して返します．
     * 
     * @param messageCode
     *            列挙
     * @param args
     *            引数
     * @return 列挙に定義されたパターンを使用して作成したメッセージ
     */
    public String getMessage(final T messageCode, final Object... args) {
        return MessageFormat.format(getPattern(messageCode), args);
    }

}

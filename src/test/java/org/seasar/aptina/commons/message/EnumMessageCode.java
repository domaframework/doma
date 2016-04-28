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

import java.util.Locale;

import javax.tools.Diagnostic.Kind;

/**
 * メッセージコードを定義した列挙が実装するインタフェースです．
 * <p>
 * メッセージコードを記述した列挙はそれぞれの列挙定数に {@link javax.tools.Diagnostic.Kind 診断レベル} と，
 * ロケールごとのメッセージを持ちます． サポートするメッセージのロケールは {@code SUPORTED_LOCALES} という名前の {@code
 * static} フィールドに {@link Locale} の配列として定義します．
 * </p>
 * 
 * <pre>
 * public static final Locale[] SUPORTED_LOCALES = new Locale[] { Locale.ROOT, Locale.JAPANESE };
 * </pre>
 * <p>
 * {@code SUPPORTED_LOCALES} 配列には {@link Locale#ROOT} を含めるべきです．
 * {@link #getMessageFormat(int)} の引数には， {@code SUPPORTED_LOCALES}
 * 配列のインデックスがロケールとして渡されます．
 * </p>
 * <p>
 * 典型的な列挙の定義は次のようになります．
 * </p>
 * 
 * <pre>
 * public enum TestMessageCode implements EnumMessageCode {
 *     // 第 2 引数はルートロケールのメッセージ，第 3 引数は日本語ロケールのメッセージ
 *     XXXX(Kind.ERROR,   "Error",   "エラー"),
 *     YYYY(Kind.WARNING, "Warning", "警告"),
 *     ...
 *     ;
 * 
 *     &#x2F;** サポートするロケールの配列 *&#x2F;
 *     public static final Locale[] SUPPORTED_LOCALES = new Locale[] {
 *             Locale.ROOT, Locale.JAPANESE };
 * 
 *     &#x2F;** 診断の種類 *&#x2F;
 *     private final Kind kind;
 * 
 *     &#x2F;** メッセージフォーマット *&#x2F;
 *     private final String[] messageFormats;
 * 
 *     &#x2F;**
 *      * インスタンスを構築します．
 *      * 配列の要素は SUPPORTED_LOCALES 配列のロケールに対応するメッセージフォーマットです．
 *      * 
 *      * &#x40;param messageFormats
 *      *            メッセージフォーマットの配列
 *      *&#x2F;
 *     private TestMessageCode(final Kind kind, final String... messageFormats) {
 *         this.kind = kind;
 *         this.messageFormats = messageFormats;
 *     }
 * 
 *     &#x2F;**
 *      * 診断の種類を返します．
 *      * 
 *      * &#x40;return 診断の種類
 *      *&#x2F;
 *     public Kind getKind() {
 *         return kind;
 *     }
 * 
 *     &#x2F;**
 *      * 指定されたロケールのメッセージフォーマットを返します．
 *      * 
 *      * &#x40;param locale
 *      *            ロケール
 *      * &#x40;return 指定されたロケールのメッセージフォーマット
 *      *&#x2F;
 *     public String getMessageFormat(final int locale) {
 *         return messageFormats[locale];
 *     }
 * }
 * </pre>
 * 
 * @author koichik
 */
public interface EnumMessageCode {

    /** サポートするロケールの配列を定義した定数の名前 */
    String SUPPORTED_LOCALES_NAME = "SUPPORTED_LOCALES";

    /**
     * 診断の種類を返します．
     * 
     * @return 診断の種類
     */
    Kind getKind();

    /**
     * 指定されたロケールのメッセージを返します．
     * 
     * @param locale
     *            ロケールのインデックス
     * @return 指定されたロケールのメッセージ
     */
    String getMessageFormat(final int locale);

}

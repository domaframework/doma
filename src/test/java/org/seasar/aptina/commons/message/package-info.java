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
/**
 * プロパティファイルの代わりに列挙に記述されたメッセージを使用する 
 * {@link java.util.ResourceBundle} を提供します．
 * <p>
 * メッセージコードを記述した列挙は，
 * {@link org.seasar.aptina.commons.message.EnumMessageCode} を実装し， 列挙定数ごとに
 * {@link javax.tools.Diagnostic.Kind 診断レベル} と， ロケールごとのメッセージを持ちます．
 * 列挙がサポートするメッセージのロケールは {@literal SUPORTED_LOCALES} という名前の
 * {@code static} フィールドに {@link java.util.Locale} の配列として定義します．
 * {@code SUPPORTED_LOCALES} 配列には {@link java.util.Locale#ROOT} を含めるべきです．
 * {@link org.seasar.aptina.commons.message.EnumMessageCode#getMessageFormat(int)} の引数には，
 * {@code SUPPORTED_LOCALES} 配列のインデックスがロケールとして渡されます．
 * </p>
 * <p>
 * メッセージを定義した列挙は次のようになります．
 * </p>
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
 * <p>
 * 列挙を指定して {@link java.util.ResourceBundle} を取得するには，
 * {@link org.seasar.aptina.commons.message.EnumMessageResourceBundle} の
 * {@code static} メソッドを使用します．
 * </p>
 * <pre>
 * ResourceBundle bundle = EnumMessageResourceBundle.getBundle(XxxMessageCode.class);
 * </pre>
 * <p>
 * 列挙の文字列表現をキーとしてメッセージを取得することができます．
 * </p>
 * <pre>
 * String message = bundle.getString(WARN.name());
 * </pre>
 * <p>
 * 列挙のメッセージとして {@link java.util.Formatter} のパターンを定義した場合は
 * {@link org.seasar.aptina.commons.message.EnumMessageFormatter} を使用することができます．
 * </p>
 * <pre>
 * public enum YyyMessageCode implements EnumMessageCode {
 *     E0000(Kind.ERROR, "Exception occurred : %1$s%n", "例外が発生しました : %1$s%n"),
 *     ...
 * </pre>
 * <pre>
 * EnumMessageFormatter&lt;YyyMessageCode&gt; formatter = 
 *     new EnumMessageFormatter&lt;TestMessageCode&gt;(YyyMessageCode.class);
 * String message = formatter.getMessage(E0000, e);
 * </pre>
 * <p>
 * または， 
 * {@link org.seasar.aptina.commons.message.EnumMessageFormatter}
 * の出力先を指定してインスタンス化して使うこともできます．
 * </p>
 * <pre>
 * StringBuilder builder = new StringBuilder();
 * EnumMessageFormatter&lt;YyyMessageCode&gt; formatter = 
 *     new EnumMessageFormatter&lt;YyyMessageCode&gt;(YyyMessageCode.class, builder);
 * formatter.format(E0000, e); // builder にフォーマットされた文字列が追加される
 * </pre>
 * <p>
 * 列挙のメッセージとして {@link java.text.MessageFormat} のパターンを定義した場合は
 * {@link org.seasar.aptina.commons.message.EnumMessageTextFormatter} を使用することができます．
 * </p>
 * <pre>
 * public enum ZzzMessageCode implements EnumMessageCode {
 *     E0000(Kind.ERROR, "Exception occurred : {0}", "例外が発生しました : {0}"),
 *     ...
 * </pre>
 * <pre>
 * EnumMessageTextFormatter&lt;ZzzMessageCode&gt; formatter = 
 *     new EnumMessageTextFormatter&lt;ZzzMessageCode&gt;(ZzzMessageCode.class);
 * String message = formatter.getMessage(E0000, e);
 * </pre>
 */
package org.seasar.aptina.commons.message;


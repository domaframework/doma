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
package org.seasar.doma.message;

/**
 * メッセージのリソースを表します。
 * <p>
 * このインタフェースの実装はスレッドセーフでなければいけません。
 * <p>
 * 
 * @author taedium
 * 
 */
public interface MessageResource {

    /**
     * 一意のコードを返します。
     * 
     * @return コード
     */
    String getCode();

    /**
     * メッセージパターンの文字列を返します。
     * <p>
     * この文字列は{0}や{1}といった置換パラメータを含みます。
     * 
     * @return メッセージパターンの文字列
     */
    String getMessagePattern();

    /**
     * メッセージコードを含んだメッセージを返します。
     * <p>
     * メッセージパターンに含まれる置換パラメータは引数により解決されます。
     * 
     * @param args
     *            置換パラメータに対応する引数
     * @return メッセージ
     */
    String getMessage(Object... args);

    /**
     * メッセージコードを含まないメッセージを返します。
     * <p>
     * メッセージパターンに含まれる置換パラメータは引数により解決されます。
     * 
     * @param args
     *            置換パラメータに対応する引数
     * @return メッセージ
     */
    String getSimpleMessage(Object... args);
}

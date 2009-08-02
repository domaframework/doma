/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.doma.converter;

/**
 * 値を特定の型へ変換するコンバーターです。
 * <p>
 * このインタフェースの実装はスレッドセーフでなければいけません。
 * <p>
 * 
 * @author taedium
 * 
 * @param <T>
 *            変換先の型
 */
public interface Converter<T> {

    /**
     * 変換します。
     * 
     * @param value
     *            値
     * @param pattern
     *            変換時に利用するパターン
     * @return 変換された値、値が {@code null} の場合は {@code null}
     * @throws ConversionException
     *             変換に失敗した場合
     */
    T convert(Object value, String pattern) throws ConversionException;

}

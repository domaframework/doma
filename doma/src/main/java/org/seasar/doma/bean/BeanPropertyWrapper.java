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
package org.seasar.doma.bean;

/**
 * {@literal JavaBeans} のプロパティのラッパーです。
 * <p>
 * このインタフェースの実装はスレッドセーフであることを要求されません。
 * </p>
 * 
 * @author taedium
 * 
 */
public interface BeanPropertyWrapper {

    /**
     * 名前を返します。
     * 
     * @return 名前
     */
    String getName();

    /**
     * プロパティから値を取得できるかどうかを返します。
     * 
     * @return 値を返却できる場合 {@code true}
     */
    boolean isValueGettable();

    /**
     * プロパティの値を取得します。
     * 
     * @return 値
     * @throws PropertyReadAccessException
     *             取得に失敗した場合
     */
    Object getValue();

    /**
     * プロパティに値を設定できるかどうかを返します。
     * 
     * @return 値を設定できる場合 {@code true}
     */
    boolean isValueSettable();

    /**
     * プロパティに値を設定します。
     * 
     * @param value
     * @throws PropertyWriteAccessException
     */
    void setValue(Object value);

    /**
     * プロパティのクラスを返します。
     * 
     * @return プロパティのクラス
     */
    Class<?> getPropertyClass();

}

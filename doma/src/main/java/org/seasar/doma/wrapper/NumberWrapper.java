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
package org.seasar.doma.wrapper;

/**
 * {@link Number} のラッパーです。
 * 
 * @author taedium
 * 
 * @param <V>
 *            値の型
 */
public interface NumberWrapper<V extends Number> extends Wrapper<V> {

    @Override
    void set(Number value);

    /**
     * 値をインクリメントします。
     */
    void increment();

    /**
     * 値をデクリメントします。
     */
    void decrement();

    /**
     * インクリメントした値を取得します。
     * 
     * @return インクリメントした値
     */
    V getIncrementedValue();

    /**
     * デクリメントした値を取得します。
     * 
     * @return デクリメントした値
     */
    V getDecrementedValue();
}

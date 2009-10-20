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
package org.seasar.doma.wrapper;

import org.seasar.doma.DomaNullPointerException;

/**
 * {@link Wrapper} の骨格実装です。
 * 
 * @author taedium
 * 
 */
public abstract class AbstractWrapper<V> implements Wrapper<V> {

    /** 値 */
    protected V value;

    /**
     * インスタンスを構築します。
     */
    public AbstractWrapper() {
    }

    /**
     * 値を指定してインスタンスを構築します。
     * 
     * @param value
     *            値
     */
    public AbstractWrapper(V value) {
        this.value = value;
    }

    @Override
    public void set(V v) {
        value = v;
    }

    @Override
    public V get() {
        return value;
    }

    @Override
    public boolean isEqual(Wrapper<?> other) {
        if (other == null) {
            throw new DomaNullPointerException("other");
        }
        if (value == null) {
            return other.get() == null;
        }
        return value.equals(other.get());
    }

    @Override
    public String toString() {
        return value != null ? value.toString() : null;
    }

}

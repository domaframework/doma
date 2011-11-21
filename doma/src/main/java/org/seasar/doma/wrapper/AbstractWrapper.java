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
 * {@link Wrapper} の骨格実装です。
 * 
 * @author taedium
 * 
 */
public abstract class AbstractWrapper<V> implements Wrapper<V> {

    /** 値 */
    protected V value;

    protected Accessor<V> accessor;

    /**
     * インスタンスを構築します。
     */
    protected AbstractWrapper() {
    }

    /**
     * 値を指定してインスタンスを構築します。
     * 
     * @param value
     *            値
     */
    protected AbstractWrapper(V value) {
        doSet(value);
    }

    @Override
    public final void set(V value) {
        doSet(value);
    }

    /**
     * 値を設定します。
     * 
     * @param value
     *            値
     */
    protected void doSet(V value) {
        if (accessor != null) {
            accessor.set(value);
        } else {
            this.value = value;
        }
    }

    @Override
    public final V get() {
        return doGet();
    }

    /**
     * 値を返します。
     * 
     * @return 値
     */
    protected V doGet() {
        if (accessor != null) {
            return accessor.get();
        }
        return value;
    }

    @Override
    public final V getCopy() {
        return doGetCopy();
    }

    /**
     * 値のコピーを返します。
     * 
     * @return 値のコピーを返します。
     */
    protected V doGetCopy() {
        return doGet();
    }

    @Override
    public V getDefault() {
        return null;
    }

    @Override
    public final boolean hasEqualValue(Object otherValue) {
        return doHasEqualValue(otherValue);
    }

    /**
     * 等しい値を持っているかどうか判定します。
     * 
     * @param otherValue
     *            値
     * @return 等しい値を持っている場合 {@code true}
     */
    protected boolean doHasEqualValue(Object otherValue) {
        V value = doGet();
        if (value == null) {
            return otherValue == null;
        }
        return value.equals(otherValue);
    }

    @Override
    public void setAccessor(Accessor<V> accessor) {
        doSetAccessor(accessor);
    }

    protected void doSetAccessor(Accessor<V> accessor) {
        this.accessor = accessor;
    }

}

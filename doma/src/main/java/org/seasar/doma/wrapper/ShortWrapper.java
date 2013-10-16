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

import org.seasar.doma.DomaNullPointerException;

/**
 * {@link Short} のラッパーです。
 * 
 * @author taedium
 * 
 */
public class ShortWrapper extends AbstractWrapper<Short> implements
        NumberWrapper<Short> {

    /**
     * インスタンスを構築します。
     */
    public ShortWrapper() {
        super(Short.class);
    }

    /**
     * 値を指定してインスタンスを構築します。
     * 
     * @param value
     *            値
     */
    public ShortWrapper(Short value) {
        super(Short.class, value);
    }

    @Override
    public void set(Number v) {
        super.set(v.shortValue());
    }

    @Override
    public Short getDefault() {
        return Short.valueOf((short) 0);
    }

    @Override
    public void increment() {
        Short value = doGet();
        if (value != null) {
            doSet((short) (value.shortValue() + 1));
        }
    }

    @Override
    public void decrement() {
        Short value = doGet();
        if (value != null) {
            doSet((short) (value.shortValue() - 1));
        }
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            WrapperVisitor<R, P, TH> visitor, P p) throws TH {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        return visitor.visitShortWrapper(this, p);
    }
}

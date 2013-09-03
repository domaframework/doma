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
 * {@link Long} のラッパーです。
 * 
 * @author taedium
 * 
 */
public class LongWrapper extends AbstractWrapper<Long> implements
        NumberWrapper<Long> {

    /**
     * インスタンスを構築します。
     */
    public LongWrapper() {
    }

    /**
     * 値を指定してインスタンスを構築します。
     * 
     * @param value
     *            値
     */
    public LongWrapper(Long value) {
        super(value);
    }

    @Override
    public void set(Number v) {
        super.set(v.longValue());
    }

    @Override
    public Long getDefault() {
        return Long.valueOf(0L);
    }

    @Override
    public void increment() {
        Long value = getIncrementedValue();
        if (value != null) {
            doSet(value);
        }
    }

    @Override
    public void decrement() {
        Long value = getDecrementedValue();
        if (value != null) {
            doSet(value);
        }
    }

    @Override
    public Long getIncrementedValue() {
        Long value = doGet();
        if (value != null) {
            return Long.valueOf((value.longValue() + 1L));
        }
        return null;
    }

    @Override
    public Long getDecrementedValue() {
        Long value = doGet();
        if (value != null) {
            return Long.valueOf(value.longValue() - 1L);
        }
        return null;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            WrapperVisitor<R, P, TH> visitor, P p) throws TH {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        if (visitor instanceof LongWrapperVisitor<?, ?, ?>) {
            LongWrapperVisitor<R, P, TH> v = (LongWrapperVisitor<R, P, TH>) visitor;
            return v.visitLongWrapper(this, p);
        }
        return visitor.visitUnknownWrapper(this, p);
    }
}

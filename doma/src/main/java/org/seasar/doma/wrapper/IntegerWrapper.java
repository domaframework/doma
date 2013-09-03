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
 * {@link Integer} のラッパーです。
 * 
 * @author taedium
 * 
 */
public class IntegerWrapper extends AbstractWrapper<Integer> implements
        NumberWrapper<Integer> {

    /**
     * インスタンスを構築します。
     */
    public IntegerWrapper() {
    }

    /**
     * 値を指定してインスタンスを構築します。
     * 
     * @param value
     *            値
     */
    public IntegerWrapper(Integer value) {
        super(value);
    }

    @Override
    public void set(Number v) {
        set(v.intValue());
    }

    @Override
    public Integer getDefault() {
        return Integer.valueOf(0);
    }

    @Override
    public void increment() {
        Integer value = getIncrementedValue();
        if (value != null) {
            doSet(value);
        }
    }

    @Override
    public void decrement() {
        Integer value = getDecrementedValue();
        if (value != null) {
            doSet(value);
        }
    }

    @Override
    public Integer getIncrementedValue() {
        Integer value = doGet();
        if (value != null) {
            return Integer.valueOf((value.intValue() + 1));
        }
        return null;
    }

    @Override
    public Integer getDecrementedValue() {
        Integer value = doGet();
        if (value != null) {
            return Integer.valueOf(value.intValue() - 1);
        }
        return null;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            WrapperVisitor<R, P, TH> visitor, P p) throws TH {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        if (visitor instanceof IntegerWrapperVisitor<?, ?, ?>) {
            IntegerWrapperVisitor<R, P, TH> v = (IntegerWrapperVisitor<R, P, TH>) visitor;
            return v.visitIntegerWrapper(this, p);
        }
        return visitor.visitUnknownWrapper(this, p);
    }
}

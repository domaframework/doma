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
 * {@link Double} のラッパーです。
 * 
 * @author taedium
 * 
 */
public class DoubleWrapper extends AbstractWrapper<Double> implements
        NumberWrapper<Double> {

    /**
     * インスタンスを構築します。
     */
    public DoubleWrapper() {
    }

    /**
     * 値を指定してインスタンスを構築します。
     * 
     * @param value
     *            値
     */
    public DoubleWrapper(Double value) {
        super(value);
    }

    @Override
    public void set(Number v) {
        super.set(v.doubleValue());
    }

    @Override
    public Double getDefault() {
        return Double.valueOf(0d);
    }

    @Override
    public void increment() {
        Double value = getIncrementedValue();
        if (value != null) {
            doSet(value);
        }
    }

    @Override
    public void decrement() {
        Double value = getDecrementedValue();
        if (value != null) {
            doSet(value);
        }
    }

    @Override
    public Double getIncrementedValue() {
        Double value = doGet();
        if (value != null) {
            return Double.valueOf((value.doubleValue() + 1d));
        }
        return null;
    }

    @Override
    public Double getDecrementedValue() {
        Double value = doGet();
        if (value != null) {
            return Double.valueOf(value.doubleValue() - 1d);
        }
        return null;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            WrapperVisitor<R, P, TH> visitor, P p) throws TH {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        if (visitor instanceof DoubleWrapperVisitor<?, ?, ?>) {
            DoubleWrapperVisitor<R, P, TH> v = (DoubleWrapperVisitor<R, P, TH>) visitor;
            return v.visitDoubleWrapper(this, p);
        }
        return visitor.visitUnknownWrapper(this, p);
    }
}

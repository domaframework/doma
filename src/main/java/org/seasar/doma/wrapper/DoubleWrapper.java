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
 * A wrapper for the {@link Double} class.
 */
public class DoubleWrapper extends AbstractWrapper<Double> implements NumberWrapper<Double> {

    public DoubleWrapper() {
        super(Double.class);
    }

    public DoubleWrapper(Double value) {
        super(Double.class, value);
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
        Double value = doGet();
        if (value != null) {
            doSet(value.doubleValue() + 1d);
        }
    }

    @Override
    public void decrement() {
        Double value = doGet();
        if (value != null) {
            doSet(value.doubleValue() - 1d);
        }
    }

    @Override
    public <R, P, Q, TH extends Throwable> R accept(WrapperVisitor<R, P, Q, TH> visitor, P p, Q q)
            throws TH {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        return visitor.visitDoubleWrapper(this, p, q);
    }
}

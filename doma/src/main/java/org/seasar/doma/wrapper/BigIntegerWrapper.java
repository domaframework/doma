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

import java.math.BigDecimal;
import java.math.BigInteger;

import org.seasar.doma.DomaNullPointerException;

/**
 * {@link BigInteger} のラッパーです。
 * 
 * @author taedium
 * 
 */
public class BigIntegerWrapper extends AbstractWrapper<BigInteger> implements
        NumberWrapper<BigInteger> {

    /**
     * インスタンスを構築します。
     */
    public BigIntegerWrapper() {
    }

    /**
     * 値を指定してインスタンスを構築します。
     * 
     * @param value
     *            値
     */
    public BigIntegerWrapper(BigInteger value) {
        super(value);
    }

    @Override
    public void set(Number v) {
        if (v instanceof BigInteger) {
            super.set((BigInteger) v);
        } else if (v instanceof BigDecimal) {
            super.set(((BigDecimal) v).toBigInteger());
        } else {
            super.set(BigInteger.valueOf(v.longValue()));
        }
    }

    @Override
    public void increment() {
        BigInteger value = getIncrementedValue();
        if (value != null) {
            doSet(value);
        }
    }

    @Override
    public void decrement() {
        BigInteger value = getDecrementedValue();
        if (value != null) {
            doSet(value);
        }
    }

    @Override
    public BigInteger getIncrementedValue() {
        BigInteger value = doGet();
        if (value != null) {
            return value.add(BigInteger.ONE);
        }
        return null;
    }

    @Override
    public BigInteger getDecrementedValue() {
        BigInteger value = doGet();
        if (value != null) {
            return value.subtract(BigInteger.ONE);
        }
        return null;
    }

    @Override
    public <R, P, TH extends Throwable> R accept(
            WrapperVisitor<R, P, TH> visitor, P p) throws TH {
        if (visitor == null) {
            throw new DomaNullPointerException("visitor");
        }
        if (visitor instanceof BigIntegerWrapperVisitor<?, ?, ?>) {
            BigIntegerWrapperVisitor<R, P, TH> v = (BigIntegerWrapperVisitor<R, P, TH>) visitor;
            return v.visitBigIntegerWrapper(this, p);
        }
        return visitor.visitUnknownWrapper(this, p);
    }
}
